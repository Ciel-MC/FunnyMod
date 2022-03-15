package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import com.mojang.authlib.GameProfile;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.chat.ChatManager;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PacketEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.modules.movement.FlightModule;
import hk.eric.funnymod.utils.HasFlag;
import hk.eric.funnymod.utils.PacketUtil;
import hk.eric.funnymod.utils.classes.XYRot;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class FreecamModule extends ToggleableModule {

    private static final EventHandler<PacketEvent> packetEventHandler = new EventHandler<>() {
        @Override
        public void handle(PacketEvent event) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof ServerboundMovePlayerPacket movePlayerPacket) {
                if (!((HasFlag) movePlayerPacket).getFlag(HasFlag.Flags.SENT_BY_FUNNY_MOD)) {
                    event.setCancelled(true);
                    ServerboundMovePlayerPacket p = PacketUtil.movePlayerPacketBuilder()
                            .setOnGround(fakePlayer.isOnGround())
                            .build();
                    ((HasFlag) p).setFlag(HasFlag.Flags.SENT_BY_FUNNY_MOD, true);
                    PacketUtil.send(p);
                }
            }else if(packet instanceof ClientboundPlayerPositionPacket playerPositionPacket) {
                event.setCancelled(true);
                Vec3 pos = fakePlayer.position().add(playerPositionPacket.getX(), playerPositionPacket.getY(), playerPositionPacket.getZ());
                XYRot rot = new XYRot(playerPositionPacket.getXRot(), playerPositionPacket.getYRot());

                fakePlayer.moveTo(pos.x, pos.y, pos.z, rot.getYRot(), rot.getXRot());

                PacketUtil.send(PacketUtil.creatAcceptTeleport(playerPositionPacket));
                PacketUtil.send(PacketUtil.movePlayerPacketBuilder()
                        .setPos(pos)
                        .setRot(rot)
                        .setOnGround(false)
                        .build()
                );

                if (FunnyModClient.debug) {
                    ChatManager.sendMessage("Freecam", "Player teleported! X: " + pos.x + " Y: " + pos.y + " Z: " + pos.z);
                }
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
        getPlayer().setYHeadRot(fakePlayer.getYHeadRot());
        getPlayer().setOnGround(fakePlayer.isOnGround());
        fakePlayer = null;
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}