package hk.eric.funnymod.mixin;

import hk.eric.funnymod.openedClasses.OpenMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements OpenMinecraft {
    @Mutable
    @Shadow @Final private Timer timer;

    public Timer getTimer() {
        return timer;
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    public void injectSetScreen(Screen screen, CallbackInfo ci) {

    }
}
