package hk.eric.funnymod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import hk.eric.funnymod.event.events.Render3DEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void injectRenderLevelHead(PoseStack poseStack, float partialTicks, long finishTimeNano, boolean drawBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightMapIn, Matrix4f projectionIn, CallbackInfo ci) {
        new Render3DEvent.Pre(poseStack, partialTicks, projectionIn).call();
    }

    @Inject(
            method = "renderLevel",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void injectRenderLevelReturn(PoseStack poseStack, float partialTicks, long finishTimeNano, boolean drawBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightMapIn, Matrix4f projectionIn, CallbackInfo ci) {
        new Render3DEvent.Post(poseStack, partialTicks, projectionIn).call();
    }

}
