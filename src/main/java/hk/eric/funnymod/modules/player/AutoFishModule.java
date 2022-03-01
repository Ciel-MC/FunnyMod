package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PlayerUtil;
import hk.eric.funnymod.utils.WaitUtils;
import net.minecraft.world.item.Items;

public class AutoFishModule extends ToggleableModule {

    private static AutoFishModule instance;
    public static final EnumSetting<AutoFishModeEnum> autoFishMode = new EnumSetting<>("Auto Fish Mode", "AutoFishAutoFishMode", "Mode", AutoFishModeEnum.MCQP, AutoFishModeEnum.class);
    public static final IntegerSetting fishUpDelay = new IntegerSetting("Fish Up Delay", "AutoFishFishUpDelay", "Delay before rolling the rod back(ms)", 0, 2000, 100);
    public static final IntegerSetting recastDelay = new IntegerSetting("Recast Delay", "AutoFishRecastDelay", "Delay before recasting the rod(ms)", 0, 2000, 100);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "AutoFishKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public AutoFishModule() {
        super("AutoFish", "Automatically fish", () -> true);
        instance = this;
        settings.add(fishUpDelay);
        settings.add(recastDelay);
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        autoFishMode.getValue().getMode().start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        autoFishMode.getValue().getMode().interrupt();
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public enum AutoFishModeEnum {
        MCQP(AutoFishMode.MCQP);

        private final AutoFishMode mode;

        AutoFishModeEnum(AutoFishMode mode) {
            this.mode = mode;
        }

        public AutoFishMode getMode() {
            return mode;
        }
    }

    public abstract static class AutoFishMode {
        
        public static final AutoFishMode MCQP = new AutoFishMode() {

            private static Thread fishThread;

            @Override
            public void interrupt() {
                fishThread.interrupt();
            }

            @Override
            public void start() {
                fishThread = new Thread(() -> {
                    try {
                        while (true) {
                            PlayerUtil.switchToHotbar(item -> item.getItem().equals(Items.FISHING_ROD));
                            PlayerUtil.rightClick();
                            WaitUtils.waitForTitle(title -> title.getComponent().getContents().contains("魚上鉤了!"));
                            Thread.sleep(fishUpDelay.getValue());
                            PlayerUtil.rightClick();
                            Thread.sleep(recastDelay.getValue());
                        }
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, "AutoFish");
                fishThread.start();
            }
        };
        
        public abstract void interrupt();
        
        public abstract void start();
        
    }
}