package hk.eric.funnymod.mixin;

import hk.eric.funnymod.modules.visual.AnimationModule;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private Entity entity;

    @Shadow
    private float eyeHeightOld;

    @Shadow
    private float eyeHeight;

    /**
     * @author Eric
     * @reason To disable the sneaking animation
     */
    @Overwrite
    public void tick() {
        if (this.entity != null) {
            if (AnimationModule.getToggle().isOn() && AnimationModule.noSmoothSneak.isOn()) {
                this.eyeHeight = this.eyeHeightOld = this.entity.getEyeHeight();
            } else {
                this.eyeHeightOld = this.eyeHeight;
                this.eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * .5f;
            }
        }
    }
}
