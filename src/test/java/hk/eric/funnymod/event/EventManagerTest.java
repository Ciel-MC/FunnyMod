package hk.eric.funnymod.event;

import hk.eric.funnymod.event.events.TickEvent;
import org.junit.jupiter.api.Test;

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

        @Test
        void test1() throws InterruptedException {

            EventHandler<TickEvent.Pre> tickEventPreHandler = new EventHandler<>() {
                @Override
                public void handle(TickEvent.Pre event) {
                    System.out.println("TickEvent.Pre");
                }
            };

            EventHandler<TickEvent.Post> tickEventPostHandler = new EventHandler<>() {
                @Override
                public void handle(TickEvent.Post event) {
                    System.out.println("TickEvent.Post");
                }
            };

            EventHandler<TickEvent> tickEventHandler = new EventHandler<>() {
                @Override
                public void handle(TickEvent event) {
                    System.out.println("TickEvent: " + event.getState());
                }
            };



            EventManager.getInstance().register(tickEventPreHandler);
            EventManager.getInstance().register(tickEventPostHandler);
            EventManager.getInstance().register(tickEventHandler);

            EventManager.getInstance().callEvent(new TickEvent.Pre());
            EventManager.getInstance().callEvent(new TickEvent.Post());
        }

        private class EventA extends Event {

        }

        private class EventB extends EventA {

        }
    }