package hk.eric.funnymod.modules.world;

import com.lukflug.panelstudio.base.IToggleable;
import com.mojang.authlib.GameProfile;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PacketEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.modules.movement.FlightModule;
import hk.eric.funnymod.utils.PacketUtil;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;

public class FreecamModule extends ToggleableModule {

    private static final EventHandler<PacketEvent.SendPacketEvent> packetEventHandler = new EventHandler<>() {
        @Override
        public void handle(PacketEvent.SendPacketEvent event) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof ServerboundMovePlayerPacket && !(packet instanceof ServerboundMovePlayerPacket.StatusOnly)) {
                event.setCancelled(true);
                PacketUtil.sendPacket(PacketUtil.createStatusOnly(fakePlayer.isOnGround()));
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
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}