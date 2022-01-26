package hk.eric.funnymod.utils.classes;

public class Result<T> {

    private final T value;
    private final boolean isChanged;

    public static < T > Result<T> unchanged() {
        return new UnchangedResult<>();
    }

    public static < T > Result<T> of(T value) {
        return new Result<>(value, true);
    }

    private Result(T value, boolean isChanged) {
        this.value = value;
        this.isChanged = isChanged;
    }

    public T get() {
        return value;
    }

    public boolean isChanged() {
        return isChanged;
    }

    private static class UnchangedResult<T> extends Result<T> {
        private UnchangedResult() {
            super(null, false);
        }

        @Override
        public T get() {
            throw new IllegalStateException("Can't get value of unchanged result");
        }
    }
}
