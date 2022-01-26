package hk.eric.funnymod.utils;

import hk.eric.funnymod.FunnyModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import org.apache.logging.log4j.LogManager;

public class PacketUtil {

    private static final Minecraft mc = FunnyModClient.mc;

    public static void sendPacket(Packet<?>... packets) {
        if (mc.getConnection() != null) {
            for (Packet<?> packet : packets) {
                mc.getConnection().send(packet);
            }
        }else {
            LogManager.getLogger().error("Connection is null");
        }
    }

    public static ServerboundMovePlayerPacket.Pos createPos(double x, double y, double z, boolean onGround) {
        return new ServerboundMovePlayerPacket.Pos(x, y, z, onGround);
    }

    public static ServerboundMovePlayerPacket.Rot createRot(float yaw, float pitch, boolean onGround) {
        return new ServerboundMovePlayerPacket.Rot(yaw, pitch, onGround);
    }

    public static ServerboundMovePlayerPacket.PosRot createPosRot(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        return new ServerboundMovePlayerPacket.PosRot(x, y, z, yaw, pitch, onGround);
    }

    public static ServerboundMovePlayerPacket.StatusOnly createLook(boolean onGround) {
        return new ServerboundMovePlayerPacket.StatusOnly(onGround);
    }

    public static ServerboundPlayerCommandPacket createPlayerCommand(ServerboundPlayerCommandPacket.Action action) {
        assert mc.player != null;
        return new ServerboundPlayerCommandPacket(mc.player, action);
    }
}
