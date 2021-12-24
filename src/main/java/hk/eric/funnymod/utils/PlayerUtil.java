package hk.eric.funnymod.utils;

import hk.eric.funnymod.utils.classes.XYRot;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
}
