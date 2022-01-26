package hk.eric.funnymod.modules.misc;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class TooltipScrollingModule extends ToggleableModule {

    private static TooltipScrollingModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "TooltipScrollingKeybind", null, -1, () -> instance.toggle(), true);

    public TooltipScrollingModule() {
        super("Tooltip Scrolling", "Allows you to scroll tooltips");
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}