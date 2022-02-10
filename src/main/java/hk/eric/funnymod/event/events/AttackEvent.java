package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class AttackEvent extends EventCancellable {
    
    private final Entity target;
    private final boolean remote;

    public AttackEvent(Entity target) {
        this(target, false);
    }

    public AttackEvent(Entity target, boolean remote) {
        this.target = target;
        this.remote = remote;
    }

    public Entity getTarget() {
        return target;
    }

    public boolean isRemote() {
        return remote;
    }

    public static class RemoteAttackEvent extends AttackEvent {

        private final Vec3 location;

        public RemoteAttackEvent(Entity target, Vec3 location) {
            super(target, true);
            this.location = location;
        }

        public Vec3 getLocation() {
            return location;
        }
    }
}
