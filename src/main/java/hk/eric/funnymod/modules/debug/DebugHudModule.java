package hk.eric.funnymod.modules.debug;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.setting.IClient;
import hk.eric.funnymod.gui.setting.FloatSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.HasComponents;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.MathUtil;
import net.minecraft.world.entity.Entity;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Collections;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class DebugHudModule extends ToggleableModule implements HasComponents {

    public static int pps = 0;

    private static final Timer timer = new Timer();
    private static DebugHudModule instance;
    public static final FloatSetting launchStrength = new FloatSetting("Launch Strength", "DebugHudLaunchStrength", "The strength you're launched at", 0, 10, 2, 0.1f);
    public static final FloatSetting launchYBoost = new FloatSetting("Y boost", "DebugHudYBoost", "The amount of Y boost that's given with the launch", 0, 10, .5f, 0.1f);
    public static final KeybindSetting launch = new KeybindSetting("Launch", "DebugHudLaunchKeybind", "Launches you forward", GLFW.GLFW_KEY_G, ()->launchEntity(getPlayer()));
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "DebugHudKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public DebugHudModule() {
        super("DebugHud", "Debug Hud", () -> true);
        instance = this;
        settings.add(launchStrength);
        settings.add(launchYBoost);
        settings.add(launch);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public static void count() {
        pps++;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pps--;
            }
        }, 1000);
    }

    public Set<IFixedComponent> getComponents(IClient client, IContainer<IFixedComponent> container, Supplier<Animation> animation) {
        return Collections.singleton(new HUDComponent(() -> "MCQP Display", new Point(500, 10), "mcqp") {
            @Override
            public void render(Context context) {
                super.render(context);
                context.getInterface().drawString(context.getPos(), 10, "Packets Per Second: " + pps, new Color(255, 255, 255));
            }

            @Override
            public Dimension getSize(IInterface inter) {
                return new Dimension(500, 30);
            }
        });
    }

    private static void launchEntity(Entity entity) {
        float theta = MathUtil.toRadians(getPlayer().getYHeadRot());
        entity.lerpMotion(-Math.sin(theta) * launchStrength.getValue(), launchYBoost.getValue(), Math.cos(theta) * launchStrength.getValue());
    }
}