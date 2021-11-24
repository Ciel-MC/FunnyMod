package hk.eric.funnymod.event.events;

import baritone.api.event.events.type.EventState;
import hk.eric.funnymod.event.Event;

public class TickEvent extends Event {

    private final EventState state;

    public TickEvent(EventState state) {
        this.state = state;
    }

    public EventState getState() {
        return state;
    }
}
