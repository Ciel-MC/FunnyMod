package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.MotionEvent;
import hk.eric.funnymod.gui.setting.DoubleSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class FlightModule extends ToggleableModule {

    private static boolean flip = false;
    private static final double notMovingSpeed = 1.0E-5f;
    private static final double bobSpeed = 0.04;

    private static FlightModule instance;
    public static final DoubleSetting speed = new DoubleSetting("Speed", "FlightSpeed", null, 0, 100, 2, .1D);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "FlightKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public FlightModule() {
        super("Flight", "Weeeeee", () -> true);
        instance = this;
        settings.add(speed);
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
            Vec3 vec3 = getPlayer().getDeltaMovement();
            double x, y = 0, z;

            double speed = FlightModule.speed.getValue();
            if (Minecraft.getInstance().options.keyJump.isDown()) {
                y += speed * .3;
            }
            if (Minecraft.getInstance().options.keyShift.isDown()) {
                y -= speed * .3;
            }
            Vec2 move = getPlayer().input.getMoveVector();

            double yaw = MathUtil.toRadians(getPlayer().getYHeadRot());

            x = (move.x * Math.cos(yaw)) - (move.y * Math.sin(yaw));
            z = (move.x * Math.sin(yaw)) + (move.y * Math.cos(yaw));

            getPlayer().setDeltaMovement(x * speed, y - (flip ? bobSpeed : -bobSpeed), z * speed);
            flip = !flip;
        }
    };

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getPlayer().setDeltaMovement(0, 0, 0);
    }
}