package hk.eric.funnymod.mixin;

import hk.eric.funnymod.openedClasses.OpenMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements OpenMinecraft {
    @Mutable
    @Shadow @Final private Timer timer;

    public Timer getTimer() {
        return timer;
    }
}
