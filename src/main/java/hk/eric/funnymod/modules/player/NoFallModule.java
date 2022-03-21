package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PacketEvent;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.EnumSettingWithSubSettings;
import hk.eric.funnymod.mixin.OpenServerboundMovePlayerPacket;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.ericLib.utils.classes.Result;
import hk.eric.ericLib.utils.classes.getters.Getter;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class NoFallModule extends ToggleableModule {

    private static final EventHandler<PacketEvent.SendPacketEvent> packetEventSendPacketEventHandler = new EventHandler<>() {
        @Override
        public void handle(PacketEvent.SendPacketEvent event) {
            if (event.getPacket() instanceof ServerboundMovePlayerPacket movePlayerPacket){
                Result<Boolean> result;
                if ((result = shouldGround.get()).isChanged()) {
                    ((OpenServerboundMovePlayerPacket) movePlayerPacket).setOnGround(result.get());
                    if (result.get()) {
                        getPlayer().fallDistance = 0;
                    }
                }
            }
        }
    };

    private static NoFallModule instance;
    public static final EnumSettingWithSubSettings<Mode> mode = new EnumSettingWithSubSettings<>("Mode", "NoFallMode", "Mode", Mode.PACKET, Mode.class);
    public static final IntegerSetting packet_distance = new IntegerSetting("Packet distance", "NoFallPacket_distance", "Distance before a packet is sent(Less 3 for no fall damage)", 1, 5, 3);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "NoFallKeybind", null, -1, () -> instance.toggle(), true);

    public static final Getter<Result<Boolean>> shouldGround = () -> getToggle().isOn() ? switch (mode.getValue()) {
            case ALWAYS_GROUND -> Result.of(true);
            case NO_GROUND -> Result.of(false);
            case PACKET -> getPlayer().fallDistance > 3 ? Result.of(true) : Result.unchanged();
        } : Result.unchanged();

    public NoFallModule() {
        super("NoFall", "Prevents fall damage");
        instance = this;
        settings.add(mode);
        mode.addSubSettings(Mode.PACKET, packet_distance);
        settings.add(keybind);

        registerOnOffHandler(packetEventSendPacketEventHandler);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public enum Mode {
        ALWAYS_GROUND,
        NO_GROUND,
        PACKET
    }
}