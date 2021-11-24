package hk.eric.funnymod.modules.mcqp;

import baritone.api.event.events.type.EventState;
import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventListener;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;

public class MCQPFastReviveModule extends ToggleableModule {

    private static MCQPFastReviveModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPFastReviveKeybind", "", () -> true, -1, () -> instance.toggle());

    private static final EventHandler<TickEvent> spamRevive = new EventHandler<>() {
        @Override
        @EventListener
        public void handle(TickEvent e) {
            if (e.getState() == EventState.POST) {
                assert FunnyModClient.mc.player != null;
                FunnyModClient.mc.player.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
            }
        }
    };

    public MCQPFastReviveModule() {
        super("MCQPFastRevive", "Spams revive button", () -> true);
        instance = this;
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EventManager.getInstance().register(spamRevive);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EventManager.getInstance().unregister(spamRevive);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}