package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.mcqp.MCQPNoGhostHitModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void injectAttack(Player player, Entity entity, CallbackInfo ci) {
        if (MCQPNoGhostHitModule.getToggle().isOn()) {
            if (player instanceof LocalPlayer) {
                ci.cancel();
            }
        }
    }
}
