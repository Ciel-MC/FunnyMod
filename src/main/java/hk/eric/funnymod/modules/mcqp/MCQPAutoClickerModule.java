package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.ericLib.utils.ClientPacketUtil;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.events.MotionEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class MCQPAutoClickerModule extends ToggleableModule {

    private static MCQPAutoClickerModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAutoClickerKeybind", null, -1, () -> instance.toggle(), true);

    private static final EventHandler<MotionEvent> autoclicker = new EventHandler<>() {
        @Override
        public void handle(MotionEvent e) {
            if (e.getState() == EventState.PRE) {
                if (getPlayer() == null) return;
                getPlayer().swing(InteractionHand.MAIN_HAND);
                if (FunnyModClient.mc.hitResult.getType() == HitResult.Type.BLOCK) {
                    ClientPacketUtil.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, ((BlockHitResult) mc.hitResult).getBlockPos(), getPlayer().getDirection()));
                }
            }
        }
    };

    public MCQPAutoClickerModule() {
        super("MCQPAutoClicker", "Automatically attacks on MCQP");
        instance = this;
        settings.add(keybind);

        registerOnOffHandler(autoclicker);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}