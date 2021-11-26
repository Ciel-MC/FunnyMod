package hk.eric.funnymod.event;

import hk.eric.funnymod.event.events.TickEvent;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;

class EventManagerTest {

    EventManager eventManager;

    @BeforeEach
    void setUp() {
        eventManager = EventManager.getInstance();
    }

    @org.junit.jupiter.api.Test
    void test() {
        EventHandler<TickEvent> handler = EventHandler.of(TickEvent.class, event -> {
            event.getState();
        });
        getEventClassOfEventHandler(handler);
    }

    public < E extends Event> void getEventClassOfEventHandler(EventHandler<E> handler) {
        System.out.println(((ParameterizedType) handler.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

}

abstract class EventHandler<E extends Event> {

    public static < T extends Event > EventHandler<T> of(Class<T> clazz, Consumer<T> consumer) {
        return new EventHandler<>() {
            @Override
            public void handle(T e) {
                consumer.accept(e);
            }
        };
    }

    abstract void handle(E e);
}