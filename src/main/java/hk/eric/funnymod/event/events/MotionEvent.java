package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.Event;
import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.HasState;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class MotionEvent extends Event implements HasState {
    private final MoverType moverType;
    private final Vec3 movement;
    private final EventState state;

    public MotionEvent(MoverType moverType, Vec3 movement, EventState state) {
        this.moverType = moverType;
        this.movement = movement;
        this.state = state;
    }

    @Override
    public EventState getState() {
        return state;
    }

    public MoverType getMoverType() {
        return moverType;
    }

    public Vec3 getMovement() {
        return movement;
    }
}
