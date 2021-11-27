package hk.eric.funnymod.event;

import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class EventManager {

    /*Potential to have mapped events called by strings*/

    private final static EventManager instance;
    private final Map<Class<? extends Event>, Set<ListeningMethod>> registry;

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
        Map<Class<? extends Event>, List<ListeningMethod>> methods = new HashMap<>();
        Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).forEach(method -> {
            if (method.getParameterCount() != 1) {
                throw new IllegalArgumentException("Method " + method.getName() + " has wrong number of arguments");
            }
            Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
            method.setAccessible(true);

            methods.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(new ListeningMethod(handler, method));
        });
        methods.forEach((aClass, listeningMethods) -> {
            registry.computeIfAbsent(aClass, k -> new HashSet<>()).addAll(listeningMethods);
        });
    }

    public <E extends Event> void register(EventHandler<E> handler) {
        Class<E> eventClass = getEventClassOfEventHandler(handler);
        try {
            Method method = handler.getClass().getMethod("handle", eventClass);
            method.setAccessible(true);
//            System.out.println("Methods: ");
//            for (Method declaredMethod : handler.getClass().getDeclaredMethods()) {
//                System.out.println(declaredMethod.getName() + " " + Arrays.toString(declaredMethod.getParameterTypes()));
//            }
            registry.computeIfAbsent(eventClass, k -> new HashSet<>()).add(new ListeningMethod(handler, method));
        } catch (NoSuchMethodException e) {
            LogManager.getLogger().error("Failed to register event handler " + handler.getClass().getSimpleName() + ": No method handle(" + eventClass.getSimpleName() + ") found");
        }
    }

    public void unregister(Object handler) {
        for (Map.Entry<Class<? extends Event>, Set<ListeningMethod>> entry : registry.entrySet()) {
            Set<ListeningMethod> handlers = entry.getValue();
            handlers.removeIf(listeningMethod -> listeningMethod.object() == handler);
        }
    }

    public void callEvent(Event event) {
        Set<ListeningMethod> handlers = registry.get(event.getClass());
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

    public < E extends Event > Class<E> getEventClassOfEventHandler(EventHandler<E> handler) {
        return (Class<E>) ((ParameterizedType) handler.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
