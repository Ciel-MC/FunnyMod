package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.movement.FlightModule;
import hk.eric.funnymod.modules.movement.KeepSprintModule;
import hk.eric.funnymod.modules.player.HeightModule;
import hk.eric.funnymod.modules.world.FreecamModule;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow
    @Final
    public static EntityDimensions STANDING_DIMENSIONS;
    @Shadow
    @Final
    private static Map<Pose, EntityDimensions> POSES;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "attack",
            slice = @Slice(
                    from = @At("HEAD"),
                    to = @At("TAIL")
            ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V")
    )
    public void redirectSetSprinting(Player player, boolean sprinting) {
        player.setSprinting(KeepSprintModule.getToggle().isOn() || sprinting);
    }

    /**
     * @author Eric
     * @reason To Implement HeightModule
     */
    @Overwrite
    public EntityDimensions getDimensions(Pose pose) {
        if (HeightModule.getToggle().isOn()) {
            return switch (pose) {
                case STANDING -> EntityDimensions.scalable(.6F, HeightModule.getHeight(HeightModule.HeightType.NORMAL_HITBOX));
                case CROUCHING -> EntityDimensions.scalable(.6F, HeightModule.getHeight(HeightModule.HeightType.SNEAKING_HITBOX));
                default -> POSES.getOrDefault(pose, STANDING_DIMENSIONS);
            };
        } else {
            return POSES.getOrDefault(pose, STANDING_DIMENSIONS);
        }
    }

    /**
     * @author Eric
     * @reason To Implement HeightModule
     */
    @Overwrite
    public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        switch (pose) {
            case SWIMMING, FALL_FLYING, SPIN_ATTACK -> {
                return 0.4f;
            }
            case CROUCHING -> {
                return HeightModule.getToggle().isOn() ? HeightModule.getHeight(HeightModule.HeightType.SNEAKING_EYE_HEIGHT) : 1.27f;
            }
        }
        return HeightModule.getToggle().isOn() ? HeightModule.getHeight(HeightModule.HeightType.NORMAL_EYE_HEIGHT) : 1.62f;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    public boolean injectTick(Player instance) {
        return instance.isSpectator() || (FlightModule.getToggle().isOn() && FreecamModule.getToggle().isOn());
    }
}
