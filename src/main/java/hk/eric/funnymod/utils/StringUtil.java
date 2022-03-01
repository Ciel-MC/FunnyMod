package hk.eric.funnymod.utils;

import java.util.Queue;

public class StringUtil {
    public static String getString(Queue<String> queue) {
        return queue.isEmpty() ? "" : queue.poll();
    }
}
