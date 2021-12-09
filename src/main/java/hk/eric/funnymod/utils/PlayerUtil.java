package hk.eric.funnymod.utils;

import hk.eric.funnymod.utils.classes.Pair;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class PlayerUtil {

    public static Pair<Float, Float> getRotFromCoordinate(Entity source, Entity target) {
        return getRotFromCoordinate(source, target.getX(), target.getY(), target.getZ());
    }

    public static Pair<Float, Float> getRotFromCoordinate(Entity source, double x, double y, double z) {
        return getRotFromCoordinates(source.getX(), source.getY(), source.getZ(), source.getEyeHeight(), x, y, z);
    }

    public static Pair<Float, Float> getRotFromCoordinates(double x, double y, double z, double yOffset, double x1, double y1, double z1) {
        double xDiff = x1 - x;
        double yDiff = y1 - y + yOffset;
        double zDiff = z1 - z;
        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        Pair<Float, Float> yawPitch = new Pair<>();
        yawPitch.setFirst(Mth.wrapDegrees((float)(-(Mth.atan2(yDiff, distance) * 57.2957763671875))));
        yawPitch.setSecond(Mth.wrapDegrees((float)(Mth.atan2(zDiff, xDiff) * 57.2957763671875) - 90.0f));
        return yawPitch;
    }
}
