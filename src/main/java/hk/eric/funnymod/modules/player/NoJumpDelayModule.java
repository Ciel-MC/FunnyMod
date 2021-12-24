package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class NoJumpDelayModule extends ToggleableModule {

    private static NoJumpDelayModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "NoJumpDelayKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public NoJumpDelayModule() {
        super("NoJumpDelay", "Removes jump delay", () -> true);
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}