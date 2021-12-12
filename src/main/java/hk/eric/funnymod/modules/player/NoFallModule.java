package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class NoFallModule extends ToggleableModule {

    private static NoFallModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "NoFallKeybind", null, () -> true, -1, () -> instance.toggle());

    public NoFallModule() {
        super("NoFall", "Prevents fall damage", () -> true);
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}