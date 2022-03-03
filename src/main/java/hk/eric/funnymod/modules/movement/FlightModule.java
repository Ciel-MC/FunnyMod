package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.MotionEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.DoubleSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.BooleanSettingWithSubSettings;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.modules.world.FreecamModule;
import hk.eric.funnymod.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec2;

public class FlightModule extends ToggleableModule {

    private static boolean bobUp = false;

    private static FlightModule instance;
    public static final DoubleSetting speed = new DoubleSetting("Speed", "FlightSpeed", null, 0, 100, 2, .1D);
    public static final DoubleSetting bob = new DoubleSetting("Bob", "FlightBob", null, 0.01, .1, 0.04, .01D);
    public static final BooleanSettingWithSubSettings antiVanillaKick = new BooleanSettingWithSubSettings("Anti Vanilla Kick", "FlightAntiVanillaKick", "Anti vanilla kick", false);
    public static final BooleanSetting strongMode = new BooleanSetting("Strong Mode", "FlightStrongMode", "Ensures you dont get kicked, but flying up looks buggy", false);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "FlightKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public FlightModule() {
        super("Flight", "Weeeeee", () -> true);
        instance = this;

        settings.add(speed);
        settings.add(bob);
        settings.add(antiVanillaKick);
        antiVanillaKick.addSubSetting(true, strongMode);
        settings.add(keybind);

        registerOnOffHandler(motionEventHandler);
    }

    private static final EventHandler<MotionEvent.Post> motionEventHandler = new EventHandler<>() {
        @Override
        public void handle(MotionEvent.Post event) {
            getPlayer().noPhysics = true;
            getPlayer().fallDistance = 0;
            getPlayer().setOnGround(false);
            getPlayer().getAbilities().flying = false;
            double x, y = 0, z;

            double speed = FlightModule.speed.getValue();
            if (Minecraft.getInstance().options.keyJump.isDown()) {
                y += speed * .5;
            }
            if (Minecraft.getInstance().options.keyShift.isDown()) {
                y -= speed * .5;
            }
            Vec2 move = getPlayer().input.getMoveVector();

            double yaw = MathUtil.toRadians(getPlayer().getYHeadRot());

            x = (move.x * Math.cos(yaw)) - (move.y * Math.sin(yaw));
            z = (move.x * Math.sin(yaw)) + (move.y * Math.cos(yaw));

            if (antiVanillaKick.isOn()) {
                if (strongMode.isOn()) {
                    if (bobUp) {
                        if (y > 0) {
                            y *= 2;
                        } else if (y == 0) {
                            y = bob.getValue();
                        }
                    }else {
                        if (y >= 0) {
                            y = -bob.getValue();
                        }
                    }
                }else {
                    y += (bobUp ? bob.getValue() : -bob.getValue());
                }
                bobUp = !bobUp;
            }

            getPlayer().setDeltaMovement(x * speed, y, z * speed);
        }
    };

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    private static boolean shouldBob() {
        return antiVanillaKick.isOn() && !FreecamModule.getToggle().isOn();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getPlayer().setDeltaMovement(0, 0, 0);
    }
}