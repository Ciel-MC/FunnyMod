package hk.eric.funnymod.network.packets;

import hk.eric.funnymod.network.FunnyModPacketHandler;
import hk.eric.simpleTCP.byteBuffer.IAbstractedByteBuffer;
import hk.eric.simpleTCP.packets.Packet;
import hk.eric.simpleTCP.packets.PacketID;

import java.util.UUID;

public class ServerboundLoginPacket extends Packet<FunnyModPacketHandler> {

    private final PacketID id = new PacketID("LG");
    
    private final String username;
    private final UUID uuid;
    private final String ip;
    private final int port;

    public ServerboundLoginPacket() {
        this("", UUID.fromString(""), "", 0);
    }

    public ServerboundLoginPacket(String username, UUID uuid, String ip, int port) {
        this.username = username;
        this.uuid = uuid;
        this.ip = ip;
        this.port = port;
    }
    
    public ServerboundLoginPacket(IAbstractedByteBuffer buffer) {
        this.username = buffer.readString();
        this.uuid = buffer.readUUID();
        this.ip = buffer.readString();
        this.port = buffer.readInt();
    }
    
    @Override
    public void write(IAbstractedByteBuffer buffer) {
        buffer.writeString(username);
        buffer.writeUUID(uuid);
        buffer.writeString(ip);
        buffer.writeInt(port);
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
        return new ServerboundLoginPacket(byteBuffer);
    }

    @Override
    public int getSize() {
        return 4;
    }

    public PacketID getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getIP() {
        return ip;
    }
}
