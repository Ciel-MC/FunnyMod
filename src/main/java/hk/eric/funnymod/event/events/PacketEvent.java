package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;
import net.minecraft.network.protocol.Packet;

public class PacketEvent extends EventCancellable {
    protected final Packet<?> packet;

    protected PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public static class ReceivePacketEvent extends PacketEvent {
        public ReceivePacketEvent(Packet<?> packet) {
            super(packet);
        }
    }
    public static class SendPacketEvent extends PacketEvent {
        public SendPacketEvent(Packet<?> packet) {
            super(packet);
        }
    }

}
