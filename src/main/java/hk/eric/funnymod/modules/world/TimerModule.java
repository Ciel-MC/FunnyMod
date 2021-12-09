package hk.eric.funnymod.modules.world;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.gui.setting.DoubleSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.mixin.OpenMinecraft;
import hk.eric.funnymod.mixin.OpenTimer;
import hk.eric.funnymod.modules.ToggleableModule;

public class TimerModule extends ToggleableModule {

    private static TimerModule instance;
    public static final DoubleSetting speed = new DoubleSetting("Speed","TimerSpeed","Multipler of timer",.1,10,1, .1, (speed)->{
        if(getToggle().isOn()) {
            setSpeed(speed);
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
        setSpeed(speed.getValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        setSpeed(1);
    }

    private static void setSpeed(double speed) {
        getOpenTimer().setMsPerTick((float) (1000 / (speed * 20)));
    }
}