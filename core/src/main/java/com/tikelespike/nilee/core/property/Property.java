package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.property.convenience.FirstValueSelector;
import com.tikelespike.nilee.core.property.events.UpdateEvent;
import com.tikelespike.nilee.core.property.events.ValueChangeEvent;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Describes a value constructed from a base value and modifiers. The base value is chosen from all base values provided
 * by a set of base value suppliers using a selection strategy (like the first value or the maximum). The modifiers are
 * applied in the order they are added.
 * <p>
 * For example, a property could represent a character's armor class. The base value could be the standard AC calculated
 * as {@code 10 + DEX}. Another way to calculate the base value could be given by armor worn, which may calculate the
 * base AC as something like {@code 12 + DEX}. The modifiers represent a number of temporary bonuses and penalties, such
 * as a +2 bonus from a shield, or a -1 penalty from a spell. The final value is automatically calculated from the
 * selected base value (default is the first one added) and the application of all modifiers.
 *
 * @param <T> the type of the value (typically, an integer or a dice roll like 3d4)
 */
public class Property<T> implements EventListener<UpdateEvent> {

    private final Set<PropertyBaseSupplier<T>> baseValueSuppliers = new LinkedHashSet<>();

    private final List<PropertyModifier<T>> modifiers = new ArrayList<>();

    private ValueSelector<T> baseValueSelector = new FirstValueSelector<>();

    private final Map<PropertyModifier<T>, Registration> modifierRegistrations = new HashMap<>();

    private final Map<PropertyBaseSupplier<T>, Registration> baseRegistrations = new HashMap<>();

    private Registration baseSelectorRegistration = Registration.getInvalid();

    private final EventBus eventBus = new EventBus();

    private T lastKnownValue;

    /**
     * Creates a new property with no base value suppliers or modifiers. You must add at least one base value supplier
     * before calling {@link #getValue()} or {@link #getBaseValue()} by using
     * {@link #addBaseValueSupplier(PropertyBaseSupplier)}.
     */
    protected Property() {

    }

    /**
     * Creates a property with the given default base value supplier.
     *
     * @param baseValueSupplier supplies the default value returned when calling {@link #getBaseValue()}.
     */
    public Property(@NotNull PropertyBaseSupplier<T> baseValueSupplier) {
        addBaseValueSupplier(Objects.requireNonNull(baseValueSupplier));
    }


    /**
     * Calculates the value of this property by selecting a base value and applying all modifiers. There has to be at
     * least one base value supplier added before calling this method.
     *
     * @return the effective value of this property
     * @throws IllegalStateException if no base value suppliers have been added before calling this method
     */
    public T getValue() {
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
    public T getBaseValue() {
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
     * affect the end result retrieved by {@link #getValue()}. All modifiers are applied in the order they are added.
     * <p>
     * Calling this method will trigger a value change event, as will any update events sent by the modifier.
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
     * <p>
     * Calling this method will trigger a value change event, as will any update events sent by the modifier.
     *
     * @param modifier the modifier to add to this property
     * @param index the index at which to add the modifier. Has to be between 0 and the number of modifiers
     *         already added
     */
    public void addModifier(int index, @NotNull PropertyModifier<T> modifier) {
        Objects.requireNonNull(modifier);
        Registration registration = modifier.addUpdateListener(this);
        modifiers.add(index, modifier);
        modifierRegistrations.put(modifier, registration);
        notifyListeners();
    }

    /**
     * Removes a modifier from this property. The modifier will no longer affect the end result retrieved by
     * {@link #getValue()}. If the modifier was not previously added, this method has no effect.
     * <p>
     * Calling this method will trigger a value change event.
     *
     * @param modifier the modifier to remove from this property
     */
    public void removeModifier(@NotNull PropertyModifier<T> modifier) {
        Objects.requireNonNull(modifier);
        if (!modifiers.contains(modifier)) {
            return;
        }
        modifiers.remove(modifier);
        modifierRegistrations.get(modifier).unregisterAll();
        modifierRegistrations.remove(modifier);
        notifyListeners();
    }

    /**
     * Removes all modifiers from this property. The property will no longer be affected by any modifiers. Calling this
     * method will trigger a value change event.
     */
    public void clearModifiers() {
        modifiers.clear();
        modifierRegistrations.values().forEach(Registration::unregisterAll);
        modifierRegistrations.clear();
        notifyListeners();
    }


    /**
     * Retrieves the set of base value suppliers used by this property. The base value suppliers are used to calculate
     * the base value of this property, which is then modified by the modifiers. The returned set is a copy of the
     * internal set, so any changes to the returned set will not affect the property. The base value is selected using
     * the base value selector, which can be set using {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @return a copy of the set of base value suppliers used by this property
     */
    public Set<PropertyBaseSupplier<T>> getBaseValueSuppliers() {
        return new LinkedHashSet<>(baseValueSuppliers);
    }

    /**
     * Adds a base value supplier to this property. The added supplier will provide a base value for the selection
     * strategy to choose from. The selection strategy can be set using {@link #setBaseValueSelector(ValueSelector)}.
     * <p>
     * Calling this method will trigger a value change event, as will any update events sent by the base value
     * supplier.
     *
     * @param baseValueSupplier the base value supplier to add to this property
     */
    public void addBaseValueSupplier(@NotNull PropertyBaseSupplier<T> baseValueSupplier) {
        Objects.requireNonNull(baseValueSupplier);
        Registration registration = baseValueSupplier.addUpdateListener(this);
        baseValueSuppliers.add(baseValueSupplier);
        baseRegistrations.put(baseValueSupplier, registration);
        notifyListeners();
    }

    /**
     * Removes a base value supplier from this property. The removed supplier will no longer provide a base value for
     * the selection strategy to choose from. If the supplier was not previously added, this method has no effect.
     * <p>
     * Calling this method will trigger a value change event.
     *
     * @param baseValueSupplier the base value supplier to remove from this property
     */
    public void removeBaseValueSupplier(@NotNull PropertyBaseSupplier<T> baseValueSupplier) {
        Objects.requireNonNull(baseValueSupplier);
        baseValueSuppliers.remove(baseValueSupplier);
        baseRegistrations.get(baseValueSupplier).unregisterAll();
        baseRegistrations.remove(baseValueSupplier);
        notifyListeners();
    }


    /**
     * Sets the strategy used to select the base value from all base values provided by the base value suppliers.
     * Typical strategies include selecting the first value, the highest value, or the lowest value. The default is
     * {@link FirstValueSelector}.
     * <p>
     * Calling this method will trigger a value change event, as will any update events sent by the value selector.
     *
     * @param baseValueSelector the strategy selecting the base value from all base values provided by the base
     *         value suppliers
     */
    public void setBaseValueSelector(@NotNull ValueSelector<T> baseValueSelector) {
        Objects.requireNonNull(baseValueSelector);
        this.baseValueSelector = baseValueSelector;
        baseSelectorRegistration.unregisterAll();
        this.baseSelectorRegistration = baseValueSelector.addUpdateListener(this);
        notifyListeners();
    }

    /**
     * Retrieves the strategy used to select the base value from all base values provided by the base value suppliers.
     *
     * @return the strategy used to select the base value from all base values provided by the base value suppliers
     */
    public ValueSelector<T> getBaseValueSelector() {
        return baseValueSelector;
    }

    /**
     * Registers a listener to be notified when the way this property is calculated changes. Specifically, the listener
     * will be notified when the base value changes, when a modifier is added or removed, or when the base value
     * selector changes. If the implementation of the modifiers and base value providers of this property notify this
     * property when they change, the listener will also be called on those changes. However, it is not guaranteed that
     * the listener will be called on every change, as the implementation of the modifiers and base value providers may
     * not notify this property of every change. It is also not guaranteed that the new value will be different from the
     * old value, since changes to the property may not affect the end result (for example, changes to a base value
     * supplier that is not selected by the selection strategy).
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
     * Notifies all listeners that the value of this property might have changed.
     */
    protected void notifyListeners() {
        T newValue = baseValueSuppliers.isEmpty() ? null : getValue();
        eventBus.fireEvent(new ValueChangeEvent<>(lastKnownValue, newValue));
        lastKnownValue = newValue;
    }

    @Override
    public void onEvent(UpdateEvent event) {
        notifyListeners();
    }
}
