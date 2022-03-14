package hk.eric.funnymod.utils.classes;

import net.minecraft.world.phys.Vec3;

public class PosRot {
    
    private double x,y,z;
    private float yaw, pitch;
    private boolean hasPos = false, hasRot = false;

    public PosRot(Vec3 vec3, XYRot xyRot) {
        this(vec3.x(), vec3.y(), vec3.z(), xyRot);
    }

    public PosRot(double x, double y, double z, XYRot xyRot) {
        this(x, y, z, xyRot.getYaw(), xyRot.getPitch());
    }

    public PosRot(Vec3 vec3, float yaw, float pitch) {
        this(vec3.x(), vec3.y(), vec3.z(), yaw, pitch);
    }

    public PosRot(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        hasPos = true;
        hasRot = true;
    }

    public PosRot(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        hasPos = true;
    }

    public PosRot(Vec3 vec3) {
        this(vec3.x(), vec3.y(), vec3.z());
    }

    public PosRot(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        hasRot = true;
    }

    public PosRot(XYRot xyRot) {
        this(xyRot.getYaw(), xyRot.getPitch());
    }

    public PosRot() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vec3 getVec3() {
        return new Vec3(x,y,z);
    }

    public void setVec3(Vec3 vec3) {
        this.x = vec3.x();
        this.y = vec3.y();
        this.z = vec3.z();
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public XYRot getXYRot() {
        return new XYRot(yaw, pitch);
    }

    public void setRot(XYRot xyRot) {
        this.yaw = xyRot.getYaw();
        this.pitch = xyRot.getPitch();
    }

    public boolean hasPos() {
        return hasPos;
    }

    public boolean hasRot() {
        return hasRot;
    }
}
