package hk.eric.funnymod.modules.movement;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PlayerMoveEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.mixin.OpenVec3;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.MathUtil;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public class StrafeModule extends ToggleableModule {

    private static StrafeModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "StrafeKeybind", "", () -> true, -1, () -> instance.toggle());

    private static final EventHandler<PlayerMoveEvent> strafeEventHandler = new EventHandler<>() {
        @Override
        public void handle(PlayerMoveEvent playerMoveEvent) {
            if (getToggle().isOn()) {
                Vec3 movement = playerMoveEvent.getMovement();
                handleMovement(movement, getDirectionYaw(getPlayer()));
            }
        }
    };

    public StrafeModule() {
        super("Strafe", "Changes your midair strafe speed", () -> true);
        instance = this;
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.register(strafeEventHandler);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.unregister(strafeEventHandler);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    public static void handleMovement(Vec3 movement, float yaw) {
        double angle = Math.toRadians(yaw);
        double speed = MathUtil.pythagorean(movement.x, movement.z);
        OpenVec3 vec3 = (OpenVec3) movement;
        vec3.setX(-Math.sin(angle) * speed);
        vec3.setZ(Math.cos(angle) * speed);
    }

    public static float getDirectionYaw(LocalPlayer player) {
        float yaw = player.getYRot();
        Options options = mc.options;

        if(options.keyDown.isDown()) {
            yaw += 180;
        }

        float forward = 1f;
        if (options.keyDown.isDown()) {
            forward = -0.5f;
        } else if (options.keyUp.isDown()) {
            forward = 0.5f;
        }

        if (options.keyLeft.isDown()) {
            yaw -= 90f * forward;
        }
        if (options.keyRight.isDown()) {
            yaw += 90f * forward;
        }

        return yaw;
    }
}