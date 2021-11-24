package hk.eric.funnymod.mixin;

import hk.eric.funnymod.openedClasses.OpenLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public abstract class LevelMixin implements OpenLevel {

}
