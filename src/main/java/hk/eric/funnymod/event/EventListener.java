package hk.eric.funnymod.event;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
    EventPriority priority() default EventPriority.NORMAL;
}
