package hk.eric.funnymod.utils;

import it.unimi.dsi.fastutil.doubles.DoubleComparator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

import static java.lang.Math.*;

public class MathUtil {

    static final int precision = 500; // gradations per degree, adjust to suit

    static final int modulus = 360*precision;
    static final float[] sin = new float[modulus]; // lookup table
    static {
        // a static initializer fills the table
        // in this implementation, units are in degrees
        for (int i = 0; i<sin.length; i++) {
            sin[i]=(float)Math.sin((i*Math.PI)/(precision*180));
        }
    }
    public static final double toRadians = PI / 180;
    public static final double toDegrees = 180 / PI;
    public static final Comparator<Double> doubleComparator = (DoubleComparator) Double::compare;
    
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
        return getDistance3D(a, b, true);
    }

    /**
     * Gets the coordinate between two {@link Vec3}.
     * @param a First coordinate
     * @param b Second coordinate
     * @return The distance between the two coordinates
     */
    public static double getDistance3D(Vec3 a, Vec3 b, boolean useSqrt) {
        return getDistance3D(a.x, a.y, a.z, b.x, b.y, b.z, useSqrt);
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
        return getDistance3D(x1, y1, z1, x2, y2, z2, true);
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
    public static double getDistance3D(double x1, double y1, double z1, double x2, double y2, double z2, boolean useSqrt) {
        double x = x2 - x1;
        double y = y2 - y1;
        double z = z2 - z1;
        double distSquared = x * x + y * y + z * z;
        return useSqrt ? sqrt(distSquared) : distSquared;
    }

    public static int compareDistance3D(Vec3 a, Vec3 b, double threshold) {
        return compareDistance3D(a.x, a.y, a.z, b.x, b.y, b.z, threshold);
    }

    public static int compareDistance3D(double x1, double y1, double z1, double x2, double y2, double z2, double threshold) {
        double x = x2 - x1;
        double y = y2 - y1;
        double z = z2 - z1;
        return doubleComparator.compare(x * x + y * y + z * z, threshold * threshold);
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

    /**
     * Generates the component of a vector
     * @param yaw Left & right
     * @param pitch Up & down
     * @param distance The length of the vector
     * @return The component of the vector
     */
    public static Vec3 getCoordFromAngles(float yaw, float pitch, float distance) {
        double x = distance * -Math.sin(toRadians(yaw) * Math.cos(toRadians(pitch)));
        double y = distance * -Math.sin(toRadians(pitch));
        double z = distance * Math.cos(toRadians(yaw) * Math.cos(toRadians(pitch)));
        return new Vec3(x,y,z);
    }

    public static Vec3 getCoordFromAngles(float yaw, float pitch) {
        return getCoordFromAngles(yaw, pitch, 1);
    }

    public static double toRadians(double degrees) {
        return degrees * toRadians;
    }

    public static float toRadians(float degrees) {
        return (float) toRadians((double) degrees);
    }

    public static double toDegrees(double radians) {
        return radians * toDegrees;
    }

    public static float toDegrees(float degrees) {
        return (float) toDegrees((double) degrees);
    }

    /**
     * Fast way to calculate inverse square root.
     * @param number The value to calculate the inverse square root of
     * @return The inverse square root of the value
     */
    public static double invSqrt(double number) {
        return invSqrt(number, 2);
    }

    /**
     * Fast way to calculate inverse square root.
     * @param number The value to calculate the inverse square root of
     * @param precision The precision of the calculation, 4 for maximum precision
     * @return The inverse square root of the value
     */
    public static double invSqrt(double number, int precision) {
        double x = number;
        double halfX = 0.5d*x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i>>1);
        x = Double.longBitsToDouble(i);
        for(int it = 0; it < 4; it++){
            x = x*(1.5d - halfX*x*x);
        }
        x *= number;
        return x;
    }

    public static float invSqrt(float number) {
        float halfX = 0.5f * number;
        int i = Float.floatToIntBits(number);
        i = 0x5f3759df - (i >> 1);
        number = Float.intBitsToFloat(i);
        number *= (1.5f - halfX * number * number);
        return number;
    }

    public static int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    public static int max(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    // Private function for table lookup
    private static float sinLookup(int a) {
        return a>=0 ? sin[a%(modulus)] : -sin[-a%(modulus)];
    }

    // These are your working functions:
    public static float sin(float a) {
        return sinLookup((int)(a * precision + 0.5f));
    }
    public static float cos(float a) {
        return sinLookup((int)((a+90f) * precision + 0.5f));
    }

    public enum UnknownSide {
        LONG,
        SHORT
    }
}
