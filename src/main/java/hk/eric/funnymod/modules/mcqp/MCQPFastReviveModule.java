package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PacketUtil;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;

public class MCQPFastReviveModule extends ToggleableModule {

    private static MCQPFastReviveModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPFastReviveKeybind", null, -1, () -> instance.toggle(), true);

    private static final EventHandler<TickEvent> spamRevive = new EventHandler<>() {
        @Override
        public void handle(TickEvent e) {
            if (e.getState() == EventState.POST) {
                PacketUtil.sendPacket(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            }
        }
    };

    public MCQPFastReviveModule() {
        super("MCQPFastRevive", "Spams revive button");
        instance = this;
        settings.add(keybind);

        registerOnOffHandler(spamRevive);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}