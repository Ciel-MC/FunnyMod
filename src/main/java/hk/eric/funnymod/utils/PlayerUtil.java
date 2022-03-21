package hk.eric.funnymod.utils;

import hk.eric.funnymod.mixin.OpenMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

import static hk.eric.funnymod.FunnyModClient.player;

public class PlayerUtil {

    public static Vec3 exactPosition(Player player) {
        return new Vec3(player.getX(), player.getEyeY(), player.getZ());
    }

    public static void setHotbar(int slot) {
        PlayerUtil.setHotbar(slot, false);
    }

    public static void setHotbar(int slot, boolean force) {
        if (slot < 0 || slot > 8) {
            return;
        }
        if (force || !(slot == player().getInventory().selected)) {
            player().getInventory().selected = slot;
        }
    }

    public static int findInHotbar(Predicate<ItemStack> predicate) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player().getInventory().getItem(i);
            if (itemStack != null && predicate.test(itemStack)) {
                return i;
            }
        }
        return -1;
    }

    public static void switchToHotbar(Predicate<ItemStack> predicate) {
        int slot = findInHotbar(predicate);
        if (slot != -1) {
            setHotbar(slot);
        }
    }

    public static boolean leftClick() {
        return ((OpenMinecraft) Minecraft.getInstance()).invokeStartAttack();
    }

    public static void rightClick() {
        ((OpenMinecraft) Minecraft.getInstance()).invokeStartUseItem();
    }
}
