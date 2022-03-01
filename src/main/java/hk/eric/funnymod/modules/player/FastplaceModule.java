package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class FastplaceModule extends ToggleableModule {

    private static FastplaceModule instance;
    public static final IntegerSetting delay = new IntegerSetting("Delay", "FastplaceDelay", "The delay between blocks(default 4)", 0, 20, 4);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "FastplaceKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public FastplaceModule() {
        super("Fastplace", "Places block faster when you hold down the mouse button", () -> true);
        instance = this;
        settings.add(delay);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}