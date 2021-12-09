package hk.eric.funnymod.mixin;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Vec3.class)
public interface OpenVec3 {
    @Accessor("x")
    @Mutable
    double getX();

    @Accessor("x")
    @Mutable
    void setX(double x);

    @Accessor("z")
    @Mutable
    double getZ();

    @Accessor("z")
    @Mutable
    void setZ(double z);
}
