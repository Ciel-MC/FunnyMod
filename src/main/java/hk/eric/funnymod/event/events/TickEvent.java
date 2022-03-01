package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.Event;
import hk.eric.funnymod.event.HasState;

public class TickEvent extends Event implements HasState {

    private final EventState state;

    public TickEvent(EventState state) {
        this.state = state;
    }

    public static class Pre extends TickEvent {
        public Pre() {
            super(EventState.PRE);
        }
    }

    public static class Post extends TickEvent {
        public Post() {
            super(EventState.POST);
        }
    }

    @Override
    public EventState getState() {
        return state;
    }
}
