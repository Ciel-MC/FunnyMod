package hk.eric.funnymod.mixin;

import hk.eric.funnymod.event.events.ChatReceivedEvent;
import hk.eric.funnymod.event.events.GuiOpenEvent;
import hk.eric.funnymod.modules.combat.VelocityModule;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
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
            if (VelocityModule.getToggle().isOn()) {
                double horizontalMultiplier = VelocityModule.horizontal.getValue() / 100d;
                double verticalMultiplier = VelocityModule.vertical.getValue() / 100d;
                entity.setDeltaMovement(new Vec3(x * horizontalMultiplier, y * verticalMultiplier, z * horizontalMultiplier));
                return;
            }
        }
        entity.setDeltaMovement(x, y, z);
    }

    @Redirect(method = "handleExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    public void redirectHandleExplosion(LocalPlayer player, Vec3 vec3) {
        player.setDeltaMovement(player.getDeltaMovement().add(VelocityModule.processVelocity(vec3.subtract(player.getDeltaMovement()))));
    }

    @Inject(method = "handleOpenScreen", at = @At("HEAD"), cancellable = true)
    public void injectHandleOpenScreen(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
        GuiOpenEvent event = new GuiOpenEvent(packet.getContainerId(), ((OpenClientboundOpenScreenPacket) packet).getType(), packet.getTitle());
        if (event.call().isCancelled()) {
            ci.cancel();
        }else {
            ((OpenClientboundOpenScreenPacket) packet).setContainerId(event.getContainerId());
            ((OpenClientboundOpenScreenPacket) packet).setType(event.getType());
            ((OpenClientboundOpenScreenPacket) packet).setTitle(event.getTitle());
        }
    }

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void injectHandleChat(ClientboundChatPacket packet, CallbackInfo ci) {
        ChatReceivedEvent event = new ChatReceivedEvent(packet.getMessage(), packet.getType(), packet.getSender());
        if (event.call().isCancelled()) {
            ci.cancel();
        }else {
            ((OpenClientboundChatPacket) packet).setMessage(event.getMessage());
            ((OpenClientboundChatPacket) packet).setType(event.getType());
            ((OpenClientboundChatPacket) packet).setSender(event.getSender());
        }
    }
}
