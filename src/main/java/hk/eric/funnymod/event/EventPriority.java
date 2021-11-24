package hk.eric.funnymod.event;

public enum EventPriority {
    LOWEST(-2),
    LOW(-1),
    NORMAL(0),
    HIGH(1),
    HIGHEST(2),
    MONITOR(5);

    public final int priority;

    EventPriority(final int priority) {
        this.priority = priority;
    }
}
