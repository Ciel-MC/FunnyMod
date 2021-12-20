package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.movement.AntiVineModule;
import hk.eric.funnymod.modules.player.NoJumpDelayModule;
import hk.eric.funnymod.modules.visual.EspModule;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow private int noJumpDelay;

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    public void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (EspModule.getToggle().isOn() && EspModule.ESP_MODE.getValue() == EspModule.EspMode.GLOWING && EspModule.shouldGlow.test((LivingEntity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "aiStep",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isControlledByLocalInstance()Z", shift = At.Shift.AFTER),
                    to = @At("TAIL")
            ),
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;noJumpDelay:I", opcode = Opcodes.GETFIELD))
    public int redirectNoJumpDelay(LivingEntity livingEntity) {
        if (NoJumpDelayModule.getToggle().isOn()) {
            return 0;
        }else {
            return this.noJumpDelay;
        }
    }

    @Redirect(method = "onClimbable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/Tag;)Z"))
    public boolean redirectOnClimbable(BlockState blockState, Tag tag) {
        if (AntiVineModule.getToggle().isOn()) {
            if (tag == BlockTags.CLIMBABLE && blockState.getBlock().equals(Blocks.VINE)) {
                return false;
            }
        }
        return blockState.is(tag);
    }
}
