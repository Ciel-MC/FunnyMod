package hk.eric.funnymod.event;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventHandler<?> that = (EventHandler<?>) o;
        return priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority);
    }
}
