package hk.eric.funnymod.mixin;

import hk.eric.funnymod.event.events.AttackEvent;
import hk.eric.funnymod.modules.exploit.BowInstantKillModule;
import hk.eric.funnymod.utils.PacketUtil;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static hk.eric.funnymod.FunnyModClient.mc;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"),
            cancellable = true
    )
    public void injectAttack(Player player, Entity entity, CallbackInfo ci) {
        if (new AttackEvent(entity).call().isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"releaseUsingItem"}
    )
    private void onReleaseUsingItem(Player player, CallbackInfo ci) {
        if (BowInstantKillModule.getToggle().isOn() && player.getInventory().getSelected().getItem().equals(Items.BOW)) {
            PacketUtil.sendPacket(PacketUtil.createPlayerCommand(ServerboundPlayerCommandPacket.Action.START_SPRINTING));
            for(int i = 0; i < BowInstantKillModule.cycles.getValue(); ++i) {
                PacketUtil.sendPacket(PacketUtil.createPos(mc.player.getX(), mc.player.getY() - 1.0E-9D, mc.player.getZ(), true));
                PacketUtil.sendPacket(PacketUtil.createPos(mc.player.getX(), mc.player.getY() + 1.0E-9D, mc.player.getZ(), false));
            }
            PacketUtil.sendPacket(PacketUtil.createPlayerCommand(ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
        }
    }
}
