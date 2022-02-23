package hk.eric.funnymod.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

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

    public static double distanceToEntitySquared(Entity entity, Entity target) {
        return MathUtil.getDistance3D(entity.position(), target.position(), false);
    }
}
