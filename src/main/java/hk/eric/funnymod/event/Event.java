package hk.eric.funnymod.event;

public class Event{
    public void call(){
        EventManager.getInstance().callEvent(this);
    }
}

enum EventState {
    PRE, POST
}