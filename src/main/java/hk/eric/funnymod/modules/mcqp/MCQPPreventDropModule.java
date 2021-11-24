package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class MCQPPreventDropModule extends ToggleableModule {

    private static MCQPPreventDropModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPPreventDropKeybind", "", () -> true, -1, () -> instance.toggle());

    public MCQPPreventDropModule() {
        super("MCQPPreventDrop", "Prevents you from dropping books and unstackable items", () -> true);
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}