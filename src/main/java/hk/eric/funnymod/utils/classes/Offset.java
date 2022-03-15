package hk.eric.funnymod.utils.classes;

import net.minecraft.core.BlockPos;

import java.util.Objects;

public class Offset {

    private int xOffset, yOffset, zOffset;

    public static Offset getOffset(BlockPos pos1, BlockPos pos2) {
        return new Offset(pos1.getX() - pos2.getX(), pos1.getY() - pos2.getY(), pos1.getZ() - pos2.getZ());
    }

    public Offset(int x, int y, int z) {
        xOffset = x;
        yOffset = y;
        zOffset = z;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public int getZOffset() {
        return zOffset;
    }

    public void setXOffset(int x) {
        xOffset = x;
    }

    public void setYOffset(int y) {
        yOffset = y;
    }

    public void setZOffset(int z) {
        zOffset = z;
    }

    public void setOffset(int x, int y, int z) {
        xOffset = x;
        yOffset = y;
        zOffset = z;
    }

    public boolean isZero() {
        return xOffset == 0 && yOffset == 0 && zOffset == 0;
    }

    public boolean isZeroX() {
        return xOffset == 0;
    }

    public boolean isZeroY() {
        return yOffset == 0;
    }

    public boolean isZeroZ() {
        return zOffset == 0;
    }

    public boolean isPositiveX() {
        return xOffset > 0;
    }

    public boolean isPositiveY() {
        return yOffset > 0;
    }

    public boolean isPositiveZ() {
        return zOffset > 0;
    }

    public boolean isNegativeX() {
        return xOffset < 0;
    }

    public boolean isNegativeY() {
        return yOffset < 0;
    }

    public boolean isNegativeZ() {
        return zOffset < 0;
    }

    public boolean isPositive() {
        return xOffset > 0 || yOffset > 0 || zOffset > 0;
    }

    public boolean isNegative() {
        return xOffset < 0 || yOffset < 0 || zOffset < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offset offset = (Offset) o;
        return xOffset == offset.xOffset && yOffset == offset.yOffset && zOffset == offset.zOffset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xOffset, yOffset, zOffset);
    }

    @Override
    public String toString() {
        return "Offset{" +
                "xOffset=" + xOffset +
                ", yOffset=" + yOffset +
                ", zOffset=" + zOffset +
                '}';
    }
}
