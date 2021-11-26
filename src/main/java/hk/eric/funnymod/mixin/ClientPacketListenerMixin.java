package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.combat.VelocityModule;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Redirect(method = "handleSetEntityMotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpMotion(DDD)V"))
    public void injectHandleEntityMotion(Entity entity, double x, double y, double z) {
        if (entity instanceof LocalPlayer) {
            if(VelocityModule.getToggle().isOn()) {
                double horizontalMultiplier = VelocityModule.horizontal.getValue() / 100d;
                double verticalMultiplier = VelocityModule.vertical.getValue() / 100d;
                entity.lerpMotion(x * horizontalMultiplier, y * verticalMultiplier, z * horizontalMultiplier);
                return;
            }
        }
        entity.lerpMotion(x, y, z);
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
    public void injectSend(Packet<?> packet, CallbackInfo ci) {
//        System.out.println("Sending packet: " + packet.getClass().getSimpleName());
    }
}
