package hk.eric.funnymod.network;

import hk.eric.funnymod.network.packets.ClientboundLoginResultPacket;
import hk.eric.simpleTCP.IPacketHandler;

public class FunnyModPacketHandler implements IPacketHandler {
    @Override
    public void onDisconnect(String s) {
        System.out.println("Disconnected: " + s);
    }

    public void handleLoginResultPacket(ClientboundLoginResultPacket packet) {
        if(packet.isSuccessful()) {

        }
    }

}
