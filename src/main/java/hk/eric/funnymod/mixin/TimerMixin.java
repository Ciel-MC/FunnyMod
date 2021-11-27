package hk.eric.funnymod.mixin;

import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Timer.class)
public interface TimerMixin {
    @Accessor("msPerTick")
    @Mutable
    float getMsPerTick();

    @Accessor("msPerTick")
    @Mutable
    void setMsPerTick(float msPerTick);
}
