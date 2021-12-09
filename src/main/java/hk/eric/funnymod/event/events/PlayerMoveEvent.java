package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.Event;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class PlayerMoveEvent extends Event {
    private final MoverType moverType;
    private final Vec3 movement;

    public PlayerMoveEvent(MoverType moverType, Vec3 movement) {
        this.moverType = moverType;
        this.movement = movement;
    }

    public MoverType getMoverType() {
        return moverType;
    }

    public Vec3 getMovement() {
        return movement;
    }
}
