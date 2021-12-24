package hk.eric.funnymod.modules.combat.killaura;

import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.events.Render3DEvent;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.modules.combat.KillAuraModule;
import hk.eric.funnymod.utils.EntityUtil;
import hk.eric.funnymod.utils.PacketUtil;
import hk.eric.funnymod.utils.PlayerUtil;
import hk.eric.funnymod.utils.RenderUtils;
import hk.eric.funnymod.utils.classes.ThreeDimensionalLine;
import hk.eric.funnymod.utils.classes.XYRot;
import hk.eric.funnymod.utils.classes.minecraftPlus.BetterBlockPos;
import hk.eric.funnymod.utils.classes.pathFind.AStarPathFinder;
import hk.eric.funnymod.utils.classes.pathFind.Node;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.stream.Stream;

public class InfiniteKillAuraMode extends KillauraMode {

    private int packetThisTick = 0;

    private static final Set<ThreeDimensionalLine> linesToDraw = new HashSet<>();
    private static final Set<AABB> boxesToDraw = new HashSet<>();

    final Comparator<LivingEntity> armorEntitySorter = Comparator.comparingInt(EntityUtil::rateArmor);
    final Comparator<LivingEntity> healthEntitySorter = Comparator.comparingDouble(LivingEntity::getHealth).reversed();

    public Comparator<LivingEntity> getEntityComparator(LocalPlayer player, KillAuraModule.SortType sortType) {
        return switch (sortType) {
            case DISTANCE -> Comparator.comparingDouble(e -> EntityUtil.distanceToEntitySquared(player, e));
            case HEALTH -> healthEntitySorter;
            case ARMOR -> armorEntitySorter;
        };
    }

    @Override
    public Stream<LivingEntity> process(Stream<LivingEntity> entityStream) {
        return entityStream
                .filter(e -> EntityUtil.distanceToEntitySquared(e, FunnyModClient.mc.player) <= KillAuraModule.infiniteAuraRange.getValue() * KillAuraModule.infiniteAuraRange.getValue())
                .sorted(getEntityComparator(FunnyModClient.mc.player, KillAuraModule.sortType.getValue()))
                .limit(KillAuraModule.infiniteAuraTargetLimit.getValue());
    }

    @Override
    public void attack(LivingEntity entity) {
        LocalPlayer player = FunnyModClient.mc.player;
        assert player != null;
        Node path = AStarPathFinder.search(BetterBlockPos.fromEntity(player).asNode(), BetterBlockPos.fromEntity(entity).asNode(), KillAuraModule.infiniteAuraMaxStep.getValue(), true, true);
        if (path == null) return;
        List<Node> pathAsList = path.asList();
        pathAsList.remove(pathAsList.size() - 1);
        if (!canFinish(pathAsList.size() + 2)) return;
        Queue<Node> nodes = new ArrayDeque<>(pathAsList);
        Node oldNode = nodes.remove();
        while (!nodes.isEmpty()) {
            Node node = nodes.poll();
            Vec3 pos = node.getPos().toCenteredVec3();
            Vec3 oPos = oldNode.getPos().toCenteredVec3();
            double oX = oPos.x, oY = oPos.y, oZ = oPos.z;
            double x = pos.x() ,y = pos.y(), z = pos.z();
            linesToDraw.add(new ThreeDimensionalLine(oX, oY, oZ, x, y, z));
            boxesToDraw.add(player.getDimensions(player.getPose()).makeBoundingBox(pos));
            if (!nodes.isEmpty()) {
                sendPacket(PacketUtil.createPos(x, y, z, true));
            }else {
                XYRot rot = PlayerUtil.getRotFromCoordinate(player, entity.getX(), entity.getY() + entity.getBbHeight()/2, entity.getZ());
                sendPacket(PacketUtil.createPosRot(x, y, z, rot.getYaw(), rot.getPitch(), true));
            }
            oldNode = node;
        }
        //TODO: Optimize Sides (combine long lines of the same direction)
        //TODO: Dont move on last step, and set Rot on the second to last step
        sendPacket(ServerboundInteractPacket.createAttackPacket(entity, false));
        player.swing(InteractionHand.MAIN_HAND);
        player.resetAttackStrengthTicker();
    }

    @Override
    public void render(Render3DEvent event) {
        RenderUtils.startLines(Color.CYAN,100, true);
        linesToDraw.forEach(line -> RenderUtils.drawLine(event.getStack(), line));
        boxesToDraw.forEach(box -> RenderUtils.drawAABB(event.getStack(), box));
        RenderUtils.endLines(true);
    }

    @Override
    public void tick(TickEvent event) {
        linesToDraw.clear();
        boxesToDraw.clear();
        packetThisTick = 0;
    }


    private boolean canFinish(int packets) {
        return packets <= KillAuraModule.infiniteAuraPacketLimit.getValue() - packetThisTick;
    }

    private void sendPacket(Packet<?> packet) {
        if (packetThisTick < KillAuraModule.infiniteAuraPacketLimit.getValue()) {
            PacketUtil.sendPacket(packet);
            packetThisTick++;
        }
    }
}
