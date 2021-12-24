package hk.eric.funnymod.event.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RemoteAttackEvent extends AttackEvent {

    private final Vec3 location;

    public RemoteAttackEvent(Entity target, Vec3 location) {
        super(target, true);
        this.location = location;
    }

    public Vec3 getLocation() {
        return location;
    }
}
