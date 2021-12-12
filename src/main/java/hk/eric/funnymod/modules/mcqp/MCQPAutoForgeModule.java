package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MCQPAutoForgeModule extends ToggleableModule {

    private static MCQPAutoForgeModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAutoForgeKeybind", null, () -> true, -1, () -> instance.toggle());
    private static final EventHandler<TickEvent> tickHandler = new EventHandler<>() {
        @Override
        public void handle(TickEvent tickEvent) {
            click();
        }
    };

    public MCQPAutoForgeModule() {
        super("MCQPAutoForge", "Automatically clicks smelting QTE for you", () -> true);
        instance = this;
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EventManager.getInstance().register(tickHandler);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EventManager.getInstance().unregister(tickHandler);
    }

    private static void click() {
        Screen screen = mc.screen;
        if (screen != null) {
            if(screen instanceof ContainerScreen containerScreen) {
                if(containerScreen.getTitle().getString().contains("§b鑄造加工")) {
                    ChestMenu menu = containerScreen.getMenu();
                    menu.slots.forEach(slot -> {
                        ItemStack itemStack = slot.getItem();
                        if (itemStack.getItem().equals(Items.IRON_INGOT)) {
                            if(itemStack.getDisplayName().getString().equals("[鑄造]")) {
                                getGameMode().handleInventoryMouseClick(menu.containerId,slot.index,0, ClickType.CLONE,getPlayer());
                            }
                        }
                    });
                }
            }
        }
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}