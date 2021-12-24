package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class AntiVineModule extends ToggleableModule {

    private static AntiVineModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "AntiVineKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public AntiVineModule() {
        super("AntiVine", "Prevents vines from slowing you down", () -> true);
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}