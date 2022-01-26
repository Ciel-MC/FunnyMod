package hk.eric.funnymod.mixin;

import hk.eric.funnymod.event.events.PacketEvent;
import hk.eric.funnymod.modules.debug.DebugModule;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    public void injectSendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericFutureListener, CallbackInfo ci) {
        DebugModule.countSend();
        if (new PacketEvent.SendPacketEvent(packet).call().isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void injectChannelRead0(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        DebugModule.countReceive();
        if (new PacketEvent.ReceivePacketEvent(packet).call().isCancelled()) {
            ci.cancel();
        }
    }

}
