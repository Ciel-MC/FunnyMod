package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.player.FastplaceModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @ModifyConstant(method = "startUseItem", constant = @Constant(intValue = 4))
    private int injected(int value) {
        return FastplaceModule.getToggle().isOn()? FastplaceModule.delay.getValue() : value;
    }
}
