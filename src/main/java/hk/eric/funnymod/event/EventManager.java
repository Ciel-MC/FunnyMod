package hk.eric.funnymod.event;

import java.lang.reflect.Method;
import java.util.*;

public class EventManager {

    /*Potential to have mapped events called by strings*/

    private final static EventManager instance;
    private final Map<Class<? extends Event>, List<ListeningMethod>> registry;

    static {
        instance = new EventManager();
    }

    private EventManager() {
        registry = new HashMap<>();
    }

    public static EventManager getInstance() {
        return instance;
    }

    public void register(Object handler) {
        Class<?> clazz = handler.getClass();
        Map<Class<? extends Event>,List<ListeningMethod>> methods = new HashMap<>();
        Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).forEach(method -> {
            if(method.getParameterCount() != 1) {
                throw new IllegalArgumentException("Method " + method.getName() + " has wrong number of arguments");
            }
            Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
            method.setAccessible(true);

            methods.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(new ListeningMethod(handler, method));
        });
        methods.forEach((aClass, listeningMethods) -> {
            registry.computeIfAbsent(aClass, k -> new ArrayList<>()).addAll(listeningMethods);
        });
    }

    public void unregister(Object handler) {
        for (Map.Entry<Class<? extends Event>, List<ListeningMethod>> entry : registry.entrySet()) {
            List<ListeningMethod> handlers = entry.getValue();
            handlers.removeIf(listeningMethod -> listeningMethod.object() == handler);
        }
    }

    public void callEvent(Event event) {
        List<ListeningMethod> handlers = registry.get(event.getClass());
        if(handlers != null) {
            handlers.forEach(listeningMethod -> {
                try {
                    listeningMethod.method().invoke(listeningMethod.object(), event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
