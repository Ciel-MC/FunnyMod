package hk.eric.funnymod.event;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EventListener {
    EventPriority priority() default EventPriority.NORMAL;
}
