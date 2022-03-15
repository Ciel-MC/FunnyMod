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
import hk.eric.funnymod.event.events.Render3DEvent;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.FloatSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.HasComponents;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.*;
import hk.eric.funnymod.utils.classes.TimedCounter;
import hk.eric.funnymod.utils.classes.XYRot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Collections;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class DebugModule extends ToggleableModule implements HasComponents {

    private static final Logger logger = LogManager.getLogger("FunnyMod Debug");

    public static int ppsSend = 0;
    public static int ppsReceived = 0;

    private static double distance = 0;

    public static final TimedCounter counter = new TimedCounter(1000);

    private static final EventHandler<PacketEvent> packetEventHandler = new EventHandler<>() {
        @Override
        public void handle(PacketEvent event) {
            if (event instanceof PacketEvent.SendPacketEvent && logSendPacket.isOn()) {
//                System.out.println("[FunnyMod] Send Packet: " + event.getPacket().getClass().getSimpleName());
                logger.info("[FunnyMod] Sending Packet: " + event.getPacket().getClass().getSimpleName());
            }else if (event instanceof PacketEvent.ReceivePacketEvent && logReceivePacket.isOn()) {
                System.out.println("[FunnyMod] Receive Packet: " + event.getPacket().getClass().getSimpleName());
            }
        }
    };

    private static final EventHandler<Render3DEvent> render3DEventHandler = new EventHandler<>() {
        @Override
        public void handle(Render3DEvent event) {
            if (showClosestPoint.isOn()) {
                Entity e = EntityUtil.getClosestEntity();
                if (e == null) return;
                Vec3 boxLoc = MathUtil.closestPointInAABB(getPlayer().getEyePosition(), e.getBoundingBox());
                AABB box = new AABB(boxLoc.add(.1, .1, .1), boxLoc.add(-.1, -.1, -.1));
                distance = MathUtil.getDistance3D(boxLoc, getPlayer().getEyePosition());
                RenderUtil.drawAABB(event.getStack(), box);
            }
        }
    };

    private static final EventHandler<TickEvent.Post> tickEventPostHandler = new EventHandler<>() {
        @Override
        public void handle(TickEvent.Post event) {
            if (look.isOn()) {
                Entity e = EntityUtil.getClosestEntity();
                if (e == null) return;
                Vec3 target = MathUtil.closestPointInAABB(getPlayer().getEyePosition(), e.getBoundingBox());
                XYRot rot = MathUtil.getLookAtRotation(getPlayer(), target);
                RotationUtil.setRot(rot);
            }
        }
    };

    private static final Timer timer = new Timer();
    private static DebugModule instance;
    public static final BooleanSetting logReceivePacket = new BooleanSetting("Log receive packet", "logReceivePacket", "Logs all packets received by the client", false);
    public static final BooleanSetting logSendPacket = new BooleanSetting("Log send packet", "logSendPacket", "Logs all packets sent by the client", false);
    public static final FloatSetting launchStrength = new FloatSetting("Launch Strength", "DebugLaunchStrength", "The strength you're launched at", 0, 10, 2, 0.1f);
    public static final FloatSetting launchYBoost = new FloatSetting("Y boost", "DebugYBoost", "The amount of Y boost that's given with the launch", 0, 10, .5f, 0.1f);
    public static final KeybindSetting launch = new KeybindSetting("Launch", "DebugLaunchKeybind", "Launches you forward", -1, ()->launchEntity(getPlayer()));
    public static final BooleanSetting showClosestPoint = new BooleanSetting("Show closest point", "DebugShowClosestPoint", "Shows the closest point to the player", false);
    public static final BooleanSetting look = new BooleanSetting("Look", "DebugLook", null, false);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "DebugKeybind", null, -1, () -> instance.toggle(), true);

    public DebugModule() {
        super("Debug", "Debug", FunnyModClient.debug);
        instance = this;
        settings.add(logReceivePacket);
        settings.add(logSendPacket);
        settings.add(launchStrength);
        settings.add(launchYBoost);
        settings.add(launch);
        settings.add(showClosestPoint);
        settings.add(look);
        settings.add(keybind);

        registerOnOffHandler(packetEventHandler);
        registerOnOffHandler(render3DEventHandler);
        registerOnOffHandler(tickEventPostHandler);
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
                pos.translate(0, 10);
                context.getInterface().drawString(pos, 10, "X: " + getPlayer().getX() + " Y: " + getPlayer().getY() + " Z: " + getPlayer().getZ(), new Color(255, 255, 255));
                pos.translate(0, 10);
                context.getInterface().drawString(pos, 10, "Pitch: " + getPlayer().getXRot() + " Yaw: " + getPlayer().getYHeadRot(), new Color(255, 255, 255));
                pos.translate(0, 10);
                context.getInterface().drawString(pos, 10, "Closest Point: " + distance, new Color(255, 255, 255));
                pos.translate(0, 10);
                context.getInterface().drawString(pos, 10, "Counter: " + counter.getCount(), new Color(255, 255, 255));
            }

            @Override
            public Dimension getSize(IInterface inter) {
                return new Dimension(500, 70);
            }
        });
    }

    private static void launchEntity(Entity entity) {
        System.out.println("Launching entity");
        float theta = MathUtil.toRadians(getPlayer().getYHeadRot());
        entity.lerpMotion(-Math.sin(theta) * launchStrength.getValue(), launchYBoost.getValue(), Math.cos(theta) * launchStrength.getValue());
    }
}