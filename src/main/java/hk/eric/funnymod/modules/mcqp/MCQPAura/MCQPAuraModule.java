package hk.eric.funnymod.modules.mcqp.MCQPAura;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.events.MotionEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.DoubleSetting;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.BooleanSettingWithSubSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.EnumSettingWithSubSettings;
import hk.eric.funnymod.mixin.OpenLevel;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PacketUtil;
import net.minecraft.world.entity.LivingEntity;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MCQPAuraModule extends ToggleableModule {
    private static MCQPAuraModule instance;
    public static final EnumSettingWithSubSettings<Mode> mode = new EnumSettingWithSubSettings<>("Mode", "MCQPAuraMode", "Mode for MCQPAura", Mode.NORMAL, Mode.class);
    //Normal settings
    public static final BooleanSettingWithSubSetting cycleAttack = new BooleanSettingWithSubSetting("Cycle","MCQPAuraCycle","Cycle different entities on each tick",false);
    public static final IntegerSetting cycleTime = new IntegerSetting("CycleTime","MCQPAuraCycleTime","Time to cycle", 1, 5, 2);
    public static final DoubleSetting range = new DoubleSetting("Range", "MCQPAuraRange", "The range of the aura", 1, 20, 10, .1);
    public static final IntegerSetting maxTarget = new IntegerSetting("Max Target", "MCQPAuraTargets", "How many entity to attack in 1 tick", 1, 20, 5);
    //Rage settings
    public static final DoubleSetting rageRange = new DoubleSetting("Rage Range", "MCQPAuraRageRange", "The range of rage", 1, 50, 10, .1);
    public static final BooleanSetting inGui = new BooleanSetting("In GUI", "MCQPAuraInGui", "Whether to attack in GUI(May cause inability to revive)", true);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAuraKeybind", null, -1, () -> instance.toggle(), true);

    private static final EventHandler<MotionEvent> killaura = new EventHandler<>() {
        @Override
        public void handle(MotionEvent e) {
            if (inGui.isOn() && mc.screen != null) return;
            if (getPlayer() == null) return;
            if (e.getState() == EventState.PRE) {
                double x = getPlayer().getX(), y = getPlayer().getY(), z = getPlayer().getZ();
                Stream<LivingEntity> entities = StreamSupport.stream(((OpenLevel)getLevel()).callGetEntities().getAll().spliterator(), false).filter(entity -> entity instanceof LivingEntity).map(entity -> (LivingEntity) entity);
                AuraMode auraMode = switch (mode.getValue()) {
                    case NORMAL -> AuraModes.NORMAL_MODE;
                    case RAGE -> AuraModes.RAGE_MODE;
                };
                auraMode.getEntities().apply(entities,getPlayer(),range.getValue()).forEach(entity -> auraMode.getAttack().accept(entity, getPlayer(), PacketUtil::sendPacket));
            }
        }
    };

    public MCQPAuraModule() {
        super("MCQPAura", "Killaura for MCQP");
        instance = this;
        settings.add(mode);
        mode.addSubSetting(Mode.NORMAL,cycleAttack);
        cycleAttack.addSubSetting(true,cycleTime);
        mode.addSubSetting(Mode.NORMAL,range);
        mode.addSubSetting(Mode.NORMAL,maxTarget);

        mode.addSubSetting(Mode.RAGE,rageRange);
        settings.add(inGui);
        settings.add(keybind);

        registerOnOffHandler(killaura);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public enum Mode {
        NORMAL,
        RAGE
        //TODO: Smart Rage / Semi Rage
    }
}