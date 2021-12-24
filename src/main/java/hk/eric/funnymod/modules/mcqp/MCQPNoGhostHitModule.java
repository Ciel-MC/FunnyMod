package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventPriority;
import hk.eric.funnymod.event.events.AttackEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class MCQPNoGhostHitModule extends ToggleableModule {

    private static MCQPNoGhostHitModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPNoHitKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    private static final EventHandler<AttackEvent> attackEventHandler = new EventHandler<>(EventPriority.HIGHEST) {
        @Override
        public void handle(AttackEvent event) {
            event.setCancelled(true);
        }
    };

    public MCQPNoGhostHitModule() {
        super("No Ghost Hit", "Disables hitting mobs to prevent false hits(WARNING: WILL PREVENT YOU FROM HITTING MOBS ON OTHER SERVERS)", () -> true);
        instance = this;
        settings.add(keybind);

        registerOnOffHandler(attackEventHandler);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}