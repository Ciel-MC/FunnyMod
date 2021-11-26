package hk.eric.funnymod.modules.visual;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class AnimationModule extends ToggleableModule {

    private static AnimationModule instance;
    public static final BooleanSetting noSmoothSneak = new BooleanSetting("No Smooth Sneak", "AnimationNoSmoothSneak", "Removes the smooth sneaking animation", false);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "AnimationKeybind", "", -1, () -> instance.toggle());

    public AnimationModule() {
        super("Animation", "Modifies animations");
        instance = this;
        settings.add(noSmoothSneak);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}