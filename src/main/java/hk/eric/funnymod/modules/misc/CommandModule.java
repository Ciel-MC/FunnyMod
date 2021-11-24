package hk.eric.funnymod.modules.misc;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.Module;
import hk.eric.funnymod.modules.ToggleableModule;

public class CommandModule extends ToggleableModule {

    private static CommandModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "CommandKeybind", null, () -> true, -1,()-> instance.toggle());

    public CommandModule() {
        super("Command", "Commands", () -> true);
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}