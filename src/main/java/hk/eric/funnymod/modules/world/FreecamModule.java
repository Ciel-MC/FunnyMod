package hk.eric.funnymod.modules.world;

import com.lukflug.panelstudio.base.IToggleable;
import com.mojang.authlib.GameProfile;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PacketEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.mixin.flags.HasFlag;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.modules.movement.FlightModule;
import hk.eric.funnymod.utils.PacketUtil;
import hk.eric.funnymod.utils.classes.XYRot;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class FreecamModule extends ToggleableModule {

    private static Vec3 lastPos;
    private static XYRot lastLook;
    private static Boolean lastOnGround;

    private static final EventHandler<PacketEvent> packetEventHandler = new EventHandler<>() {
        @Override
        public void handle(PacketEvent event) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof ServerboundMovePlayerPacket movePlayerPacket) {
                if (!((HasFlag) movePlayerPacket).getFlag(HasFlag.Flags.SENT_BY_FUNNY_MOD)) {
                    event.setCancelled(true);
                    ServerboundMovePlayerPacket p = PacketUtil.ServerboundMovePlayerPacketBuilder.create()
                            .setPos(lastPos)
                            .setRot(lastLook)
                            .setOnGround(lastOnGround)
                            .build();
                    ((HasFlag) p).setFlag(HasFlag.Flags.SENT_BY_FUNNY_MOD, true);
                    PacketUtil.sendPacket(p);
                    lastPos = null;
                    lastLook = null;
                    lastOnGround = null;
                }
            }else if(packet instanceof ClientboundTeleportEntityPacket clientboundTeleportEntityPacket && clientboundTeleportEntityPacket.getId() == getPlayer().getId()) {
                event.setCancelled(true);
                lastPos = new Vec3(clientboundTeleportEntityPacket.getX(), clientboundTeleportEntityPacket.getY(), clientboundTeleportEntityPacket.getZ());
                lastLook = new XYRot(clientboundTeleportEntityPacket.getxRot(), clientboundTeleportEntityPacket.getyRot());
                lastOnGround = clientboundTeleportEntityPacket.isOnGround();

                fakePlayer.setPos(lastPos);
                fakePlayer.setXRot(lastLook.getXRot());
                fakePlayer.setYRot(lastLook.getYRot());
                fakePlayer.setOnGround(lastOnGround);
            }
        }
    };

    private static boolean flightWasOn = false;

    private static FreecamModule instance;
    public static final BooleanSetting enableFlight = new BooleanSetting("Enable Flight", "FreecamEnableFlight", "Automatically enable flight", false);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "FreecamKeybind", null, () -> true, -1, () -> instance.toggle(), true);
    public static RemotePlayer fakePlayer;

    public FreecamModule() {
        super("Freecam", "Allows your camera to move away from your body", () -> true);
        instance = this;

        settings.add(enableFlight);
        settings.add(keybind);

        registerOnOffHandler(packetEventHandler);
    }

    @Override
    public void onEnable() {

        if (enableFlight.isOn()) {
            if (!(flightWasOn = FlightModule.getToggle().isOn())) {
                FlightModule.getToggle().toggle();
            }
        }

        super.onEnable();
        GameProfile fakeProfile = new GameProfile(null, getPlayer().getGameProfile().getName());
        fakePlayer = new RemotePlayer(getLevel(), fakeProfile);
        fakePlayer.copyPosition(getPlayer());
        fakePlayer.setYHeadRot(getPlayer().getYHeadRot());
        fakePlayer.setOnGround(getPlayer().isOnGround());
        fakePlayer.setDeltaMovement(getPlayer().getDeltaMovement());
        getLevel().addPlayer(fakePlayer.getId(), fakePlayer);
    }

    @Override
    public void onDisable() {

        if (enableFlight.isOn() && !flightWasOn) {
            FlightModule.getToggle().toggle();
        }

        super.onDisable();
        getPlayer().setDeltaMovement(fakePlayer.getDeltaMovement());
        getLevel().removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
        getPlayer().copyPosition(fakePlayer);
        getPlayer().setOnGround(fakePlayer.isOnGround());
        fakePlayer = null;

        lastLook = null;
        lastPos = null;
        lastOnGround = null;
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}