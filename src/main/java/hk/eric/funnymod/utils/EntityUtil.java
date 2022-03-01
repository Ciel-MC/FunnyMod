package hk.eric.funnymod.utils;

import hk.eric.funnymod.mixin.OpenLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static hk.eric.funnymod.FunnyModClient.mc;

public class EntityUtil {

    private static final Predicate<Entity> isHostile = (entity) -> entity instanceof Monster;
    private static final Predicate<Entity> isPlayer = (entity) -> entity instanceof Player;
    private static final Predicate<Entity> isPassive = (entity) -> entity instanceof Animal;

    public static final Predicate<Entity> isNeutral = (entity) -> entity instanceof NeutralMob;
    public static boolean isHostile(Entity entity) {
        return isHostile.test(entity);
    }
    public static boolean isPlayer(Entity entity) {
        return isPlayer.test(entity);
    }
    public static boolean isPassive(Entity entity) {
        return isPassive.test(entity);
    }
    public static boolean isNeutral(Entity entity) {
        return isNeutral.test(entity);
    }
    public static int rateArmor(LivingEntity entity) {
        return 0; //TODO: Implement
    }

    public static LevelEntityGetter<? extends EntityAccess> getEntityGetter() {
        Level level = mc.level;
        if (level == null) {
            throw new IllegalStateException("No level loaded");
        }
        return ((OpenLevel)mc.level).callGetEntities();
    }

    public static Iterable<? extends EntityAccess> getAllEntities() {
        return getEntityGetter().getAll();
    }

    public static Stream<? extends Entity> streamAllEntities(EntityFilter filter) {
        return filter.process(StreamSupport.stream(getAllEntities().spliterator(), filter.parallel()).filter(Entity.class::isInstance).map(Entity.class::cast));
    }

    public static Entity getClosestEntity(EntityFilter filter) {
        if (mc.player == null) {
            return null;
        }else {
            return streamAllEntities(filter).min(Comparator.comparingDouble(entity -> distanceToEntitySquared(entity, mc.player))).orElse(null);
        }
    }

    public static double distanceToEntitySquared(Entity entity, Entity target) {
        return MathUtil.getDistance3D(entity.position(), target.position(), false);
    }

    public record EntityFilter(boolean sortByDistance,
                               Predicate<Entity> predicate,
                               boolean parallel) {

        public Stream<? extends Entity> process(@NotNull Stream<? extends Entity> stream) {
            Stream<? extends Entity> filtered;
            if (predicate != null) {
                filtered = stream.filter(predicate);
            } else {
                filtered = stream;
            }
            if (sortByDistance && mc.player != null) {
                filtered = filtered.sorted(Comparator.comparingDouble(e -> MathUtil.getDistance3D(e.position(), mc.player.position(), false)));
            }
            return filtered;
        }
    }

    public static class EntityFilterBuilder {
        private boolean sortByDistance = false;
        private Predicate<Entity> predicate = null;
        private boolean parallel = false;

        public EntityFilterBuilder setPredicate(Predicate<Entity> predicate) {
            this.predicate = predicate;
            return this;
        }

        public EntityFilterBuilder setParallel(boolean parallel) {
            this.parallel = parallel;
            return this;
        }

        public EntityFilterBuilder setSortByDistance(boolean sortByDistance) {
            this.sortByDistance = sortByDistance;
            return this;
        }

        public EntityUtil.EntityFilter createEntityFilter() {
            return new EntityUtil.EntityFilter(sortByDistance, predicate, parallel);
        }
    }
}
