package hk.eric.funnymod.modules.debug;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.setting.IClient;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PacketEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
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

public class DebugModule extends ToggleableModule implements HasComponents {

    public static int ppsSend = 0;
    public static int ppsReceived = 0;

    private static final EventHandler<PacketEvent> packetEventHandler = new EventHandler<>() {
        @Override
        public void handle(PacketEvent event) {
            if (event instanceof PacketEvent.SendPacketEvent && logSendPacket.isOn()) {
                System.out.println("[FunnyMod] Send Packet: " + event.getPacket().getClass().getSimpleName());
            }else if (event instanceof PacketEvent.ReceivePacketEvent && logReceivePacket.isOn()) {
                System.out.println("[FunnyMod] Receive Packet: " + event.getPacket().getClass().getSimpleName());
            }
        }
    };

//    private static final EventHandler<TickEvent.Pre> tickEventPreHandler = new EventHandler<>() {
//        @Override
//        public void handle(TickEvent.Pre event) {
//            if (getLevel() == null) return;
//            StreamSupport.stream(((OpenLevel) getLevel()).callGetEntities().getAll().spliterator(), false).filter(entity -> !(entity instanceof LocalPlayer)).min(Comparator.comparingDouble(entity -> entity.distanceTo(getPlayer()))).ifPresent(entity -> {
//                XYRot xyRot = PlayerUtil.getRotFromCoordinate(getPlayer(), entity.getX(), entity.getY(), entity.getZ());
//                PlayerUtil.setRot(xyRot);
//            });
//        }
//    };

    private static final Timer timer = new Timer();
    private static DebugModule instance;
    public static final BooleanSetting logReceivePacket = new BooleanSetting("Log receive packet", "logReceivePacket", "Logs all packets received by the client", false);
    public static final BooleanSetting logSendPacket = new BooleanSetting("Log send packet", "logSendPacket", "Logs all packets sent by the client", false);
    public static final FloatSetting launchStrength = new FloatSetting("Launch Strength", "DebugLaunchStrength", "The strength you're launched at", 0, 10, 2, 0.1f);
    public static final FloatSetting launchYBoost = new FloatSetting("Y boost", "DebugYBoost", "The amount of Y boost that's given with the launch", 0, 10, .5f, 0.1f);
    public static final KeybindSetting launch = new KeybindSetting("Launch", "DebugLaunchKeybind", "Launches you forward", GLFW.GLFW_KEY_G, ()->launchEntity(getPlayer()));
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "DebugKeybind", null, -1, () -> instance.toggle(), true);

    public DebugModule() {
        super("Debug", "Debug Hud", FunnyModClient.debug);
        instance = this;
        settings.add(logReceivePacket);
        settings.add(logSendPacket);
        settings.add(launchStrength);
        settings.add(launchYBoost);
        settings.add(launch);
        settings.add(keybind);

        registerOnOffHandler(packetEventHandler);
//        registerOnOffHandler(tickEventPreHandler);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public static void countSend() {
        ppsSend++;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ppsSend--;
            }
        }, 1000);
    }

    public static void countReceive() {
        ppsReceived++;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ppsReceived--;
            }
        }, 1000);
    }

    public Set<IFixedComponent> getComponents(IClient client, IContainer<IFixedComponent> container, Supplier<Animation> animation) {
        return Collections.singleton(new HUDComponent(() -> "MCQP Display", new Point(500, 10), "mcqp") {
            @Override
            public void render(Context context) {
                super.render(context);
                Point pos = context.getPos();
                context.getInterface().drawString(pos, 10, "Packets Sent Per Second: " + ppsSend, new Color(0, 255, 0));
                pos.translate(0, 10);
                context.getInterface().drawString(pos, 10, "Packets Received Per Second: " + ppsReceived, new Color(255, 0, 0));
                pos.translate(0, 10);
                context.getInterface().drawString(pos, 10, "Fall distance: " + getPlayer().fallDistance, getPlayer().fallDistance > 3 ? new Color(255, 0, 0) : new Color(0, 255, 0));
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