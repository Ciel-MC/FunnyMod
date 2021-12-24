package hk.eric.funnymod.utils.classes;

public record ThreeDimensionalLine(double x1, double y1, double z1, double x2, double y2, double z2) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ThreeDimensionalLine cmp) {
            return cmp.x1 == x1 && cmp.y1 == y1 && cmp.z1 == z1 && cmp.x2 == x2 && cmp.y2 == y2 && cmp.z2 == z2 ||
                    cmp.x1 == x2 && cmp.y1 == y2 && cmp.z1 == z2 && cmp.x2 == x1 && cmp.y2 == y1 && cmp.z2 == z1;
        }
        return false;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getZ1() {
        return z1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double getZ2() {
        return z2;
    }
}
