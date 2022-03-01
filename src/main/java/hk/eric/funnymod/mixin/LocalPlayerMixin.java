package hk.eric.funnymod.mixin;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.ChatEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.SprintStateEvent;
import com.mojang.authlib.GameProfile;
import hk.eric.funnymod.chat.ChatManager;
import hk.eric.funnymod.event.events.MotionEvent;
import hk.eric.funnymod.modules.mcqp.MCQPPreventDropModule;
import hk.eric.funnymod.modules.movement.NoSlowModule;
import hk.eric.funnymod.modules.movement.SprintModule;
import hk.eric.funnymod.modules.player.NoFallModule;
import hk.eric.funnymod.utils.MathUtil;
import hk.eric.funnymod.utils.classes.Result;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
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

@Mixin(value = LocalPlayer.class, priority = 99999)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow
    public Input input;
    @Shadow
    @Final
    private ClientRecipeBook recipeBook;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Shadow
    public abstract void setSprinting(boolean bl);

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract void tick();

    @Override
    public float getSpeed() {
        return SprintModule.getToggle().isOn() ? (float) (super.getSpeed() * SprintModule.speed.getNumber()) : super.getSpeed();
    }

    @Override
    public boolean isOnGround() {
        Result<Boolean> result;
        if ((result = NoFallModule.shouldGround.get()).isChanged()) {
            return result.get();
        }else{
            return this.onGround;
        }
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void onDrop(boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (MCQPPreventDropModule.getToggle().isOn()) {
            if (this.getInventory().getSelected() != null) {
                ChatManager.sendMessage(Component.text("A feature has stopped you from dropping your hotbar.").color(TextColor.color(0x00FFFF)));
                cir.cancel();
                cir.setReturnValue(false);
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
    public boolean redirectHasForwardImpulse(Input instance) {
        return SprintModule.omnidirectional.isOn() && SprintModule.getToggle().isOn() ? MathUtil.compare_force(instance.getMoveVector(), 1.0E-5) == 1 : instance.hasForwardImpulse();
    }

    /**
     * @author Eric
     * @reason To implement omnidirectional sprinting
     */
    @Overwrite
    private boolean hasEnoughImpulseToStartSprinting() {
        double threshold = .8;
        Vec2 moveVector = this.input.getMoveVector();
        return SprintModule.omnidirectional.isOn() && SprintModule.getToggle().isOn() ? (this.isUnderWater() ? input.hasForwardImpulse() : MathUtil.compare_force(moveVector, .8) == 1) : (this.isUnderWater() ? this.input.hasForwardImpulse() : (double) this.input.forwardImpulse >= threshold);
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
        IBaritone b = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (b != null) {
            b.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(baritone.api.event.events.type.EventState.PRE));
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
        IBaritone b = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (b != null) {
            b.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(baritone.api.event.events.type.EventState.POST));
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

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void hookMoveStart(CallbackInfo ci) {
        new MotionEvent.Pre().call();
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void hookMoveEnd(CallbackInfo ci) {
        new MotionEvent.Post().call();
    }
}
