package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class MCQPNoHitModule extends ToggleableModule {

    private static MCQPNoHitModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPNoHitKeybind", "", () -> true, -1, () -> instance.toggle());

    public MCQPNoHitModule() {
        super("MCQPNoHit", "Disables hitting mobs to prevent false hits(WARNING: WILL PREVENT YOU FROM HITTING MOBS ON OTHER SERVERS)", () -> true);
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}