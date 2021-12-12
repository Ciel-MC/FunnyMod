package hk.eric.funnymod.modules.mcqp;

import baritone.api.event.events.type.EventState;
import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PacketUtil;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class MCQPAutoClickerModule extends ToggleableModule {

    private static MCQPAutoClickerModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAutoClickerKeybind", null, -1, () -> instance.toggle());

    private static final EventHandler<TickEvent> autoclicker = new EventHandler<>() {
        @Override
        public void handle(TickEvent e) {
            if (e.getState() == EventState.PRE) {
                if(getPlayer() == null) return;
                if(FunnyModClient.mc.hitResult.getType() == HitResult.Type.BLOCK) {
                    PacketUtil.sendPacket(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, ((BlockHitResult) mc.hitResult).getBlockPos(), getPlayer().getDirection()));
                }else {
                    PacketUtil.sendPacket(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                }
            }
        }
    };

    public MCQPAutoClickerModule() {
        super("MCQPAutoClicker", "Automatically attacks on MCQP");
        instance = this;
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EventManager.getInstance().register(autoclicker);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EventManager.getInstance().unregister(autoclicker);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}