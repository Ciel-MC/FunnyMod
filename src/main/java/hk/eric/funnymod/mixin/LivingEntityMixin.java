package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.movement.SprintModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract void tick();

    @Shadow public abstract AttributeMap getAttributes();

    /**
     * @author Eric
     */
    @Overwrite
    public double getAttributeValue(Attribute attribute) {
        if(attribute == Attributes.MOVEMENT_SPEED) {
            if((Object)this instanceof LocalPlayer localPlayer) {
                if(SprintModule.getToggle().isOn()) {
                    return localPlayer.getAttributes().getValue(attribute) * SprintModule.speed.getValue();
                }
            }
        }
        return this.getAttributes().getValue(attribute);
    }
}
