package hk.eric.funnymod.utils;

import hk.eric.funnymod.FunnyModClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ContainerUtil {

    public static boolean titleContains(String text) {
        return getContainerScreen() != null && getContainerScreen().getTitle().getString().contains(text);
    }

    public static @Nullable ContainerScreen getContainerScreen() {
        return getContainerScreen(FunnyModClient.mc.screen);
    }

    public static @Nullable ContainerScreen getContainerScreen(Screen screen) {
        if (screen != null) {
            if (screen instanceof ContainerScreen containerScreen) {
                return containerScreen;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    public static ChestMenu getChestMenu() {
        return getChestMenu(getContainerScreen());
    }

    public static ChestMenu getChestMenu(ContainerScreen containerScreen) {
        return containerScreen == null? null: containerScreen.getMenu();
    }


    /**
     * Find the first slot that contains the item and click it.
     * @param item The item to click
     * @param itemName The name of the item to click
     * @return true if the item was clicked, false if there is no gui or the item was not found
     */
    public static boolean findAndClick(@Nullable Item item, String itemName) {
        Slot slot = find(item, itemName);
        if (slot != null) {
            click(getChestMenu(), slot);
            return true;
        }
        return false;
    }

    public static void click(Slot slot) {
        click(getChestMenu(), slot);
    }

    public static void click(ChestMenu menu, Slot slot) {
        assert FunnyModClient.mc.gameMode != null;
        FunnyModClient.mc.gameMode.handleInventoryMouseClick(menu.containerId, slot.index, 0, ClickType.CLONE, FunnyModClient.mc.player);
    }

    public static Slot find(@Nullable Item item, String itemName) {
        return find(getChestMenu(), item, itemName);
    }

    public static Slot find(ChestMenu menu, @Nullable Item item, String itemName) {
        Optional<Slot> optional = menu.slots.stream().filter(slot -> {
            ItemStack itemStack = slot.getItem();
            return (itemStack.getItem().equals(item) || item == null) && itemStack.getDisplayName().getString().contains(itemName);
        }).findFirst();
        return optional.orElse(null);
    }
}
