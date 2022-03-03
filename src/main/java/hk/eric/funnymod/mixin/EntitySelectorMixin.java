package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.player.FreecamModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin {
    @Inject(method = "pushableBy", at = @At("HEAD"), cancellable = true)
    private static void injectPushableBy(Entity entity, CallbackInfoReturnable<Predicate<Entity>> cir) {
        if (FreecamModule.fakePlayer != null) {
            if (entity == FreecamModule.fakePlayer) {
                cir.setReturnValue(e -> false);
            }
        }
    }
}
