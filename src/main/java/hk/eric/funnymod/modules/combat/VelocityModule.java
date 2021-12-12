package hk.eric.funnymod.modules.combat;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.EnumSettingWithSubSettings;
import hk.eric.funnymod.modules.ToggleableModule;

public class VelocityModule extends ToggleableModule {

    private static VelocityModule instance;
    public static final EnumSettingWithSubSettings<Mode> velocityMode = new EnumSettingWithSubSettings<>("Mode", "velocityMode", "Velocity mode", Mode.CANCEL, Mode.class);
    public static final IntegerSetting horizontal = new IntegerSetting("Horizontal","KBHorizontal","Horizontal knockback modifier",-100, 100, 100);
    public static final IntegerSetting vertical = new IntegerSetting("Vertical","KBVertical","Vertical knockback modifier",-100, 100, 100);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "VelocityKeybind", null, -1, () -> instance.toggle());

    public VelocityModule() {
        super("Velocity", "Modifies your knockback");
        instance = this;
        settings.add(velocityMode);
        velocityMode.addSubSettings(Mode.MODIFY, horizontal, vertical);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    public enum Mode {
        CANCEL,
        MODIFY
    }
}