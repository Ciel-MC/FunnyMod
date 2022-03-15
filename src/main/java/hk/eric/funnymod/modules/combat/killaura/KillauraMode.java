package hk.eric.funnymod.modules.combat.killaura;

import hk.eric.funnymod.event.events.AttackEvent;
import hk.eric.funnymod.event.events.Render3DEvent;
import hk.eric.funnymod.event.events.TickEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.stream.Stream;

public abstract class KillauraMode {

    /**
     * Processes the stream to filter all the ones that are not fitting according to the mode's configuration.
     * @param entityStream The stream to filter.
     * @return The filtered stream.
     */
    public abstract Stream<? extends LivingEntity> process(Stream<? extends LivingEntity> entityStream);

    /**
     * Attacks the {@code entity} and fires an {@link AttackEvent}.
     * @param entity The entity to attack.
     */
    public abstract void attack(LivingEntity entity);

    /**
     * Render anything you want to render.
     * @param event The event.
     */
    public abstract void render(Render3DEvent event);

    /**
     * For initialization.
     */
    public abstract void tick(TickEvent event);
}
