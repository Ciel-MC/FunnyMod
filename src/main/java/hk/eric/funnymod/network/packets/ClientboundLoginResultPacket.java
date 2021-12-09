package hk.eric.funnymod.network.packets;

import hk.eric.funnymod.network.FunnyModPacketHandler;
import hk.eric.simpleTCP.byteBuffer.IAbstractedByteBuffer;
import hk.eric.simpleTCP.packets.Packet;
import hk.eric.simpleTCP.packets.PacketID;

public class ClientboundLoginResultPacket extends Packet<FunnyModPacketHandler> {

    private final PacketID id = new PacketID("");

    private final boolean success;

    public ClientboundLoginResultPacket(boolean success) {
        this.success = success;
    }

    public ClientboundLoginResultPacket(IAbstractedByteBuffer buffer) {
        this.success = buffer.readBoolean();
    }

    @Override
    public void write(IAbstractedByteBuffer byteBuffer) {
        byteBuffer.writePacketID(getPacketID());
        byteBuffer.writeBoolean(success);
    }

    @Override
    public void handle(FunnyModPacketHandler handler) {

    }

    @Override
    public PacketID getPacketID() {
        return id;
    }

    @Override
    public Packet<FunnyModPacketHandler> createThis(IAbstractedByteBuffer byteBuffer) {
        return new ClientboundLoginResultPacket(byteBuffer);
    }

    @Override
    public int getSize() {
        return 4;
    }

    public PacketID getId() {
        return id;
    }

    public boolean isSuccessful() {
        return success;
    }
}
