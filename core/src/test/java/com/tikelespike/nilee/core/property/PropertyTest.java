package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.events.EventStoreListener;
import com.tikelespike.nilee.core.property.convenience.AdditiveModifier;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseProperty;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseValue;
import com.tikelespike.nilee.core.property.convenience.FirstValueSelector;
import com.tikelespike.nilee.core.property.convenience.ManualOverrideModifier;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;
import com.tikelespike.nilee.core.property.convenience.MultiplicativeModifier;
import com.tikelespike.nilee.core.property.events.UpdateEvent;
import com.tikelespike.nilee.core.property.events.ValueChangeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PropertyTest {

    //CHECKSTYLE.OFF: MagicNumber

    private static final int TEST_VALUE = 42;

    // CUT
    private ConstantBaseProperty<Integer> property;

    private EventStoreListener<ValueChangeEvent<Integer>> valueListener;
    private EventStoreListener<UpdateEvent> updateListener;

    @BeforeEach
    void setUp() {
        property = new ConstantBaseProperty<>(TEST_VALUE, t -> "Test property");
        property.setBaseValueSelector(new MaxValueSelector<>());

        valueListener = new EventStoreListener<>();
        updateListener = new EventStoreListener<>();
    }

    @Test
    void testSingleBaseNoModifiers() {
        assertEquals(TEST_VALUE, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void testMultipleBaseNoModifiers() {
        property.addBaseValueSupplier(new ConstantBaseValue<>(TEST_VALUE + 1, t -> "Test base 2"));
        assertEquals(TEST_VALUE + 1, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());
    }

    @Test
    void testSingleBaseSingleModifier() {
        property.addModifier(new AdditiveModifier(1, t -> "Test modifier"));
        assertEquals(TEST_VALUE + 1, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void testSingleBaseMultipleModifiers() {
        property.addModifier(new AdditiveModifier(1, t -> "Add 1 mod"));
        property.addModifier(new MultiplicativeModifier(2, t -> "x2 mod"));
        assertEquals((TEST_VALUE + 1) * 2, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void testMultipleBaseMultipleModifiers() {
        property.addBaseValueSupplier(new ConstantBaseValue<>(TEST_VALUE + 1, t -> "Test base 2"));
        property.addModifier(new AdditiveModifier(1, t -> "Add 1 mod"));
        property.addModifier(new MultiplicativeModifier(2, t -> "x2 mod"));
        assertEquals((TEST_VALUE + 1 + 1) * 2, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());
    }

    @Test
    void testChangeSelector() {
        property.addBaseValueSupplier(new ConstantBaseValue<>(TEST_VALUE + 1, t -> "Test base 2"));
        property.addModifier(new AdditiveModifier(1, t -> "Add 1 mod"));
        assertEquals(TEST_VALUE + 2, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());

        property.setBaseValueSelector(new FirstValueSelector<>());
        assertEquals(TEST_VALUE + 1, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void testRemoveBaseValueSupplier() {
        ConstantBaseValue<Integer> base = new ConstantBaseValue<>(TEST_VALUE + 1, t -> "Test base 2");
        property.addBaseValueSupplier(base);
        property.addModifier(new MultiplicativeModifier(2, t -> "x2 mod"));
        assertEquals((TEST_VALUE + 1) * 2, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());

        property.removeBaseValueSupplier(base);
        assertEquals(TEST_VALUE * 2, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void testRemoveModifier() {
        AdditiveModifier modifier = new AdditiveModifier(1, t -> "Add 1 mod");
        property.addModifier(modifier);
        property.addModifier(new MultiplicativeModifier(2, t -> "x2 mod"));
        assertEquals((TEST_VALUE + 1) * 2, property.getValue());

        property.removeModifier(modifier);
        assertEquals(TEST_VALUE * 2, property.getValue());

        property.addModifier(modifier);
        assertEquals(TEST_VALUE * 2 + 1, property.getValue(),
                "Order of modifiers should correspond to order of addition");
    }

    @Test
    void testSelectorChangeEvent() {
        property.addBaseValueSupplier(new ConstantBaseValue<>(TEST_VALUE + 1, t -> "Test base 2"));
        property.addValueChangeListener(valueListener);
        property.setBaseValueSelector(new FirstValueSelector<>());
        ValueChangeEvent<Integer> event = valueListener.getLatestEvent();
        assertNotNull(event, "Event should be fired when value has changed because of new selector");
        assertEquals(TEST_VALUE + 1, event.getOldValue(), "Old value should be the value before the selector change");
        assertEquals(TEST_VALUE, event.getNewValue(), "New value should be the value after the selector change");
    }

    @Test
    void testNewBaseSupplierEvent() {
        property.addValueChangeListener(valueListener);
        property.addBaseValueSupplier(new ConstantBaseValue<>(TEST_VALUE + 1, t -> "Test base 2"));
        ValueChangeEvent<Integer> event = valueListener.getLatestEvent();
        assertNotNull(event, "Event should be fired when value changes due to new base supplier");
        assertEquals(TEST_VALUE, event.getOldValue(),
                "Old value should be the value before the base supplier addition");
        assertEquals(TEST_VALUE + 1, event.getNewValue(),
                "New value should be the value after the base supplier addition");
    }

    @Test
    void testRemoveBaseSupplierEvent() {
        PropertyBaseSupplier<Integer> base = new ConstantBaseValue<>(TEST_VALUE + 1, t -> "Test base 2");
        property.addBaseValueSupplier(base);
        property.addValueChangeListener(valueListener);
        property.removeBaseValueSupplier(base);
        ValueChangeEvent<Integer> event = valueListener.getLatestEvent();
        assertNotNull(event, "Event should be fired when value changes due to removed base supplier");
        assertEquals(TEST_VALUE + 1, event.getOldValue(),
                "Old value should be the value before the base supplier removal");
        assertEquals(TEST_VALUE, event.getNewValue(), "New value should be the value after the base supplier removal");
    }

    @Test
    void testBaseValueChangeEvent() {
        property.addValueChangeListener(valueListener);
        property.setDefaultBaseValue(TEST_VALUE + 1);
        ValueChangeEvent<Integer> event = valueListener.getLatestEvent();
        assertNotNull(event, "Event should be fired when base value is changed");
        assertEquals(TEST_VALUE, event.getOldValue(), "Old value should be the value before the base value change");
        assertEquals(TEST_VALUE + 1, event.getNewValue(), "New value should be the value after the base value change");
    }

    @Test
    void testNewModifierEvent() {
        property.addValueChangeListener(valueListener);
        property.addModifier(new AdditiveModifier(1, t -> "Test modifier"));
        ValueChangeEvent<Integer> event = valueListener.getLatestEvent();
        assertNotNull(event, "Event should be fired when value changes due to new modifier");
        assertEquals(TEST_VALUE, event.getOldValue(), "Old value should be the value before the modifier addition");
        assertEquals(TEST_VALUE + 1, event.getNewValue(), "New value should be the value after the modifier addition");
    }

    @Test
    void testRemoveModifierEvent() {
        PropertyModifier<Integer> modifier = new AdditiveModifier(1, t -> "Test modifier");
        property.addModifier(modifier);
        property.addValueChangeListener(valueListener);
        property.removeModifier(modifier);
        ValueChangeEvent<Integer> event = valueListener.getLatestEvent();
        assertNotNull(event, "Event should be fired when value changes because modifier is removed");
        assertEquals(TEST_VALUE + 1, event.getOldValue(), "Old value should be the value before the modifier removal");
        assertEquals(TEST_VALUE, event.getNewValue(), "New value should be the value after the modifier removal");
    }

    @Test
    void testModifierChangeEvent() {
        AdditiveModifier modifier = new AdditiveModifier(1, t -> "Test modifier");
        property.addModifier(modifier);
        property.addValueChangeListener(valueListener);
        modifier.setBonus(2);
        ValueChangeEvent<Integer> event = valueListener.getLatestEvent();
        assertNotNull(event, "Event should be fired when modifier value is changed");
        assertEquals(TEST_VALUE + 1, event.getOldValue(),
                "Old value should be the value before the modifier value change");
        assertEquals(TEST_VALUE + 2, event.getNewValue(),
                "New value should be the value after the modifier value change");
    }

    @Test
    void testNoValueChangeEventsWithoutValueChangeNewModifier() {
        property.addValueChangeListener(valueListener);
        property.addModifier(new AdditiveModifier(0, t -> "Test modifier"));
        assertNull(valueListener.getLatestEvent(), "No value change event should be fired when value does not change");
    }

    @Test
    void testNoValueChangeEventsWithoutValueChangeNewBase() {
        property.addValueChangeListener(valueListener);
        property.addBaseValueSupplier(new ConstantBaseValue<>(TEST_VALUE - 5, t -> "Test base 2"));
        assertNull(valueListener.getLatestEvent(), "No value event should be fired when value does not change");
    }

    @Test
    void testNoValueChangeEventsWithoutValueChangeBaseValueChanged() {
        property.addModifier(new ManualOverrideModifier<>(0));
        property.addValueChangeListener(valueListener);
        property.setDefaultBaseValue(TEST_VALUE + 5);
        assertNull(valueListener.getLatestEvent(), "No value event should be fired when value does not change");
    }

    @Test
    void testUpdateEventWhenModifierAdded() {
        property.addUpdateListener(updateListener);
        property.addModifier(new AdditiveModifier(0, t -> "Test modifier"));
        assertNotNull(updateListener.getLatestEvent(), "Update event should be fired when modifier is added");
    }

    @Test
    void testUpdateEventWhenModifierTextChanges() {
        AdditiveModifier modifier = new AdditiveModifier(0, t -> "Test modifier");
        property.addModifier(modifier);
        property.addUpdateListener(updateListener);
        modifier.setSource(t -> "New source");
        assertNotNull(updateListener.getLatestEvent(), "Update event should be fired when modifier is added");
    }

    @Test
    void testNullParameters() {
        assertThrows(NullPointerException.class, () -> property.addBaseValueSupplier(null));
        assertThrows(NullPointerException.class, () -> property.removeBaseValueSupplier(null));
        assertThrows(NullPointerException.class, () -> property.addModifier(null));
        assertThrows(NullPointerException.class, () -> property.removeModifier(null));
        assertThrows(NullPointerException.class, () -> property.setBaseValueSelector(null));
        assertThrows(NullPointerException.class, () -> property.addValueChangeListener(null));
    }

    //CHECKSTYLE.ON: MagicNumber
}
