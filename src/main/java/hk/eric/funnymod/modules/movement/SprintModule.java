package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.DoubleSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class SprintModule extends ToggleableModule {

    private static SprintModule instance;
    public static final BooleanSetting omnidirectional = new BooleanSetting("Omnidirectional", "OmnidirectionalSprint", null, ()->true, false);
    public static final DoubleSetting speed = new DoubleSetting("Speed", "SprintSpeed", null, ()->true, 1.0, 3.0, 1.0, 0.1);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "SprintKeybind", "", () -> true, -1, () -> instance.toggle());

    public SprintModule() {
        super("Sprint", "Automatically sprints for you.", () -> true);
        instance = this;
        settings.add(omnidirectional);
        settings.add(speed);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}