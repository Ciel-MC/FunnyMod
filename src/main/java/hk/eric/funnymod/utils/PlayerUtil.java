package hk.eric.funnymod.utils;

import hk.eric.funnymod.utils.classes.Pair;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PlayerUtil {
    public static Pair<Float, Float> getRotFromCoordinate(Entity source, EntityAnchorArgument.Anchor anchor, double x, double y, double z) {
        Vec3 vec32 = anchor.apply(source);
        double d = x - vec32.x;
        double e = y - vec32.y;
        double f = z - vec32.z;
        double g = Math.sqrt(d * d + f * f);
        Pair<Float, Float> yawPitch = new Pair<>();
        yawPitch.setFirst(Mth.wrapDegrees((float)(-(Mth.atan2(e, g) * 57.2957763671875))));
        yawPitch.setSecond(Mth.wrapDegrees((float)(Mth.atan2(f, d) * 57.2957763671875) - 90.0f));
        return yawPitch;
    }
}
