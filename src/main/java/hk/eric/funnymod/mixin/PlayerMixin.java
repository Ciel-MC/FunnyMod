package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.movement.KeepSprintModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "attack",
            slice = @Slice(
                    from = @At("HEAD"),
                    to = @At("TAIL")
            ),
            at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V")
    )
    public void redirectSetSprinting(Player player, boolean sprinting) {
        player.setSprinting(KeepSprintModule.getToggle().isOn()||sprinting);
    }
}
