package hk.eric.funnymod.modules.combat;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.ericLib.utils.classes.Flag;
import hk.eric.ericLib.utils.classes.TimedAPSCounter;
import hk.eric.ericLib.utils.classes.pathFind.AStarPathFinder;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.events.MotionEvent;
import hk.eric.funnymod.event.events.Render3DEvent;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.EnumSettingWithSubSettings;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.modules.combat.killaura.KillauraMode;
import hk.eric.funnymod.modules.combat.killaura.KillauraModes;
import hk.eric.funnymod.modules.debug.DebugModule;
import hk.eric.funnymod.utils.EntityUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;

public class KillAuraModule extends ToggleableModule {

    private static EventState state = EventState.PRE;

    private static final TimedAPSCounter timer = new TimedAPSCounter();

    private static final Flag attackTick = new Flag();

    private static KillAuraModule instance;
    public static final EnumSettingWithSubSettings<KillAuraMode> killAuraMode = new EnumSettingWithSubSettings<>("Killaura Mode", "KillAuraMode", "Mode", KillAuraMode.TELEPORT, KillAuraMode.class);
    public static final IntegerSetting attacksPerSecond = new IntegerSetting("Max Attacks Per Second", "KillAuraAttacksPerSecond", "Attacks per second", 1, 20, 18, timer::setAPS);
    public static final EnumSetting<KillAuraType> type = new EnumSetting<>("Type", "KillAuraType", "The type of the tick to attack", KillAuraType.PRE, KillAuraType.class);
    public static final EnumSetting<SortType> sortType = new EnumSetting<>("Sort Type", "InfiniteAuraSortType", "How to sort enemies", SortType.HEALTH, SortType.class);

    public static final IntegerSetting infiniteAuraRange = new IntegerSetting("Range", "InfiniteAuraRange", "The range of the aura", 1, 500, 100);
    public static final IntegerSetting infiniteAuraMaxStep = new IntegerSetting("Max steps", "InfiniteAuraMaxStep", "The most amount of blocks that will be searched for each entity", 1, 2000, 500);
    public static final IntegerSetting infiniteAuraPacketLimit = new IntegerSetting("Packet Limit", "InfiniteAuraPacketLimit", "The limit of packets to send per tick", -1, 5000, 500);
    public static final IntegerSetting infiniteAuraTargetLimit = new IntegerSetting("Target Limit", "InfiniteAuraTargetLimit", "Maximum numbers of target to attack", 1, 50, 1);
    public static final IntegerSetting infiniteAuraPacketRate = new IntegerSetting("Packet Rate", "InfiniteAuraPacketRate", "The rate which movement packets are sent, the lower the setting, the more is send, higher numbers could cause you to be lagged back", 1, 5, 2);
    public static final EnumSetting<AStarPathFinder.distanceAccuracy> infiniteAuraDistanceCalculationAccuracy = new EnumSetting<>("Distance Calculation Accuracy", "InfiniteAuraDistanceCalculationAccuracy", "The accuracy used to calculate the path for infinite aura", AStarPathFinder.distanceAccuracy.FLOAT, AStarPathFinder.distanceAccuracy.class);
    public static final EnumSettingWithSubSettings<TeleportBypass> infiniteAuraBypass = new EnumSettingWithSubSettings<>("Infinite Aura Bypass", "KillAuraInfiniteAuraBypass", "Choose Bypass mode", TeleportBypass.PAPER, TeleportBypass.class);

    public static final IntegerSetting infiniteAuraPaperDistance = new IntegerSetting("Distance", "KillAuraInfiniteAuraPaperDistance", "The max distance you can move in 1 tick(10 on paper by default)", 1, 50, 10);

    public static final BooleanSetting noAttackCooldown = new BooleanSetting("No attack cooldown", "KillAuraNoCooldown", "Assume no 1.9+ attack cooldown", false);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "KillAuraKeybind", null, -1, () -> instance.toggle(), true);

    private static final EventHandler<Render3DEvent.Pre> render3DEventHandler = new EventHandler<>() {
        @Override
        public void handle(Render3DEvent.Pre event) {
            if (timer.canAttack()) {
                attackTick.set();
            }
        }
    };

    private static final EventHandler<MotionEvent> aura = new EventHandler<>(){
        @Override
        public void handle(MotionEvent motionEvent) {
            if (!noAttackCooldown.isOn() && getPlayer().getAttackStrengthScale(0.0f) < 1.0f) return;
            if (shouldAttack(motionEvent.getState()) && attackTick.consume()) {
                KillauraMode mode = getMode();
                mode.process(
                        EntityUtil.streamLivingEntities(new EntityUtil.EntityFilterBuilder<LivingEntity>()
                                .setPredicate(livingEntity -> !(livingEntity instanceof LocalPlayer))
                                .createEntityFilter()
                        ).filter(LivingEntity::isAlive)
                ).peek((e) -> DebugModule.counter.count()).forEach(mode::attack);
            }
        }
    };

    private static final EventHandler<Render3DEvent> renderHandler = new EventHandler<>() {
        @Override
        public void handle(Render3DEvent event) {
            if(event.getState() == EventState.POST) {
                getMode().render(event);
            }
        }
    };

    private static final EventHandler<TickEvent.Pre> tickEventHandler = new EventHandler<>() {
        @Override
        public void handle(TickEvent.Pre event) {
            getMode().tick(event);
        }
    };

    public KillAuraModule() {
        super("KillAura", null);
        instance = this;
        settings.add(killAuraMode);
        settings.add(attacksPerSecond);
        settings.add(type);
        settings.add(sortType);
        killAuraMode.addSubSettings(KillAuraMode.TELEPORT, infiniteAuraRange, infiniteAuraMaxStep, infiniteAuraPacketLimit, infiniteAuraTargetLimit, infiniteAuraBypass);
        infiniteAuraBypass.addSubSettings(TeleportBypass.PAPER, infiniteAuraPaperDistance);

        settings.add(noAttackCooldown);
        settings.add(keybind);

        timer.setAPS(attacksPerSecond.getValue());

        registerOnOffHandler(aura);
        registerOnOffHandler(renderHandler);
        registerOnOffHandler(tickEventHandler);
        registerOnOffHandler(render3DEventHandler);
    }

    private static boolean shouldAttack(EventState state) {
        return switch (type.getValue()) {
            case PRE -> state == EventState.PRE;
            case POST -> state == EventState.POST;
            case MIX -> {
                if (KillAuraModule.state == state) {
                    yield false;
                }else {
                    KillAuraModule.state = state;
                    yield true;
                }
            }
            case BOTH -> true;
        };
    }

    private static KillauraMode getMode() {
        return KillauraModes.getMode(killAuraMode);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public enum KillAuraType {
        PRE,
        POST,
        MIX,
        BOTH
    }

    public enum SortType {
        DISTANCE,
        HEALTH,
        ARMOR
    }

    public enum KillAuraMode {
        NORMAL,
        TELEPORT
    }

    public enum Swing {
        //Only show swing client side
        CLIENT,
        //Only send swing packet
        SERVER,
        //Send swing packet and show swing client side
        BOTH,
        //Don't send swing packet or show swing client side
        NONE
    }

    public enum TeleportBypass {
        PAPER,
        NONE
    }
}