package hk.eric.funnymod.mixin;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.ChatEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.SprintStateEvent;
import baritone.api.event.events.type.EventState;
import baritone.behavior.LookBehavior;
import com.mojang.authlib.GameProfile;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.gui.Gui;
import hk.eric.funnymod.modules.mcqp.MCQPPreventDropModule;
import hk.eric.funnymod.modules.movement.NoSlowModule;
import hk.eric.funnymod.modules.movement.SprintModule;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LocalPlayer.class,priority = 99999)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Shadow public abstract void setSprinting(boolean bl);

    @Shadow public abstract boolean isUsingItem();

    @Shadow @Final private ClientRecipeBook recipeBook;

    @Shadow public Input input;

    @Shadow public abstract void tick();

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void onDrop(boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if(MCQPPreventDropModule.getToggle().isOn()) {
            ItemStack item;
            if((item = this.getInventory().getSelected()) != null) {
                if(item.getMaxStackSize() == 1 || item.getItem() instanceof BookItem) {
                    FunnyModClient.mc.gui.getChat().addMessage(new TextComponent("§7[§bFunnyMod§7] §cA feature has stopped you from dropping this \"rare\" item or tool from your hotbar."));
                    cir.cancel();
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Redirect(method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z")
    )
    public boolean redirectIsUsingItem(LocalPlayer localPlayer) {
        return !NoSlowModule.getToggle().isOn() && localPlayer.isUsingItem();
    }

    @Redirect(method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z")
    )
    public boolean autoSprint(KeyMapping instance) {
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (baritone == null) {
            return SprintModule.getToggle().isOn() || instance.isDown();
        }
        SprintStateEvent event = new SprintStateEvent();
        baritone.getGameEventHandler().onPlayerSprintState(event);
        if (event.getState() != null) {
            return event.getState();
        }
        if (baritone != BaritoneAPI.getProvider().getPrimaryBaritone()) {
            // hitting control shouldn't make all bots sprint
            return false;
        }
        return SprintModule.getToggle().isOn() || instance.isDown();

    }

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;hasForwardImpulse()Z"))
    public boolean onTick(Input instance) {
        return SprintModule.omnidirectional.isOn() && SprintModule.getToggle().isOn()? this.input.getMoveVector() != Vec2.ZERO : instance.hasForwardImpulse();
    }

    /**
     * @author Eric
     * @reason To implement omnidirectional sprinting
     */
    @Overwrite
    private boolean hasEnoughImpulseToStartSprinting() {
        double threshold = .8;
        Vec2 moveVector = this.input.getMoveVector();
        float x = moveVector.x,y = moveVector.y;
        return SprintModule.omnidirectional.isOn() && SprintModule.getToggle().isOn()? x * x + y * y >= threshold * threshold : this.isUnderWater() ? this.input.hasForwardImpulse() : (double)this.input.forwardImpulse >= threshold;
    }

    //Baritone integration
    @Inject(
            method = "chat",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sendChatMessage(String msg, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(msg);
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (baritone == null) {
            return;
        }
        baritone.getGameEventHandler().onSendChatMessage(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/player/LocalPlayer.isPassenger()Z",
                    shift = At.Shift.BY,
                    by = -3
            )
    )
    private void onPreUpdate(CallbackInfo ci) {
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (baritone != null) {
            baritone.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.PRE));
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/player/LocalPlayer.sendPosition()V",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void onPostUpdate(CallbackInfo ci) {
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (baritone != null) {
            baritone.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.POST));
        }
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/world/entity/player/Abilities.mayfly:Z"
            )
    )
    private boolean isAllowFlying(Abilities capabilities) {
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (baritone == null) {
            return capabilities.mayfly;
        }
        return !baritone.getPathingBehavior().isPathing() && capabilities.mayfly;
    }

    @Inject(
            method = "rideTick",
            at = @At(
                    value = "HEAD"
            )
    )
    private void updateRidden(CallbackInfo cb) {
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (baritone != null) {
            ((LookBehavior) baritone.getLookBehavior()).pig();
        }
    }

    @Redirect(method = "clientSideCloseContainer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;closeContainer()V"))
    public void redirectCloseContainer(AbstractClientPlayer instance) {
        if(Gui.getGUI().getGUI().getGUIVisibility().isOn()) {
//            Gui.getGUI().getGUI().getGUIVisibility().toggle();
        }
        super.closeContainer();
    }
}
