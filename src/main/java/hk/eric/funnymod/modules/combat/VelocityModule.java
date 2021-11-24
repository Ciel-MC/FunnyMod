package hk.eric.funnymod.modules.combat;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class VelocityModule extends ToggleableModule {

    private static VelocityModule instance;
    public static final IntegerSetting horizontal = new IntegerSetting("Horizontal","KBHorizontal","Horizontal knockback modifier", ()->true,-100, 100, 100);
    public static final IntegerSetting vertical = new IntegerSetting("Vertical","KBVertical","Vertical knockback modifier", ()->true,-100, 100, 100);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "VelocityKeybind", "", () -> true, -1, () -> instance.toggle());

    public VelocityModule() {
        super("Velocity", "Modifies your knockback", () -> true);
        instance = this;
        settings.add(horizontal);
        settings.add(vertical);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}