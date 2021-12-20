package hk.eric.funnymod.mixin.toolTipScrollMixins;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

import static hk.eric.funnymod.scrollableToolTips.ScrollTracker.*;
import static hk.eric.funnymod.scrollableToolTips.ScrollTracker.ScrollDirection.*;

@Mixin(Screen.class)
public class AlterPosition {
	// Reset the tracker whenever a GUI window closes.
	@Inject (method = "onClose()V", at = @At("HEAD"))
	public void resetTrackerOnScreenClose (CallbackInfo info) {
		reset();
	}

	// There are two different functions for renderTooltip, one with a list and one without, so just catch both and format the data as List regardless.
	@Inject (method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"))
	public void updateTracker (PoseStack poseStack, List<Component> lines, Optional<TooltipComponent> optionalTooltipComponent, int x, int y, CallbackInfo info) {
		setItem(lines);
	}

	@Inject (method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;II)V", at = @At("HEAD"))
	public void updateTracker (PoseStack matrices, Component text, int x, int y, CallbackInfo info) {
		List<Component> asList = List.of(text);
		setItem(asList);
	}

	@Inject (method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"))
	public void applyTracker (PoseStack matrices, List<? extends FormattedCharSequence> lines, int x, int y, CallbackInfo info) {
		long mcHandle = Minecraft.getInstance().getWindow().getWindow();
		if (InputConstants.isKeyDown(mcHandle, GLFW.GLFW_KEY_PAGE_UP)) { // Some use cases (such as creative inventory) might require button press instead of scroll wheel, so keep this here.
			if (InputConstants.isKeyDown(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				scroll(LEFT);
			}
			else {
				scroll(UP);
			}
		}
		else if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_PAGE_DOWN)) {
			if (InputConstants.isKeyDown(mcHandle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
				scroll(RIGHT);
			}
			else {
				scroll(DOWN);
			}
		}
	}

	// Using an invoke inject here because the tooltip coordinates get checked for out of bounds positions. I want the scroll offset to only apply after the bound check.
	// Targetting a method invoke because it was just conveniently placed after the bound check.

	// l is the variable that determines y-axis position of a tooltip.
	@ModifyVariable (method = "renderTooltipInternal", ordinal = 5, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.BEFORE))
	private int modifyYAxis (int y) {
		return y + getOffset(Offset.Y);
	}

	// k is the variable that determines x-axis position of a tooltip.
	@ModifyVariable (method = "renderTooltipInternal", ordinal = 4, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.BEFORE))
	private int modifyXAxis (int x) {
		return x + getOffset(Offset.X);
	}
}
