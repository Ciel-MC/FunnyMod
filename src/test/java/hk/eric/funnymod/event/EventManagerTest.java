package hk.eric.funnymod.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

    class EventManagerTest {
        @Test
        public void test() {
            EventHandler<Event> handler = new EventHandler<>(EventPriority.HIGH) {
                @Override
                public void handle(Event event) {
                    System.out.println("Hello World!");
                }
            };

            EventHandler<EventA> handlerA = new EventHandler<>(EventPriority.HIGHEST) {
                @Override
                public void handle(EventA event) {
                    System.out.println("EventA");
                }
            };

            EventHandler<EventB> handlerB = new EventHandler<>(EventPriority.LOWEST) {
                @Override
                public void handle(EventB event) {
                    System.out.println("EventB");
                }
            };

            EventManager.getInstance().register(handler);
            EventManager.getInstance().register(handlerA);
            EventManager.getInstance().register(handlerB);

            EventManager.getInstance().callEvent(new EventB());
        }

        private class EventA extends Event {

        }

        private class EventB extends EventA {

        }
    }