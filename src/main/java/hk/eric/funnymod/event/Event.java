package hk.eric.funnymod.event;

public class Event{
    public <T extends Event> T call(){
        EventManager.getInstance().callEvent(this);
        return (T) this;
    }
}

enum EventState {
    PRE, POST
}