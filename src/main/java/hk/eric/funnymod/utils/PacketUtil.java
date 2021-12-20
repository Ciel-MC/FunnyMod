package hk.eric.funnymod.utils;

import hk.eric.funnymod.FunnyModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
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
}
