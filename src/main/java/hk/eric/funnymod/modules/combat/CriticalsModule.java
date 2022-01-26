package hk.eric.funnymod.modules.combat;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventPriority;
import hk.eric.funnymod.event.events.AttackEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PacketUtil;
import hk.eric.funnymod.utils.PlayerUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

public class CriticalsModule extends ToggleableModule {

    private static CriticalsModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "CriticalsKeybind", null, -1, () -> instance.toggle());

    private static final EventHandler<AttackEvent> attackEventHandler = new EventHandler<>(EventPriority.LOWEST) {
        @Override
        public void handle(AttackEvent event) {
            if (!event.isCancelled()) {
                if (canCrit(getPlayer(),false)) {
                    Vec3 pos = PlayerUtil.exactPosition(getPlayer());
                    PacketUtil.sendPacket(PacketUtil.createPos(pos.x, pos.y + 0.11, pos.z, false));
                    PacketUtil.sendPacket(PacketUtil.createPos(pos.x, pos.y + 0.1100013579, pos.z, false));
                    PacketUtil.sendPacket(PacketUtil.createPos(pos.x, pos.y + 0.0000013579, pos.z, false));
                }
            }
        }
    };

    public CriticalsModule() {
        super("Criticals", "Automatically crits");
        instance = this;
        registerOnOffHandler(attackEventHandler);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    private static boolean canCrit(LocalPlayer player, boolean ignoreOnGround) {
        return !player.isInLava() && !player.isInWater() && !player.onClimbable() && !player.isNoGravity() &&
                !player.hasEffect(MobEffects.LEVITATION) && !player.hasEffect(MobEffects.BLINDNESS) &&
                !player.hasEffect(MobEffects.SLOW_FALLING) && !player.isHandsBusy() && (player.isOnGround() || ignoreOnGround);/* &&
                !ModuleFly.enabled && !(ModuleLiquidWalk.enabled && ModuleLiquidWalk.standingOnWater())*/
    }

}