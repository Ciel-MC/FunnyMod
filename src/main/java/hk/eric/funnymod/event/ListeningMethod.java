package hk.eric.funnymod.event;

import java.lang.reflect.Method;

public record ListeningMethod(Object object, Method method, EventPriority priority){}
