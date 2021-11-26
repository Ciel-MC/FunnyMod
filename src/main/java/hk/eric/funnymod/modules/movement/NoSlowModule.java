package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class NoSlowModule extends ToggleableModule {

    private static NoSlowModule instance;
    public static final EnumSetting<Mode> NO_SLOW_MODE = new EnumSetting<>("Mode", "NoSlowMode", null, Mode.VANILLA, Mode.class);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "NoSlowKeybind", null, -1,()-> instance.toggle());

    public NoSlowModule() {
        super("NoSlow", "Doesn't slow you down when you are using an item.");
        instance = this;
        settings.add(NO_SLOW_MODE);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    enum Mode {
        VANILLA
    }
}
