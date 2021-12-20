package hk.eric.funnymod.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ArrayUtil {
    @SafeVarargs
    public static <T> List<T> combine(List<T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            if (!list.isEmpty()) {
                result.addAll(list);
            }
        }
        return result.isEmpty()? null : result;
    }

    @SafeVarargs
    public static <T> Stream<T> combineAndStream(List<T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            if (!list.isEmpty()) {
                result.addAll(list);
            }
        }
        return result.isEmpty()? null : result.stream();
    }
}
