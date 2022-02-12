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

    private MotionEvent(MoverType moverType, Vec3 movement, EventState state) {
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

    public static class Pre extends MotionEvent {
        public Pre(MoverType moverType, Vec3 movement) {
            super(moverType, movement, EventState.PRE);
        }

        public Pre() {
            super(null, null, EventState.PRE);
        }
    }

    public static class Post extends MotionEvent {
        public Post(MoverType moverType, Vec3 movement) {
            super(moverType, movement, EventState.POST);
        }

        public Post() {
            super(null, null, EventState.POST);
        }
    }
}
