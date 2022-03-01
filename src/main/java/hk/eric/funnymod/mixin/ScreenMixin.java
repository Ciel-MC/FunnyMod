package hk.eric.funnymod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import hk.eric.funnymod.event.events.PlayerChatEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    public abstract void render(PoseStack poseStack, int i, int j, float f);

    @Redirect(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;chat(Ljava/lang/String;)V"))
    private void handleChatMessage(LocalPlayer player, String string) {
        if (!new PlayerChatEvent(string).call().isCancelled()) {
            player.chat(string);
        }
    }
}
