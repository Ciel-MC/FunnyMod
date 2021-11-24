package hk.eric.funnymod.mixin;

import hk.eric.funnymod.openedClasses.OpenTimer;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Timer.class)
public abstract class TimerMixin implements OpenTimer {
    @Mutable
    @Shadow @Final private float msPerTick;

    public float getTicks() {
        return 1000F/msPerTick;
    }
    public void setTicks(float ticks) {
        msPerTick = 1000F/ticks;
    }
}
