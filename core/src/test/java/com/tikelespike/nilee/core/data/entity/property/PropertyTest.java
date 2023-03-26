package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.property.PropertyBaseSupplier;
import com.tikelespike.nilee.core.property.PropertyModifier;
import com.tikelespike.nilee.core.property.convenience.*;
import com.tikelespike.nilee.core.property.events.ValueChangeEvent;
import com.tikelespike.nilee.core.events.EventStoreListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {

    private static final int TEST_VALUE = 42;

    // CUT
    private ConstantBaseProperty property;

    private EventStoreListener<ValueChangeEvent<Integer>> listener;

    @BeforeEach
    void setUp() {
        property = new ConstantBaseProperty(TEST_VALUE, "Test property");
        property.setBaseValueSelector(new MaxValueSelector<>());

        listener = new EventStoreListener<>();
    }

    @Test
    void test_singleBase_noModifiers() {
        assertEquals(TEST_VALUE, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void test_multipleBase_noModifiers() {
        property.addBaseValueSupplier(new ConstantBaseValue(TEST_VALUE + 1, "Test base 2"));
        assertEquals(TEST_VALUE + 1, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());
    }

    @Test
    void test_singleBase_singleModifier() {
        property.addModifier(new AdditiveModifier(1, "Test modifier"));
        assertEquals(TEST_VALUE + 1, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void test_singleBase_multipleModifiers() {
        property.addModifier(new AdditiveModifier(1, "Add 1 mod"));
        property.addModifier(new MultiplicativeModifier(2, "x2 mod"));
        assertEquals((TEST_VALUE + 1) * 2, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void test_multipleBase_multipleModifiers() {
        property.addBaseValueSupplier(new ConstantBaseValue(TEST_VALUE + 1, "Test base 2"));
        property.addModifier(new AdditiveModifier(1, "Add 1 mod"));
        property.addModifier(new MultiplicativeModifier(2, "x2 mod"));
        assertEquals((TEST_VALUE + 1 + 1) * 2, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());
    }

    @Test
    void test_changeSelector() {
        property.addBaseValueSupplier(new ConstantBaseValue(TEST_VALUE + 1, "Test base 2"));
        property.addModifier(new AdditiveModifier(1, "Add 1 mod"));
        assertEquals(TEST_VALUE + 2, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());

        property.setBaseValueSelector(new FirstValueSelector<>());
        assertEquals(TEST_VALUE + 1, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void test_removeBaseValueSupplier() {
        ConstantBaseValue base = new ConstantBaseValue(TEST_VALUE + 1, "Test base 2");
        property.addBaseValueSupplier(base);
        property.addModifier(new MultiplicativeModifier(2, "x2 mod"));
        assertEquals((TEST_VALUE + 1) * 2, property.getValue());
        assertEquals(TEST_VALUE + 1, property.getBaseValue());

        property.removeBaseValueSupplier(base);
        assertEquals(TEST_VALUE * 2, property.getValue());
        assertEquals(TEST_VALUE, property.getBaseValue());
    }

    @Test
    void test_removeModifier() {
        AdditiveModifier modifier = new AdditiveModifier(1, "Add 1 mod");
        property.addModifier(modifier);
        property.addModifier(new MultiplicativeModifier(2, "x2 mod"));
        assertEquals((TEST_VALUE + 1) * 2, property.getValue());

        property.removeModifier(modifier);
        assertEquals(TEST_VALUE * 2, property.getValue());

        property.addModifier(modifier);
        assertEquals(TEST_VALUE * 2 + 1, property.getValue(), "Order of modifiers should correspond to order of addition");
    }

    @Test
    void test_selectorChangeEvent() {
        property.addBaseValueSupplier(new ConstantBaseValue(TEST_VALUE + 1, "Test base 2"));
        property.addValueChangeListener(listener);
        property.setBaseValueSelector(new FirstValueSelector<>());
        ValueChangeEvent<Integer> event = listener.getLatestEvent();
        assertNotNull(event, "Event should be fired when selector is changed");
        assertEquals(TEST_VALUE + 1, event.getOldValue(), "Old value should be the value before the selector change");
        assertEquals(TEST_VALUE, event.getNewValue(), "New value should be the value after the selector change");
    }

    @Test
    void test_newBaseSupplierEvent() {
        property.addValueChangeListener(listener);
        property.addBaseValueSupplier(new ConstantBaseValue(TEST_VALUE + 1, "Test base 2"));
        ValueChangeEvent<Integer> event = listener.getLatestEvent();
        assertNotNull(event, "Event should be fired when base supplier is added");
        assertEquals(TEST_VALUE, event.getOldValue(), "Old value should be the value before the base supplier addition");
        assertEquals(TEST_VALUE + 1, event.getNewValue(), "New value should be the value after the base supplier addition");
    }

    @Test
    void test_removeBaseSupplierEvent() {
        PropertyBaseSupplier<Integer> base = new ConstantBaseValue(TEST_VALUE + 1, "Test base 2");
        property.addBaseValueSupplier(base);
        property.addValueChangeListener(listener);
        property.removeBaseValueSupplier(base);
        ValueChangeEvent<Integer> event = listener.getLatestEvent();
        assertNotNull(event, "Event should be fired when base supplier is removed");
        assertEquals(TEST_VALUE + 1, event.getOldValue(), "Old value should be the value before the base supplier removal");
        assertEquals(TEST_VALUE, event.getNewValue(), "New value should be the value after the base supplier removal");
    }

    @Test
    void test_baseValueChangeEvent() {
        property.addValueChangeListener(listener);
        property.setDefaultBaseValue(TEST_VALUE + 1);
        ValueChangeEvent<Integer> event = listener.getLatestEvent();
        assertNotNull(event, "Event should be fired when base value is changed");
        assertEquals(TEST_VALUE, event.getOldValue(), "Old value should be the value before the base value change");
        assertEquals(TEST_VALUE + 1, event.getNewValue(), "New value should be the value after the base value change");
    }

    @Test
    void test_newModifierEvent() {
        property.addValueChangeListener(listener);
        property.addModifier(new AdditiveModifier(1, "Test modifier"));
        ValueChangeEvent<Integer> event = listener.getLatestEvent();
        assertNotNull(event, "Event should be fired when modifier is added");
        assertEquals(TEST_VALUE, event.getOldValue(), "Old value should be the value before the modifier addition");
        assertEquals(TEST_VALUE + 1, event.getNewValue(), "New value should be the value after the modifier addition");
    }

    @Test
    void test_removeModifierEvent() {
        PropertyModifier<Integer> modifier = new AdditiveModifier(1, "Test modifier");
        property.addModifier(modifier);
        property.addValueChangeListener(listener);
        property.removeModifier(modifier);
        ValueChangeEvent<Integer> event = listener.getLatestEvent();
        assertNotNull(event, "Event should be fired when modifier is removed");
        assertEquals(TEST_VALUE + 1, event.getOldValue(), "Old value should be the value before the modifier removal");
        assertEquals(TEST_VALUE, event.getNewValue(), "New value should be the value after the modifier removal");
    }

    @Test
    void test_modifierChangeEvent() {
        AdditiveModifier modifier = new AdditiveModifier(1, "Test modifier");
        property.addModifier(modifier);
        property.addValueChangeListener(listener);
        modifier.setBonus(2);
        ValueChangeEvent<Integer> event = listener.getLatestEvent();
        assertNotNull(event, "Event should be fired when modifier value is changed");
        assertEquals(TEST_VALUE + 1, event.getOldValue(), "Old value should be the value before the modifier value change");
        assertEquals(TEST_VALUE + 2, event.getNewValue(), "New value should be the value after the modifier value change");
    }

    @Test
    void test_nullParameters() {
        assertThrows(NullPointerException.class, () -> property.addBaseValueSupplier(null));
        assertThrows(NullPointerException.class, () -> property.removeBaseValueSupplier(null));
        assertThrows(NullPointerException.class, () -> property.addModifier(null));
        assertThrows(NullPointerException.class, () -> property.removeModifier(null));
        assertThrows(NullPointerException.class, () -> property.setBaseValueSelector(null));
        assertThrows(NullPointerException.class, () -> property.addValueChangeListener(null));
    }
}