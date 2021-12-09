package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.combat.VelocityModule;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Redirect(method = "handleSetEntityMotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpMotion(DDD)V"))
    public void redirectHandleEntityMotion(Entity entity, double x, double y, double z) {
        if (entity instanceof LocalPlayer) {
            if(VelocityModule.getToggle().isOn() && VelocityModule.velocityMode.getValue() == VelocityModule.Mode.MODIFY) {
                double horizontalMultiplier = VelocityModule.horizontal.getValue() / 100d;
                double verticalMultiplier = VelocityModule.vertical.getValue() / 100d;
                entity.lerpMotion(x * horizontalMultiplier, y * verticalMultiplier, z * horizontalMultiplier);
                return;
            }
        }
        entity.lerpMotion(x, y, z);
    }

    @Inject(method = "handleSetEntityMotion", at = @At("HEAD"), cancellable = true)
    public void injectHandleEntityMotion(ClientboundSetEntityMotionPacket clientboundSetEntityMotionPacket, CallbackInfo ci) {
        if(VelocityModule.getToggle().isOn() && VelocityModule.velocityMode.getValue() == VelocityModule.Mode.CANCEL) {
            ci.cancel();
        }
    }
}
