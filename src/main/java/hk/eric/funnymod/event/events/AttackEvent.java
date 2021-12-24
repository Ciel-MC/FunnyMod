package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;
import net.minecraft.world.entity.Entity;

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
}
