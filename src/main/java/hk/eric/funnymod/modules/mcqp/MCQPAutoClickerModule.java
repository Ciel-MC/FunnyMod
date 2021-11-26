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
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;

public class MCQPAutoClickerModule extends ToggleableModule {

    private static MCQPAutoClickerModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAutoClickerKeybind", "", () -> true, -1, () -> instance.toggle());

    private static final EventHandler<TickEvent> autoclicker = new EventHandler<TickEvent>() {
        @Override
        public void handle(TickEvent e) {
            if (e.getState() == EventState.POST) {
                LocalPlayer p = FunnyModClient.mc.player;
                if(p == null) return;
                PacketUtil.sendPacket(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            }
        }
    };

    public MCQPAutoClickerModule() {
        super("MCQPAutoClicker", "Automatically attacks on MCQP", () -> true);
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