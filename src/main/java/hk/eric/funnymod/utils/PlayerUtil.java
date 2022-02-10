package hk.eric.funnymod.utils;

import hk.eric.funnymod.mixin.OpenMinecraft;
import hk.eric.funnymod.utils.classes.XYRot;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

import static hk.eric.funnymod.FunnyModClient.player;

public class PlayerUtil {

    public static XYRot getRotFromCoordinate(Player source, double x, double y, double z) {
        return getRotFromCoordinate(source.getX(), source.getEyeY(), source.getZ(), x, y, z);
    }

    public static XYRot getRotFromCoordinate(double x, double y, double z, double x1, double y1, double z1) {
        double xDiff = x1 - x;
        double yDiff = y1 - y;
        double zDiff = z1 - z;
        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        XYRot xyRot = new XYRot();
        xyRot.setXRot(Mth.wrapDegrees((float)(-(Mth.atan2(yDiff, distance) * 57.2957763671875))));
        xyRot.setYRot(Mth.wrapDegrees((float)(Mth.atan2(zDiff, xDiff) * 57.2957763671875) - 90.0f));
        return xyRot;
    }

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

    public static void leftClick() {
        ((OpenMinecraft) Minecraft.getInstance()).invokeStartAttack();
    }

    public static void rightClick() {
        ((OpenMinecraft) Minecraft.getInstance()).invokeStartUseItem();
    }
}
