package hk.eric.funnymod.modules.world;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.openedClasses.OpenMinecraft;
import hk.eric.funnymod.openedClasses.OpenTimer;
import hk.eric.funnymod.modules.ToggleableModule;

public class TimerModule extends ToggleableModule {

    private static TimerModule instance;
    public static final IntegerSetting speed = new IntegerSetting("Time", "TimerSpeed", "How many ticks are in a second", 1, 100, 20, (integer)-> {
        if(getToggle().isOn()) {
            getOpenTimer().setTicks(integer);
        }
    });
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "TimerKeybind", "", -1, () -> getToggle().toggle());

    public TimerModule() {
        super("Timer", "Changes game speed");
        instance = this;
        settings.add(speed);
        settings.add(keybind);
    }

    private static OpenTimer getOpenTimer() {
        return ((OpenTimer)(((OpenMinecraft) FunnyModClient.mc).getTimer()));
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        getOpenTimer().setTicks(speed.getValue().floatValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getOpenTimer().setTicks(20);
    }
}