package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class KeepSprintModule extends ToggleableModule {

    private static KeepSprintModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "KeepSprintKeybind", null, -1, () -> instance.toggle(), true);

    public KeepSprintModule() {
        super("KeepSprint", "Doesn't break your sprint when hitting an entity");
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    enum Mode {
        KEEP,
        BREAK,
        ETC
    }
}