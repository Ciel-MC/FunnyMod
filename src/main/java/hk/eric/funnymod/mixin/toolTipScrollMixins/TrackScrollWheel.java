package hk.eric.funnymod.mixin.toolTipScrollMixins;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static hk.eric.funnymod.scrollableToolTips.ScrollTracker.ScrollDirection.*;
import static hk.eric.funnymod.scrollableToolTips.ScrollTracker.scroll;

@Mixin(MouseHandler.class)
public class TrackScrollWheel {
    // This will affect *every* use of the mouse wheel and alter the tracker accordingly.
    // Has no impact from a blackbox perspective though since the tooltip position will be reset when selecting an item.
    @Inject(method = "onScroll", at = @At("HEAD"))
    private void trackWheel (long window, double horizontal, double vertical, CallbackInfo info) {
        long mcHandle = Minecraft.getInstance().getWindow().getWindow();
        if (vertical > 0) {
            if (InputConstants.isKeyDown(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				scroll(RIGHT);
			}
            else {
                scroll(DOWN);
            }
        }
        else if (vertical < 0) {
            if (InputConstants.isKeyDown(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				scroll(LEFT);
			}
            else {
                scroll(UP);
            }
        }
    }
}
