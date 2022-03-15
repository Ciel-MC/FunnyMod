package hk.eric.funnymod.utils;

import java.util.stream.Stream;

public class StreamUtil {
    public static < T > Stream<T> checkCast(Stream<?> stream, Class<T> clazz) {
        return stream.filter(clazz::isInstance).map(clazz::cast);
    }
}
