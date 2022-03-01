package hk.eric.funnymod.mixin;

import hk.eric.funnymod.utils.accessor.IEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderDispatcher.class)
public class OpenEntityRenderDispatcher implements IEntityRenderDispatcher {
    @Override
    public double renderX() {
        return ((EntityRenderDispatcher) (Object) this).camera.getPosition().x;
    }

    @Override
    public double renderY() {
        return ((EntityRenderDispatcher) (Object) this).camera.getPosition().y;
    }

    @Override
    public double renderZ() {
        return ((EntityRenderDispatcher) (Object) this).camera.getPosition().z;
    }
}