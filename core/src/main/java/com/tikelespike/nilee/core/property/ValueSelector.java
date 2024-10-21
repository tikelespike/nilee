package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.property.events.UpdateSubject;

import java.util.List;
import java.util.Optional;

/**
 * A strategy for selecting a value from a list of values, like always returning the first value, selecting the
 * "highest" or "lowest" value, and so on.
 * <p>
 * Usually, the implementing class should always return the same value for a given list of input values, that is, the
 * returned value should only depend on the input.
 * <p>
 * If the result for a given list of input values does change, the implementing class should notify its observers by
 * calling {@link #update()} on a change. Alternatively, if the result depends on the value of a {@link Property}, that
 * property should be added as a dependency using {@link #addDependency(UpdateSubject)} or by passing it to the
 * constructor. This will ensure the update method is called appropriately.
 *
 * @param <T> the type of the values to select from
 */
public abstract class ValueSelector<T> extends UpdateSubject {

    /**
     * Initializes the value selector with the given property dependencies. The selector will notify its observers when
     * any of the dependencies change. If the selector's selection depends on any non-constant state, that state should
     * either be added to these dependencies if it is described by a property, or the implementing subclass should call
     * {@link #update()} when the value changes.
     *
     * @param dependencies the properties this selector depends on
     */
    protected ValueSelector(Property<?>... dependencies) {
        super(dependencies);
    }

    /**
     * Chooses a value from the given list of values and returns it. The value in the returned optional must be one of
     * the values in the list. The returned optional must be empty if and only if the list is empty.
     * <p>
     * Usually, the implementing class should always return the same value for a given list of input values, that is,
     * the returned value should only depend on the input.
     * <p>
     * If the result for a given list of input values does change, the implementing class should notify its observers by
     * calling {@link #update()} on a change. Alternatively, if the result depends on the value of a {@link Property},
     * that property should be added as a dependency using {@link #addDependency(UpdateSubject)}. This will ensure the
     * update method is called appropriately.
     *
     * @param values the list of values to select from. The returned value must be one of these values.
     *
     * @return an optional containing one of the values from the list, or an empty optional if the list is empty
     */
    public abstract Optional<T> select(List<T> values);
}
