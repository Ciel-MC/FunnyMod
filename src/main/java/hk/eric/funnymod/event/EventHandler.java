package hk.eric.funnymod.event;

public abstract class EventHandler<E extends Event> {
    public abstract void handle(E e);
}
