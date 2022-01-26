package hk.eric.funnymod.utils.classes.pairs;

public class Pair<T, U> {
    protected T first;
    protected U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {}

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }
}
