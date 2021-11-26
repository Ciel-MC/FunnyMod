package hk.eric.funnymod.modules.utilities;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;

public class SlotLockingModule extends ToggleableModule {

    private static SlotLockingModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "SlotLockingKeybind", "", -1, () -> {
        /*TODO:
        *  Actually implement slot locking*/
    });

    public SlotLockingModule() {
        super("SlotLocking", "Locks Slots");
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}