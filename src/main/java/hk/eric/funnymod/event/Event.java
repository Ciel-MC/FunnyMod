package hk.eric.funnymod.event;

public class Event{
    public Event call(){
        EventManager.getInstance().callEvent(this);
        return this;
    }
}