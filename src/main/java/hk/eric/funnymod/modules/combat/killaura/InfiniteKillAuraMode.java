package hk.eric.funnymod.modules.combat.killaura;

import hk.eric.ericLib.utils.ClientPacketUtil;
import hk.eric.ericLib.utils.MathUtil;
import hk.eric.ericLib.utils.classes.ThreeDimensionalLine;
import hk.eric.ericLib.utils.classes.XYRot;
import hk.eric.ericLib.utils.classes.minecraftPlus.BetterBlockPos;
import hk.eric.ericLib.utils.classes.pathFind.AStarPathFinder;
import hk.eric.ericLib.utils.classes.pathFind.Node;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.events.Render3DEvent;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.modules.combat.KillAuraModule;
import hk.eric.funnymod.utils.EntityUtil;
import hk.eric.funnymod.utils.RenderUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class InfiniteKillAuraMode extends KillauraMode {

    private int packetThisTick = 0;

    public static final Set<ThreeDimensionalLine> linesToDraw = new HashSet<>();
    public static final Set<AABB> boxesToDraw = new HashSet<>();

    final Comparator<LivingEntity> armorEntitySorter = Comparator.comparingInt(EntityUtil::rateArmor);
    final Comparator<LivingEntity> healthEntitySorter = Comparator.comparingDouble(LivingEntity::getHealth).reversed();

    public Comparator<LivingEntity> getEntityComparator(LocalPlayer player, KillAuraModule.SortType sortType) {
        return switch (sortType) {
            case DISTANCE -> Comparator.comparingDouble(e -> MathUtil.getDistance3D(player.getX(), player.getY(), player.getZ(), e.getX(), e.getY(), e.getZ()));
            case HEALTH -> healthEntitySorter;
            case ARMOR -> armorEntitySorter;
        };
    }

    @Override
    public Stream<? extends LivingEntity> process(Stream<? extends LivingEntity> entityStream) {
        return entityStream
                .filter(e -> MathUtil.getDistance3D(e.position(), FunnyModClient.mc.player.position()) <= KillAuraModule.infiniteAuraRange.getValue() * KillAuraModule.infiniteAuraRange.getValue())
                .filter(e -> {
                    if (KillAuraModule.infiniteAuraBypass.getValue() != KillAuraModule.TeleportBypass.PAPER) {
                        return true;
                    }else {
                        return switch (KillAuraModule.infiniteAuraDistanceCalculationAccuracy.getValue()) {
                            case INTEGER -> BetterBlockPos.from(e).asNode().distanceInt(BetterBlockPos.from(FunnyModClient.mc.player).asNode(), true) <= KillAuraModule.infiniteAuraPaperDistance.getValue();
                            case FLOAT -> BetterBlockPos.from(e).asNode().distanceFloat(BetterBlockPos.from(FunnyModClient.mc.player).asNode(), true) <= KillAuraModule.infiniteAuraPaperDistance.getValue();
                            case DOUBLE -> BetterBlockPos.from(e).asNode().distanceDouble(BetterBlockPos.from(FunnyModClient.mc.player).asNode(), true) <= KillAuraModule.infiniteAuraPaperDistance.getValue();
                        };
                    }
                })
                .sorted(getEntityComparator(FunnyModClient.mc.player, KillAuraModule.sortType.getValue()))
                .limit(KillAuraModule.infiniteAuraTargetLimit.getValue());
    }

    @Override
    public void attack(LivingEntity entity) {
        LocalPlayer player = FunnyModClient.mc.player;
        assert player != null;
        moveTo(player,entity, KillAuraModule.infiniteAuraMaxStep.getValue(), this::sendPacket, pathAsList -> canFinish(pathAsList.size() + 2), (node, node2) -> node.distanceFloat(node2, true) < 3, true);
        sendPacket(ServerboundInteractPacket.createAttackPacket(entity, false));
        player.swing(InteractionHand.MAIN_HAND);
        player.resetAttackStrengthTicker();
    }

    public static void moveTo(LocalPlayer player, Entity entity, int maxStep, Consumer<Packet<?>> sender, Function<List<Node>, Boolean> canGo, BiFunction<Node, Node, Boolean> ended, boolean returnOnFail) {
        Node path = AStarPathFinder.search(BetterBlockPos.from(player).asNode(), BetterBlockPos.from(entity).asNode(), maxStep, true, ended, !returnOnFail, FunnyModClient.mc.level);
        if (path == null) {
            return;
        }
        List<Node> pathAsList = path.asList();
        pathAsList.remove(pathAsList.size() - 1);
        if (!canGo.apply(pathAsList)) return;
        Queue<Node> nodes = new ArrayDeque<>(pathAsList);
        if (nodes.isEmpty()) return;
        Node oldNode = nodes.remove();
        linesToDraw.clear();
        boxesToDraw.clear();
        boxesToDraw.add(entity.getDimensions(entity.getPose()).makeBoundingBox(entity.position()));
        while (!nodes.isEmpty()) {
            Node node = nodes.poll();
            Vec3 pos = node.getPos().toCenteredVec3();
            linesToDraw.add(ThreeDimensionalLine.of(oldNode.getPos().toVec3(), node.getPos().toVec3()));
            boxesToDraw.add(player.getDimensions(player.getPose()).makeBoundingBox(pos));
            Vec3 eyePos = node.getPos().toVec3().add(0, player.getEyeHeight(), 0);
            XYRot rot = MathUtil.getLookAtRotation(eyePos, MathUtil.closestPointInAABB(eyePos, entity.getBoundingBox()));
            linesToDraw.add(ThreeDimensionalLine.of(eyePos, eyePos.add(MathUtil.getCoordFromAngles(rot.getYaw(), rot.getPitch(), 3))));
            sender.accept(ClientPacketUtil.movePlayerPacketBuilder()
                    .setPos(pos)
                    .setRot(rot)
                    .setOnGround(true)
                    .build()
            );
            oldNode = node;
        }
    }

    @Override
    public void render(Render3DEvent event) {
        RenderUtil.startLines(Color.CYAN,100, true);
        linesToDraw.forEach(line -> RenderUtil.drawLine(event.getStack(), line));
        boxesToDraw.forEach(box -> RenderUtil.drawAABB(event.getStack(), box));
        RenderUtil.endLines(true);
    }

    @Override
    public void tick(TickEvent event) {
        packetThisTick = 0;
    }


    private boolean canFinish(int packets) {
        return packets <= KillAuraModule.infiniteAuraPacketLimit.getValue() - packetThisTick;
    }

    private void sendPacket(Packet<?> packet) {
        if (packetThisTick < KillAuraModule.infiniteAuraPacketLimit.getValue()) {
            ClientPacketUtil.send(packet);
            packetThisTick++;
        }
    }
}
