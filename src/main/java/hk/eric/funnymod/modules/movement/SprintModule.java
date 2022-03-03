package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.DoubleSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class SprintModule extends ToggleableModule {

    private static SprintModule instance;
    public static final BooleanSetting omnidirectional = new BooleanSetting("Omnidirectional", "OmnidirectionalSprint", null, false);
    public static final DoubleSetting speed = new DoubleSetting("Speed", "SprintSpeed", null, 1.0D, 0.0D, 2.0D);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "SprintKeybind", null, -1, () -> instance.toggle(), true);

    public SprintModule() {
        super("Sprint", "Automatically sprints for you.");
        instance = this;
        settings.add(omnidirectional);
        settings.add(speed);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}