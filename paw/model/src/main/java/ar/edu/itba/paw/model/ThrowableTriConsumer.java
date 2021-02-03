package ar.edu.itba.paw.model;

/**
 * Represents a function that consumes 3 arguments, returns void and could throw and exception.
 * @param <X> The first argument of the function.
 * @param <Y> The second argument of the function.
 * @param <Z> The third argument of the function.
 * @param <T> The exception that could be thrown.
 */
@FunctionalInterface
public interface ThrowableTriConsumer<X, Y, Z, T extends Throwable> {

    void apply(X x, Y y, Z z) throws T;
}
