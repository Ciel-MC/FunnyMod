package hk.eric.funnymod.event;

import java.util.function.Consumer;

public interface EventHandler<E extends Event> extends Consumer<E> {
    @Override
    @EventListener
    void accept(E e);
}
