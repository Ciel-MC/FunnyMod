package hk.eric.funnymod.event;

import java.util.function.Consumer;

public interface EventHandler<E extends Event> {
    @EventListener
    void handle(E e);
}
