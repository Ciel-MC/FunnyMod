package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.setting.IClient;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.UseItemEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.HasComponents;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.time.Time;
import hk.eric.funnymod.utils.time.TimeUnit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class MCQPHudModule extends ToggleableModule implements HasComponents {

    private static final Timer timer = new Timer(true);

    static {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(secondsLeft > 0) secondsLeft--;
            }
        }, 0, 1000);
    }

    private static long secondsLeft;

    private static final EventHandler<UseItemEvent> xpBoostTime = new EventHandler<>() {
        @Override
        public void handle(UseItemEvent useItemEvent) {
            if (!trackXPBoostTime.isOn()) return;
            ItemStack item = useItemEvent.getItem();
            if (item.getItem() == Items.PAPER) {
                List<Component> components = item.getTooltipLines(getPlayer(), TooltipFlag.Default.NORMAL);
                components.forEach(component -> {
                    String s = component.getString();
                    if(s.startsWith("加倍時間: ")) {
                        secondsLeft = Time.of(s.substring(5)).get(TimeUnit.SECOND);
                    }
                });
            }
        }
    };

    private static MCQPHudModule instance;
    public static final BooleanSetting trackXPBoostTime = new BooleanSetting("Track XP Boost Time", "MCQPHudTrackXPBoost","Tracks your time left on xp boosts",true, (b) -> {
        if (b) EventManager.getInstance().register(xpBoostTime); else EventManager.getInstance().unregister(xpBoostTime);
    });
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPHudKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public MCQPHudModule() {
        super("MCQPHud", "Displays information for MCQP", () -> true);
        instance = this;
        settings.add(trackXPBoostTime);
        settings.add(keybind);

        registerOnOffHandler(xpBoostTime);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    @Override
    public Set<IFixedComponent> getComponents(IClient client, IContainer<IFixedComponent> container, Supplier<Animation> animation) {
        return Collections.singleton(new HUDComponent(() -> "MCQP Display", new Point(250, 10), "mcqp") {
            @Override
            public void render(Context context) {
                super.render(context);
                long hours = secondsLeft / 3600;
                long minutes = (secondsLeft % 3600) / 60;
                long seconds = secondsLeft % 60;
                context.getInterface().drawString(context.getPos(), 10, "Time left on pass: " + (secondsLeft == 0 ? "Inactive" : String.format("%02d:%02d:%02d", hours, minutes, seconds)), new Color(255, 255, 255));
            }

            @Override
            public Dimension getSize(IInterface inter) {
                return new Dimension(200, 10);
            }
        });
    }
}