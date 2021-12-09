package hk.eric.funnymod.modules.mcqp.MCQPAura;

import hk.eric.funnymod.utils.EntityUtil;
import hk.eric.funnymod.utils.MathUtil;
import hk.eric.funnymod.utils.classes.lamdba.TriConsumer;
import hk.eric.funnymod.utils.classes.lamdba.TriFunction;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RageAuraMode implements AuraMode {
    @Override
    public TriFunction<Stream<LivingEntity>, LocalPlayer, Double, List<LivingEntity>> getEntities() {
        return (entities, player, range) -> entities.filter(entity -> {
            if (EntityUtil.isHostile(entity) || EntityUtil.isPassive(entity)) {
                if (!entity.isAlive() || entity.getHealth() <= 0) return false;
                return MathUtil.compareDistance3D(player.getX(), player.getY(), player.getZ(), entity.getX(), entity.getY(), entity.getZ(), range) == -1;
            }
            return false;
        }).toList();
    }

    @Override
    public TriConsumer<LivingEntity, LocalPlayer, Consumer<Packet<?>>> getAttack() {
//        return (entity, player, packetSender) -> {
//            double targetX = entity.getX(), targetY = entity.getY(), targetZ = entity.getZ();
//            double eyeHeight = player.getEyeHeight();
//            Pair<Float, Float> yawPitch = PlayerUtil.getRotFromCoordinates(targetX, targetY, targetZ, eyeHeight, targetX, targetY, targetZ);
//            float rotY = yawPitch.getSecond(), rotX = yawPitch.getFirst();
//            Vec3 from = new Vec3(targetX, targetY + eyeHeight, targetZ);
//            Vec3 to = new Vec3(entity.getX(), entity.getY() + entity.getBbHeight()/2, entity.getZ());
//            BlockHitResult result = player.level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
//
//            if (result.getType() == HitResult.Type.BLOCK) {
//                packetSender.accept(new ServerboundMovePlayerPacket.Pos(targetX, targetY, targetZ, player.isOnGround()));
//                packetSender.accept(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, result.getBlockPos(), Direction.fromYRot(rotY)));
//            } else {
//                packetSender.accept(new ServerboundMovePlayerPacket.PosRot(targetX, targetY, targetZ, rotY, rotX, player.isOnGround()));
//                packetSender.accept(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
//            }
//            packetSender.accept(new ServerboundMovePlayerPacket.Pos(player.getX(), player.getY(), player.getZ(), player.isOnGround()));
//        };
        return (entity, player, packetSender) -> {
            packetSender.accept(new ServerboundMovePlayerPacket.Pos(entity.getX(), entity.getY(), entity.getZ(), player.isOnGround()));
            packetSender.accept(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
        };
    }
}
