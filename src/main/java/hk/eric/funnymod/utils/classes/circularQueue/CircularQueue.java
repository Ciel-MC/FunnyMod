package hk.eric.funnymod.utils.classes.circularQueue;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

public class CircularQueue<T> {

    private final ArrayBlockingQueue<T> stack;

    public CircularQueue(int size) {
        this.stack = new ArrayBlockingQueue<>(size);
    }

    public void push(T item) {
        if (stack.remainingCapacity() == 0) {
            stack.remove();
        }
        stack.add(item);
    }

    public Stream<T> asStream() {
        return stack.stream();
    }

    public List<T> asList() {
        return stack.stream().toList();
    }
}
