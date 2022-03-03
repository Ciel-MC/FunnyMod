package hk.eric.funnymod.modules.mcqp.MCQPAura;

import hk.eric.funnymod.utils.EntityUtil;
import hk.eric.funnymod.utils.MathUtil;
import hk.eric.funnymod.utils.classes.lamdba.TriConsumer;
import hk.eric.funnymod.utils.classes.lamdba.TriFunction;
import hk.eric.funnymod.utils.classes.pairs.Pair;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NormalAuraMode implements AuraMode {

    private static int cycle = 0;

    @Override
    public TriFunction<Stream<LivingEntity>, LocalPlayer, Double, List<LivingEntity>> getEntities() {
        return (entities, player, range) -> {
            if (cycle >= MCQPAuraModule.cycleTime.getValue()) cycle = 0;
            List<LivingEntity> entityToAttack = entities.filter(entity -> {
                if (EntityUtil.isHostile(entity) || EntityUtil.isPassive(entity) || EntityUtil.isNeutral(entity)) {
                    if (!entity.isAlive() || entity.getHealth() <= 0) return false;
                    return MathUtil.compareDistance3D(player.getX(), player.getY(), player.getZ(), entity.getX(), entity.getY() + entity.getBbHeight()/2, entity.getZ(), range) == -1;
                }
                return false;
            }).sorted(Comparator.comparingInt(NormalAuraMode::getEntitySorter)).collect(Collectors.toList());
            if (MCQPAuraModule.cycleAttack.isOn()) {
                entityToAttack = getSublist(entityToAttack, MCQPAuraModule.maxTarget.getValue(), cycle);
            } else {
                entityToAttack = entityToAttack.subList(0, Math.min(MCQPAuraModule.maxTarget.getValue(), entityToAttack.size()));
            }
            cycle++;
            return entityToAttack;
        };
    }

    @Override
    public TriConsumer<LivingEntity, LocalPlayer, Consumer<Packet<?>>> getAttack() {
        return (entity, player, packetSender) -> {
            Pair<Float, Float> yawPitch = MathUtil.getLookAtRotation(player, entity.getX(), entity.getY(), entity.getZ());
            float rotY = yawPitch.getSecond(), rotX = yawPitch.getFirst();
            Vec3 from = player.getEyePosition(1);
            Vec3 to = entity.position().add(0, entity.getY() - entity.getBbHeight(), 0);
            HitResult result = player.pick(3,1,false);
//            BlockHitResult result = player.level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) result;
                if (!player.level.getBlockState(blockHitResult.getBlockPos()).getBlock().getClass().equals(WallSignBlock.class)) {
                    packetSender.accept(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockHitResult.getBlockPos(), Direction.fromYRot(rotY)));
                }
            } else {
                packetSender.accept(new ServerboundMovePlayerPacket.Rot(rotY, rotX, player.isOnGround()));
            }
            player.swing(InteractionHand.MAIN_HAND);
        };
    }

    private static int getEntitySorter(LivingEntity entity) {
        return entity.getId();
    }

    public static List<LivingEntity> getSublist(List<LivingEntity> list, int size, int cycle) {
        return size > list.size()? list : list.subList(cycle * size % list.size() - 1, (cycle + 1) * size % list.size() - 1);
    }
}
