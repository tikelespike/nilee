package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.data.entity.GameEntity;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Describes a value constructed from a base value and modifiers. The base value is chosen from all base values
 * provided by a set of base value suppliers using a selection strategy (like the first value or the maximum). The
 * modifiers are applied in the order they are added.
 * <p>
 * For example, a property could represent a character's armor class. The base value could be the standard AC
 * calculated as {@code 10 + DEX}.
 * Another way to calculate the base value could be given by armor worn, which may calculate the base AC as something
 * like {@code 12 + DEX}. The modifiers represent a number of temporary bonuses and penalties, such as a +2 bonus from a
 * shield, or a -1 penalty from a spell. The final value is automatically calculated from the selected base value
 * (default is the first one added) and the application of all modifiers.
 *
 * @param <T> the type of the value (typically, an integer or a dice roll like 3d4)
 */
@Entity
public class Property<T> extends AbstractEntity {

    @OneToMany(targetEntity = GameEntity.class, fetch = FetchType.EAGER) // fix for now, should this be lazy?
    protected final Set<PropertyBaseSupplier<T>> baseValueSuppliers = new LinkedHashSet<>();

    @OneToMany(targetEntity = GameEntity.class, fetch = FetchType.EAGER) // fix for now, should this be lazy?
    protected final Set<PropertyModifier<T>> modifiers = new LinkedHashSet<>();

    // fix for now, should this be lazy?
    @OneToOne(targetEntity = GameEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected ValueSelector<T> baseValueSelector = new FirstValueSelector<>();

    @Transient
    private final EventBus eventBus = new EventBus();

    /**
     * Default constructor for JPA. Will not add any base value suppliers or modifiers. You must add at least one
     * base value supplier before calling {@link #getValue()} or {@link #getBaseValue()} by using
     * {@link #addBaseValueSupplier(PropertyBaseSupplier)}.
     */
    protected Property() {

    }

    /**
     * Creates a property with the given default base value supplier.
     *
     * @param baseValueSupplier supplies the default value returned when calling {@link #getBaseValue()}.
     */
    public Property(PropertyBaseSupplier<T> baseValueSupplier) {
        this.baseValueSuppliers.add(baseValueSupplier);
    }


    /**
     * Calculates the value of this property by selecting a base value and applying all modifiers.
     * There has to be at least one base value supplier added before calling this method.
     *
     * @return the effective value of this property
     * @throws IllegalStateException if no base value suppliers have been added before calling this method
     */
    public T getValue() {
        return getValueOnBase(getBaseValue());
    }

    public T getValueOnBase(T base) {
        T value = base;
        for (PropertyModifier<T> modifier : getModifiers()) {
            value = modifier.apply(value);
        }
        return value;
    }

    /**
     * Calculates the base value of this property by selecting from the base value suppliers using the base value
     * selection strategy. Base value suppliers are added using {@link #addBaseValueSupplier(PropertyBaseSupplier)}.
     * There has to be at least
     * one base value supplier added before calling this method.
     * The selection strategy can be set using {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @return the base value of this property, as selected by the base value selector
     * @throws IllegalStateException if no base value suppliers have been added before calling this method
     */
    public T getBaseValue() {
        if (getBaseValueSuppliers().isEmpty())
            throw new IllegalStateException("No base value suppliers has been defined for this property");
        Optional<T> opt =
                baseValueSelector.select(getBaseValueSuppliers().stream().map(PropertyBaseSupplier::getBaseValue).toList());
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
    public Set<PropertyModifier<T>> getModifiers() {
        return new LinkedHashSet<>(modifiers);
    }

    /**
     * Adds a modifier to this property. The modifier will be applied after the base value is calculated, and will
     * affect the end result retrieved by {@link #getValue()}. All modifiers are applied in the order they are added.
     *
     * @param modifier the modifier to add to this property
     */
    public void addModifier(PropertyModifier<T> modifier) {
        T oldValue = getValue();
        modifiers.add(modifier);
        notifyListeners(oldValue);
    }

    /**
     * Removes a modifier from this property. The modifier will no longer affect the end result retrieved by
     * {@link #getValue()}. If the modifier was not previously added, this method has no effect.
     *
     * @param modifier the modifier to remove from this property
     */
    public void removeModifier(PropertyModifier<T> modifier) {
        T oldValue = getValue();
        modifiers.remove(modifier);
        notifyListeners(oldValue);
    }

    /**
     * Removes all modifiers from this property. The property will no longer be affected by any modifiers.
     */
    public void clearModifiers() {
        T oldValue = getValue();
        modifiers.clear();
        notifyListeners(oldValue);
    }


    /**
     * Retrieves the set of base value suppliers used by this property. The base value suppliers are used to
     * calculate the base value of this property, which is then modified by the modifiers. The returned set is a copy
     * of the internal set, so any changes to the returned set will not affect the property. The base value is selected
     * using the base value selector, which can be set using {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @return a copy of the set of base value suppliers used by this property
     */
    public Set<PropertyBaseSupplier<T>> getBaseValueSuppliers() {
        return new LinkedHashSet<>(baseValueSuppliers);
    }

    /**
     * Adds a base value supplier to this property. The added supplier will provide a base value for the selection
     * strategy to choose from. The selection strategy can be set using
     * {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @param baseValueSupplier the base value supplier to add to this property
     */
    public void addBaseValueSupplier(PropertyBaseSupplier<T> baseValueSupplier) {
        T oldValue = getValue();
        baseValueSuppliers.add(baseValueSupplier);
        notifyListeners(oldValue);
    }

    /**
     * Removes a base value supplier from this property. The removed supplier will no longer provide a base value for
     * the selection strategy to choose from. If the supplier was not previously added, this method has no effect.
     *
     * @param baseValueSupplier the base value supplier to remove from this property
     */
    public void removeBaseValueSupplier(PropertyBaseSupplier<T> baseValueSupplier) {
        T oldValue = getValue();
        baseValueSuppliers.remove(baseValueSupplier);
        notifyListeners(oldValue);
    }


    /**
     * Sets the strategy used to select the base value from all base values provided by the base value suppliers.
     * Typical strategies include selecting the first value, the highest value, or the lowest value. The default is
     * {@link FirstValueSelector}.
     *
     * @param baseValueSelector the strategy selecting the base value from all base values provided by the base value
     *                          suppliers
     */
    public void setBaseValueSelector(ValueSelector<T> baseValueSelector) {
        T oldValue = getValue();
        this.baseValueSelector = baseValueSelector;
        notifyListeners(oldValue);
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
     * Registers a listener to be notified when the way this property is calculated changes. Specifically,
     * the listener will be notified when the base value changes, when a modifier is added or removed, or when the
     * base value selector changes.
     *
     * @param listener the listener to register
     * @return a registration object that can be used to unregister the listener
     */
    public Registration addPropertyChangeListener(EventListener<? super PropertyChangeEvent> listener) {
        return eventBus.registerListener(PropertyChangeEvent.class, listener);
    }

    /**
     * Registers a listener to be notified when a change in the way this property is calculated results in a change
     * in the value of this property. This will not notify the listener when internals of the base value providers,
     * modifiers, or base value selector change, but only when modifying the property itself results in a change in
     * the value of the property.
     * <p>
     * Therefore, subscribing to this event is equivalent to subscribing to PropertyChangeEvents and checking manually
     * whether the new value is different from the old value (as in the {@code equals} relationship).
     *
     * @param listener the listener to register
     * @return a registration object that can be used to unregister the listener
     */
    public Registration addValueChangeListener(EventListener<? super ValueChangeEvent> listener) {
        return eventBus.registerListener(ValueChangeEvent.class, listener);
    }

    protected void notifyListeners(T oldValue) {
        T newValue = getValue();
        if (!Objects.equals(oldValue, newValue)) {
            eventBus.fireEvent(new ValueChangeEvent(this, oldValue));
        }
        eventBus.fireEvent(new PropertyChangeEvent(this, oldValue));
    }

    /**
     * An event fired when the way a property is calculated changes. Contains the old value and a reference to the
     * property that changed.
     */
    public class PropertyChangeEvent extends PropertyChangeEventBase {
        /**
         * Creates a new property change event.
         *
         * @param property the property that changed
         * @param oldValue the old value of the property
         */
        public PropertyChangeEvent(Property<T> property, T oldValue) {
            super(property, oldValue);
        }
    }

    /**
     * An event fired when the way a property is calculated changes, and the new value is different from the old
     * value. Contains the old value and a reference to the property that changed.
     */
    public class ValueChangeEvent extends PropertyChangeEventBase {
        /**
         * Creates a new value change event.
         *
         * @param property the property that changed
         * @param oldValue the old value of the property, has to be different to the new value
         * @throws IllegalArgumentException if the old and new value are equal
         */
        public ValueChangeEvent(Property<T> property, T oldValue) {
            super(property, oldValue);
            if (Objects.equals(oldValue, getNewValue()))
                throw new IllegalArgumentException("Old and new value are equal");
        }
    }

    private abstract class PropertyChangeEventBase extends Event {
        private final T oldValue;
        private final Property<T> property;

        public PropertyChangeEventBase(Property<T> property, T oldValue) {
            this.property = property;
            this.oldValue = oldValue;
        }

        /**
         * @return the value {@link Property#getValue()} returned before the change
         */
        public T getOldValue() {
            return oldValue;
        }

        /**
         * @return the value {@link Property#getValue()} returns after the change
         */
        public T getNewValue() {
            return property.getValue();
        }

        /**
         * @return the property that changed
         */
        public Property<T> getProperty() {
            return property;
        }
    }
}