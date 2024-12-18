package fib.br10.utility;

public class Union<T, E> {

    private final T first;
    private final E second;

    private Union(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public static <T, E> Union<T, E> ofFirst(T first) {
        return new Union<>(first, null);
    }

    public static <T, E> Union<T, E> ofSecond(E second) {
        return new Union<>(null, second);
    }

    public boolean isFirst() {
        return first != null;
    }

    public boolean isSecond() {
        return second != null;
    }

    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }

}