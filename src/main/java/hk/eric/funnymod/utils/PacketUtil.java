package hk.eric.funnymod.utils;

import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.utils.classes.PosRot;
import hk.eric.funnymod.utils.classes.XYRot;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;

public class PacketUtil {

    private static final Minecraft mc = FunnyModClient.mc;

    public static void send(Packet<?>... packets) {
        if (mc.getConnection() != null) {
            for (Packet<?> packet : packets) {
                mc.getConnection().send(packet);
            }
        }else {
            LogManager.getLogger().error("Connection is null");
        }
    }

    public static ServerboundMovePlayerPacket.Pos createPos(Vec3 pos, boolean onGround) {
        return createPos(pos.x, pos.y, pos.z, onGround);
    }

    private static ServerboundMovePlayerPacket.Pos createPos(double x, double y, double z, boolean onGround) {
        return new ServerboundMovePlayerPacket.Pos(x, y, z, onGround);
    }

    private static ServerboundMovePlayerPacket.Rot createRot(float yaw, float pitch, boolean onGround) {
        return new ServerboundMovePlayerPacket.Rot(yaw, pitch, onGround);
    }

    private static ServerboundMovePlayerPacket.PosRot createPosRot(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        return new ServerboundMovePlayerPacket.PosRot(x, y, z, yaw, pitch, onGround);
    }

    private static ServerboundMovePlayerPacket.StatusOnly createStatusOnly(boolean onGround) {
        return new ServerboundMovePlayerPacket.StatusOnly(onGround);
    }

    public static ServerboundPlayerCommandPacket createPlayerCommand(ServerboundPlayerCommandPacket.Action action) {
        assert mc.player != null;
        return new ServerboundPlayerCommandPacket(mc.player, action);
    }

    public static ServerboundAcceptTeleportationPacket creatAcceptTeleport(ClientboundPlayerPositionPacket packet) {
        return new ServerboundAcceptTeleportationPacket(packet.getId());
    }

    public static ServerboundMovePlayerPacketBuilder movePlayerPacketBuilder() {
        return new ServerboundMovePlayerPacketBuilder();
    }

    public static class ServerboundMovePlayerPacketBuilder {
        private double x;
        private double y;
        private double z;
        private boolean hasPos;

        private float yaw;
        private float pitch;
        private boolean hasRot;

        private boolean onGround;
        private boolean hasOnGround;

        private ServerboundMovePlayerPacketBuilder() {
        }

        public ServerboundMovePlayerPacketBuilder(boolean onGround) {
            this.onGround = onGround;
            this.hasOnGround = true;
        }

        public ServerboundMovePlayerPacketBuilder setPosRot(PosRot posRot) {
            if (posRot.hasPos()) {
                this.x = posRot.getX();
                this.y = posRot.getY();
                this.z = posRot.getZ();
                this.hasPos = true;
            }
            if (posRot.hasRot()) {
                this.yaw = posRot.getYaw();
                this.pitch = posRot.getPitch();
                this.hasRot = true;
            }
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setPos(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.hasPos = true;
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setX(double x) {
            this.x = x;
            this.hasPos = true;
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setY(double y) {
            this.y = y;
            this.hasPos = true;
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setZ(double z) {
            this.z = z;
            this.hasPos = true;
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setPos(Vec3 pos) {
            if (pos != null) {
                return setPos(pos.x, pos.y, pos.z);
            }
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setRot(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.hasRot = true;
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setRot(XYRot rot) {
            if (rot != null) {
                return setRot(rot.getYaw(), rot.getPitch());
            }
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setOnGround(Boolean onGround) {
            if (onGround != null) {
                return setOnGround(onGround.booleanValue());
            }
            return this;
        }

        public ServerboundMovePlayerPacketBuilder setOnGround(boolean onGround) {
            this.onGround = onGround;
            this.hasOnGround = true;
            return this;
        }

        public ServerboundMovePlayerPacket build() {
            if (!hasOnGround) {
                throw new IllegalStateException("onGround is not set");
            }
            if (hasPos) {
                if (hasRot) {
                    return PacketUtil.createPosRot(x, y, z, yaw, pitch, onGround);
                }else {
                    return PacketUtil.createPos(x, y, z, onGround);
                }
            }else {
                if (hasRot) {
                    return PacketUtil.createRot(yaw, pitch, onGround);
                }else {
                    return PacketUtil.createStatusOnly(onGround);
                }
            }
        }
    }
}
