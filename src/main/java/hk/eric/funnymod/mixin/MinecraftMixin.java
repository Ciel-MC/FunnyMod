package hk.eric.funnymod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftMixin /*extends OpenMinecraft*/ {
    @Accessor("timer")
    @Mutable
    Timer getTimer();

}
