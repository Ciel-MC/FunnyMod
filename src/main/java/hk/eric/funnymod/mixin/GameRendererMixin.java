package hk.eric.funnymod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import hk.eric.funnymod.FunnyModClient;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void injectRenderLevel(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        FunnyModClient.setPoseStack(poseStack);
    }
}
