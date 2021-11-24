package hk.eric.funnymod.openedClasses;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;

public interface OpenLevel {
    LevelEntityGetter<Entity> getEntities();
}
