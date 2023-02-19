package com.tikelespike.nilee.util;

import java.util.Objects;

/**
 * A simple pair of objects.
 *
 * @param <S> the type of the first object
 * @param <T> the type of the second object
 *           
 * @author Timo Weberruß
 */
public class Pair<S, T> {

    private final S first;
    private final T second;

    /**
     * Creates a new pair of two objects.
     *
     * @param first  the first object
     * @param second the second object
     */
    public Pair(S first, T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first object.
     *
     * @return the first object of the pair
     */
    public S getFirst() {
        return first;
    }

    /**
     * Returns the second object.
     *
     * @return the second object of the pair
     */
    public T getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Pair{" +
            "first=" + first +
            ", second=" + second +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
