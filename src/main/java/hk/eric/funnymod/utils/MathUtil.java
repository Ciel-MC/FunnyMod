package hk.eric.funnymod.utils;

import it.unimi.dsi.fastutil.doubles.DoubleComparator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

import static java.lang.Math.*;

public class MathUtil {
    
    public static Comparator<Double> doubleComparator = (DoubleComparator) Double::compare;
    
    public static Comparator<Integer> intComparator = (IntComparator) Integer::compare;

    /**
     * Gets the coordinate between two {@link Vec2}.
     * @param a First coordinate
     * @param b Second coordinate
     * @return The distance between the two coordinates
     */
    public static double getDistance(Vec2 a, Vec2 b) {
        return getDistance(a.x, a.y, b.x, b.y);
    }

    /**
     * Gets the coordinate between two 2D coordinates.
     * @param x1 First X
     * @param y1 First Y
     * @param x2 Second X
     * @param y2 Second Y
     * @return The distance between the two coordinates
     */
    public static double getDistance(double x1, double y1, double x2, double y2) {
        return pythagorean(UnknownSide.LONG, x1 - x2, y1 - y2);
    }

    /**
     * Gets the coordinate between two {@link Vec3}.
     * @param a First coordinate
     * @param b Second coordinate
     * @return The distance between the two coordinates
     */
    public static double getDistance3D(Vec3 a, Vec3 b) {
        return getDistance3D(a.x, a.y, a.z, b.x, b.y, b.z);
    }

    /**
     * Gets the coordinate between two 3D coordinates.
     * @param x1 First X
     * @param y1 First Y
     * @param z1 First Z
     * @param x2 Second X
     * @param y2 Second Y
     * @param z2 Second Z
     * @return The distance between the two coordinates
     */
    public static double getDistance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2) + pow(z1 - z2, 2));
    }

    /**
     * Gets the angle between two {@link Vec2}.
     * @param a First coordinate
     * @param b Second coordinate
     * @return The angle between the two coordinates
     */
    public static double getAngle(Vec2 a, Vec2 b) {
        return getAngle(a.x, a.y, b.x, b.y);
    }

    /**
     * Gets the angle between two 2D coordinates.
     * @param x1 First X
     * @param y1 First Y
     * @param x2 Second X
     * @param y2 Second Y
     * @return The angle between the two coordinates
     */
    public static double getAngle(double x1, double y1, double x2, double y2) {
        return atan2(y2 - y1, x2 - x1);
    }

    /**
     * Rounds number to an integer place.
     * @param value the value to be rounded
     * @param places the number of places to round to
     * @return the rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double pythagorean(Vec2 vec) {
        return pythagorean(vec.x, vec.y);
    }

    public static double pythagorean(double a, double b) {
        return pythagorean(UnknownSide.LONG, a, b);
    }

    public static double pythagorean(UnknownSide unknownSide, double a, double b) {
        return switch (unknownSide) {
            case LONG -> sqrt(a * a + b * b);
            case SHORT -> sqrt(abs(a * a - b * b));
        };
    }

    public static int compare_force(Vec2 vec, double c) {
        return pythagorean_compare(vec.x, vec.y, c);
    }

    /**
     * Compares the length of longer side formed by two shorter sides to the longer side.
     * @param a side 1 of the triangle
     * @param b side 2 of the triangle
     * @param c Side length to be compared to
     * @return -1 if c is longer, 0 if equal, 1 if c is shorter
     */
    public static int pythagorean_compare(double a, double b, double c) {
        return doubleComparator.compare(a * a + b * b, c * c);
    }

    public enum UnknownSide {
        LONG,
        SHORT
    }
}
