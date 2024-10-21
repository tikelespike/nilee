package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.property.convenience.FirstValueSelector;
import com.tikelespike.nilee.core.property.events.UpdateEvent;
import com.tikelespike.nilee.core.property.events.UpdateSubject;
import com.tikelespike.nilee.core.property.events.ValueChangeEvent;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Describes a value constructed from a base value and modifiers. The base value is chosen from all base values provided
 * by a set of base value suppliers using a selection strategy (like the first value or the maximum). The modifiers are
 * applied in the order they are added.
 * <p>
 * For example, a property could represent a character's armor class. The base value could be the standard AC calculated
 * as {@code 10 + DEX}. Another way to calculate the base value could be given by armor worn, which may provide the base
 * AC as a constant value like {@code 14}. The modifiers represent a number of temporary bonuses and penalties, such as
 * a +2 bonus from a shield, or a -1 penalty from a spell. The final value is automatically calculated from the selected
 * base value (default is the first one added) and the application of all modifiers.
 *
 * @param <T> the type of the value (typically, an integer or a dice roll like 3d4)
 */
public class Property<T> extends UpdateSubject implements EventListener<UpdateEvent> {

    private final Set<PropertyBaseSupplier<T>> baseValueSuppliers = new LinkedHashSet<>();
    private ValueSelector<T> baseValueSelector = new FirstValueSelector<>();
    private final List<PropertyModifier<T>> modifiers = new ArrayList<>();

    private final EventBus eventBus = new EventBus();

    private T lastKnownValue;

    /**
     * Creates a new property with no base value suppliers or modifiers. You must add at least one base value supplier
     * before calling {@link #getValue()} or {@link #getBaseValue()} by using
     * {@link #addBaseValueSupplier(PropertyBaseSupplier)}.
     */
    protected Property() {
        addUpdateListener(this);
    }

    /**
     * Creates a property with the given default base value supplier.
     *
     * @param baseValueSupplier supplies the default value returned when calling {@link #getBaseValue()}.
     */
    public Property(@NotNull PropertyBaseSupplier<T> baseValueSupplier) {
        this();
        addBaseValueSupplier(Objects.requireNonNull(baseValueSupplier));
    }


    /**
     * Calculates the value of this property by selecting a base value and applying all modifiers. There has to be at
     * least one base value supplier added before calling this method.
     *
     * @return the effective current value of this property
     * @throws IllegalStateException if no base value suppliers have been added before calling this method
     */
    public final T getValue() {
        T value = getBaseValue();
        for (PropertyModifier<T> modifier : getModifiers()) {
            value = modifier.apply(value);
        }
        return value;
    }

    /**
     * Calculates the base value of this property by selecting from the base value suppliers using the base value
     * selection strategy. Base value suppliers are added using {@link #addBaseValueSupplier(PropertyBaseSupplier)}.
     * There has to be at least one base value supplier added before calling this method. The selection strategy can be
     * set using {@link #setBaseValueSelector(ValueSelector)}. This corresponds to the value of the property without any
     * modifiers applied.
     *
     * @return the base value of this property, as selected by the base value selector
     * @throws IllegalStateException if no base value suppliers have been added before calling this method
     */
    public final T getBaseValue() {
        if (getBaseValueSuppliers().isEmpty()) {
            throw new IllegalStateException("No base value suppliers has been defined for this property");
        }
        Optional<T> opt = baseValueSelector.select(
                getBaseValueSuppliers().stream().map(PropertyBaseSupplier::getBaseValue).toList());
        //noinspection OptionalGetWithoutIsPresent - optional may only be empty if the list is empty
        return opt.get();
    }

    /**
     * Retrieves the set of modifiers affecting to this property. A modifier is applied after the base value is
     * calculated, and the modifiers are applied in order. The returned set is a copy of the internal set, so any
     * changes to the returned set will not affect the property.
     *
     * @return a copy of the set of modifiers applied to this property
     */
    public List<PropertyModifier<T>> getModifiers() {
        return new ArrayList<>(modifiers);
    }

    /**
     * Adds a modifier to this property. The modifier will be applied after the base value is calculated, and will
     * affect the end result retrieved by {@link #getValue()}. All modifiers are usually applied in the order they are
     * added this way.
     *
     * @param modifier the modifier to add to this property
     */
    public void addModifier(@NotNull PropertyModifier<T> modifier) {
        addModifier(modifiers.size(), modifier);
    }

    /**
     * Adds a modifier to this property. The modifier will be applied after the base value is calculated, and will
     * affect the end result retrieved by {@link #getValue()}. The modifier will be applied at the given index, so all
     * modifiers with an index greater than or equal to the given index will be applied after the new modifier.
     *
     * @param modifier the modifier to add to this property
     * @param index the index at which to add the modifier. Has to be between 0 and the number of modifiers
     *         already added
     */
    public void addModifier(int index, @NotNull PropertyModifier<T> modifier) {
        Objects.requireNonNull(modifier);
        modifiers.add(index, modifier);
        addDependency(modifier);
    }

    /**
     * Removes a modifier from this property. The modifier will no longer affect the end result retrieved by
     * {@link #getValue()}. If the modifier was not previously added, this method has no effect.
     *
     * @param modifier the modifier to remove from this property
     */
    public void removeModifier(@NotNull PropertyModifier<T> modifier) {
        Objects.requireNonNull(modifier);
        if (!modifiers.contains(modifier)) {
            return;
        }
        modifiers.remove(modifier);
        removeDependency(modifier);
    }

    /**
     * Retrieves the set of base value suppliers used by this property. The base value suppliers are used to calculate
     * the base value of this property, which is then modified by the modifiers. The returned set is a copy of the
     * internal set, so any changes to the returned set will not affect the property. The base value is selected using
     * the base value selector, which can be set using {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @return a copy of the set of base value suppliers used by this property
     */
    public final Set<PropertyBaseSupplier<T>> getBaseValueSuppliers() {
        return new LinkedHashSet<>(baseValueSuppliers);
    }

    /**
     * Adds a base value supplier to this property. The added supplier will provide a base value for the selection
     * strategy to choose from. The selection strategy can be set using {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @param baseValueSupplier the base value supplier to add to this property
     */
    public void addBaseValueSupplier(@NotNull PropertyBaseSupplier<T> baseValueSupplier) {
        Objects.requireNonNull(baseValueSupplier);
        baseValueSuppliers.add(baseValueSupplier);
        addDependency(baseValueSupplier);
    }

    /**
     * Removes a base value supplier from this property. The removed supplier will no longer provide a base value for
     * the selection strategy to choose from. If the supplier was not previously added, this method has no effect.
     *
     * @param baseValueSupplier the base value supplier to remove from this property
     */
    public void removeBaseValueSupplier(@NotNull PropertyBaseSupplier<T> baseValueSupplier) {
        Objects.requireNonNull(baseValueSupplier);
        baseValueSuppliers.remove(baseValueSupplier);
        removeDependency(baseValueSupplier);
    }


    /**
     * Sets the strategy used to select the base value from all base values provided by the base value suppliers.
     * Typical strategies include selecting the first value, the highest value, or the lowest value. The default is
     * {@link FirstValueSelector}.
     *
     * @param baseValueSelector the strategy selecting the base value from all base values provided by the base
     *         value suppliers
     */
    public void setBaseValueSelector(@NotNull ValueSelector<T> baseValueSelector) {
        Objects.requireNonNull(baseValueSelector);
        this.baseValueSelector = baseValueSelector;
        removeDependency(this.baseValueSelector);
        addDependency(baseValueSelector);
    }

    /**
     * Retrieves the strategy used to select the base value from all base values provided by the base value suppliers.
     *
     * @return the strategy used to select the base value from all base values provided by the base value suppliers
     */
    public final ValueSelector<T> getBaseValueSelector() {
        return baseValueSelector;
    }

    /**
     * Registers a listener to be notified when the value of this property changes. Note that the listener will not be
     * called if something about the way this property is calculated changes (such as new modifiers or base value
     * suppliers) if this does not result in a different value.
     * <p>
     * To get notified when something about the property might have changed (like the description of a modifier) even if
     * it does not result in a different value, use {@link #addUpdateListener(EventListener)}.
     *
     * @param listener the listener to register
     *
     * @return a registration object that can be used to unregister the listener
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Registration addValueChangeListener(@NotNull EventListener<ValueChangeEvent<T>> listener) {
        Objects.requireNonNull(listener);
        // requires this class to only send value change events of type T, otherwise, the listeners will also be
        // triggered by value change events of other type, which will cause a class cast exception
        return eventBus.registerListener(ValueChangeEvent.class, (EventListener) listener);
    }

    /**
     * Checks if the value of this property has changed since the last time {@link #notifyListeners()} was called and
     * notifies listeners if necessary.
     */
    private void notifyListeners() {
        T newValue = baseValueSuppliers.isEmpty() ? null : getValue();
        if (!Objects.equals(newValue, lastKnownValue)) {
            eventBus.fireEvent(new ValueChangeEvent<>(lastKnownValue, newValue));
            lastKnownValue = newValue;
        }
    }

    @Override
    public void onEvent(UpdateEvent event) {
        notifyListeners();
    }
}
