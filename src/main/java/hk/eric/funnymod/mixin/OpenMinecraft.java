package hk.eric.funnymod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface OpenMinecraft /*extends OpenMinecraft*/ {
    @Accessor("timer")
    @Mutable
    Timer getTimer();

    @Invoker("startAttack")
    void invokeStartAttack();

    @Invoker("startUseItem")
    void invokeStartUseItem();

}
