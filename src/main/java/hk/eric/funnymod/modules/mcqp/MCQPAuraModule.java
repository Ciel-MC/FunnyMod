package hk.eric.funnymod.modules.mcqp;

import baritone.api.event.events.type.EventState;
import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.openedClasses.OpenLevel;
import hk.eric.funnymod.utils.EntityUtil;
import hk.eric.funnymod.utils.PacketUtil;
import hk.eric.funnymod.utils.classes.Pair;
import hk.eric.funnymod.utils.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MCQPAuraModule extends ToggleableModule {

    static final Minecraft mc = FunnyModClient.mc;

    private static MCQPAuraModule instance;
    public static final IntegerSetting range = new IntegerSetting("Range", "MCQPAuraRange", "The range of the aura", 1, 20, 10);
    public static final IntegerSetting maxTarget = new IntegerSetting("Max Target", "MCQPAuraTargets", "How many entity to attack in 1 tick", 1, 20, 5);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAuraKeybind", "", -1, () -> instance.toggle());

    private static final EventHandler<TickEvent> killaura = new EventHandler<TickEvent>() {
        @Override
        public void handle(TickEvent e) {
            if (e.getState() == EventState.POST) {
                LocalPlayer player = mc.player;
                if (player == null) return;
                double x = player.getX(), y = player.getY(), z = player.getZ();
                ClientLevel level = mc.level;
                if (level == null) return;
                List<LivingEntity> entityToAttack = new ArrayList<>();
                ((OpenLevel) level).getEntities().getAll().forEach(entity -> {
                    if (EntityUtil.isHostile(entity) || EntityUtil.isPassive(entity)) {
                        if (entity.isInvulnerable()) return;
                        if (entity instanceof LivingEntity livingEntity) {
                            if (livingEntity.getHealth() <= 0) return;
                            double x1 = entity.getX(), y1 = entity.getY() + entity.getBoundingBox().getYsize() / 2, z1 = entity.getZ();
                            if ((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) + (z1 - z) * (z1 - z) <= range.getValue() * range.getValue()) {
                                entityToAttack.add(livingEntity);
                            }
                        }
                    }
                });
                entityToAttack.sort(Comparator.comparingDouble(MCQPAuraModule::getHealthOfEntity));
                entityToAttack.subList(0, Math.min(entityToAttack.size(), maxTarget.getValue())).forEach(entity -> {
                    Pair<Float, Float> yawPitch = PlayerUtil.getRotFromCoordinate(player, EntityAnchorArgument.Anchor.EYES, entity.getX(), entity.getY() + entity.getBoundingBox().getYsize() / 2, entity.getZ());
                    assert mc.getConnection() != null;
                    float rotY = yawPitch.getSecond(), rotX = yawPitch.getFirst();
                    PacketUtil.sendPacket(new ServerboundMovePlayerPacket.Rot(rotY, rotX, player.isOnGround()));
                    PacketUtil.sendPacket(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                });
            }
        }
    };

    private static double getHealthOfEntity(LivingEntity entity) {
        return entity.getHealth();
    }

    public MCQPAuraModule() {
        super("MCQPAura", "Killaura for MCQP");
        instance = this;
        settings.add(range);
        settings.add(maxTarget);
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EventManager.getInstance().register(killaura);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EventManager.getInstance().unregister(killaura);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}