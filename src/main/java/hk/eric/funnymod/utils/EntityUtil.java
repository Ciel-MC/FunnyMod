package hk.eric.funnymod.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class EntityUtil {

    private static final Predicate<Entity> isHostile = (entity) -> entity instanceof Blaze ||
            entity instanceof Creeper ||
            entity instanceof EnderDragon ||
            entity instanceof Endermite ||
            entity instanceof Evoker ||
            entity instanceof Ghast ||
            entity instanceof Giant ||
            entity instanceof Guardian ||
            entity instanceof Hoglin ||
            entity instanceof Illusioner ||
            entity instanceof Phantom ||
            entity instanceof PiglinBrute ||
            entity instanceof Pillager ||
            entity instanceof Ravager ||
            entity instanceof Shulker ||
            entity instanceof Silverfish ||
            entity instanceof Skeleton ||
            entity instanceof Slime ||
            entity instanceof Spider ||
            entity instanceof Stray ||
            entity instanceof Vex ||
            entity instanceof Witch ||
            entity instanceof WitherSkeleton ||
            entity instanceof Zombie ||
            entity instanceof ZombieHorse;
    private static final Predicate<Entity> isPlayer = (entity) -> entity instanceof Player;
    private static final Predicate<Entity> isPassive = (entity) -> entity instanceof Axolotl ||
            entity instanceof Bat ||
            entity instanceof Cat ||
            entity instanceof Chicken ||
            entity instanceof Cod ||
            entity instanceof Cow ||
            entity instanceof Donkey ||
            entity instanceof Fox ||
            entity instanceof GlowSquid ||
            entity instanceof Horse ||
            entity instanceof Llama ||
            entity instanceof Mule ||
            entity instanceof Ocelot ||
            entity instanceof Parrot ||
            entity instanceof Pig ||
            entity instanceof Rabbit ||
            entity instanceof Salmon ||
            entity instanceof Sheep ||
            entity instanceof Shulker ||
            entity instanceof Turtle ||
            entity instanceof Villager ||
            entity instanceof Wolf ||
            entity instanceof Zombie ||
            entity instanceof ZombieHorse;

    public static final Predicate<Entity> isNeutral = (entity) -> entity instanceof EnderMan || entity instanceof IronGolem || entity instanceof SnowGolem;
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
