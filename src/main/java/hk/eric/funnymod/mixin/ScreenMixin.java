package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.Category;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "sendMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    private void handleChatMessage(String string, CallbackInfo ci) {
        if(string.equalsIgnoreCase(";reloadModules")) {
            Category.reloadModules();
            ci.cancel();
        }
    }
}
