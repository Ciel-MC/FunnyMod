package hk.eric.funnymod.modules.combat;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.BooleanSettingWithSubSettings;
import hk.eric.funnymod.modules.ToggleableModule;

public class VelocityModule extends ToggleableModule {

    private static VelocityModule instance;
    public static final IntegerSetting horizontal = new IntegerSetting("Horizontal","KBHorizontal","Horizontal knockback modifier",-200, 200, 0);
    public static final IntegerSetting vertical = new IntegerSetting("Vertical","KBVertical","Vertical knockback modifier",-200, 200, 0);
    public static final BooleanSettingWithSubSettings kiteMode = new BooleanSettingWithSubSettings("Kite Mode", "VelocityKiteMode", "Change your kb when not facing the source of kb", false);
    public static final IntegerSetting kiteHorizontal = new IntegerSetting("Horizontal","KBKiteHorizontal","Horizontal knockback modifier",-200, 200, 100);
    public static final IntegerSetting kiteVertical = new IntegerSetting("Vertical","KBKiteVertical","Vertical knockback modifier",-200, 200, 100);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "VelocityKeybind", null, -1, () -> instance.toggle(), true);

    public VelocityModule() {
        super("Velocity", "Modifies your knockback");
        instance = this;
        settings.add(horizontal);
        settings.add(vertical);

//        settings.add(kiteMode);
//        kiteMode.addSubSettings(true, kiteHorizontal, kiteVertical);

        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }
}