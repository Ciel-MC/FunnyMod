package hk.eric.funnymod.event;

public abstract class EventHandler<E extends Event> {

    private final EventPriority priority;

    public EventHandler() {
        this(EventPriority.NORMAL);
    }

    public EventHandler(EventPriority priority) {
        this.priority = priority;
    }

    public abstract void handle(E e);

    public EventPriority getPriority() {
        return priority;
    }
}
