package hk.eric.funnymod.utils;

import hk.eric.funnymod.FunnyModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;

import java.util.Objects;

public class PacketUtil {

    private static final Minecraft mc = FunnyModClient.mc;

    public static void sendPacket(Packet<?> packet) {
        Objects.requireNonNull(mc.getConnection()).send(packet);
    }
}
