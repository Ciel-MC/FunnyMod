package hk.eric.funnymod.event;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Supplier;

public class EventManager {

    private final static EventManager instance = new EventManager();
    private final Map<Class<? extends Event>, List<ListeningMethod>> registry;
    private final Map<Class<? extends Event>, List<EventHandler<?>>> eventHandlers;
    private final Supplier<List<ListeningMethod>> listeningMethodSetSupplier = ArrayList::new;
    private final Supplier<List<EventHandler<?>>> eventHandlerSetSupplier = ArrayList::new;

    private EventManager() {
        registry = new HashMap<>();
        eventHandlers = new HashMap<>();
    }

    public static EventManager getInstance() {
        return instance;
    }

    public void register(Object handler) {
        Class<?> clazz = handler.getClass();
        Map<Class<? extends Event>, List<ListeningMethod>> methods = new HashMap<>();
        Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).forEach(method -> {
            if (method.getParameterCount() != 1) {
                throw new IllegalArgumentException("Method " + method.getName() + " has wrong number of arguments");
            }
            Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
            method.setAccessible(true);

            methods.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(new ListeningMethod(handler, method, method.getAnnotation(EventListener.class).priority()));
        });
        methods.forEach((aClass, listeningMethods) -> registry.computeIfAbsent(aClass, k -> listeningMethodSetSupplier.get()).addAll(listeningMethods));
    }

    public <E extends Event> void register(EventHandler<E> handler) {
        eventHandlers.computeIfAbsent(getEventClassOfEventHandler(handler), k -> eventHandlerSetSupplier.get()).add(handler);
    }

    public void unregister(Object handler) {
        for (Map.Entry<Class<? extends Event>, List<ListeningMethod>> entry : registry.entrySet()) {
            List<ListeningMethod> handlers = entry.getValue();
            handlers.removeIf(listeningMethod -> listeningMethod.object() == handler);
        }
    }

    public void unregister(EventHandler<?> handler) {
        eventHandlers.remove(getEventClassOfEventHandler(handler));
    }

    public void callEvent(Event event) {
        List<ListeningMethod> listeningMethods = registry.getOrDefault(event.getClass(), listeningMethodSetSupplier.get());
        List<EventHandler<?>> eventHandlers = this.eventHandlers.getOrDefault(event.getClass(), eventHandlerSetSupplier.get());

        Class<?> eventClass = event.getClass();
        while (eventClass != Event.class) {
            eventClass = eventClass.getSuperclass();
            listeningMethods.addAll(this.registry.getOrDefault(eventClass, listeningMethodSetSupplier.get()));
            eventHandlers.addAll(this.eventHandlers.getOrDefault(eventClass, eventHandlerSetSupplier.get()));
        }

        new ArrayList<>(listeningMethods).stream().sorted(Comparator.comparing(ListeningMethod::getPriority)).forEach(listeningMethod -> {
            try {
                listeningMethod.method().invoke(listeningMethod.object(), event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        new ArrayList<>(eventHandlers).stream().sorted(Comparator.comparing(EventHandler::getPriority)).forEach(eventHandler -> callEventToEventHandler(eventHandler, event));
    }

    private <E extends Event> void callEventToEventHandler(EventHandler<E> handler, Event event) {
        handler.handle((E) event);
    }

    public < E extends Event > Class<E> getEventClassOfEventHandler(EventHandler<E> handler) {
        return (Class<E>) ((ParameterizedType) handler.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
