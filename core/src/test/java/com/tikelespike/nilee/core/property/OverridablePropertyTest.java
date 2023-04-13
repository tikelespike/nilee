package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.property.convenience.AdditiveModifier;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OverridablePropertyTest {

    private static final int TEST_VALUE = 42;
    private static final int OVERRIDE_VALUE = 69;

    // CUT
    private OverridableProperty<Integer> property;

    @BeforeEach
    void setUp() {
        property = new OverridableProperty<>(new ConstantBaseValue(TEST_VALUE, t -> "Test base"));
    }

    @Test
    void test_noOverride() {
        assertEquals(TEST_VALUE, property.getValue());
        assertFalse(property.isOverridden());
    }

    @Test
    void test_override_noOtherModifiers() {
        property.setOverride(OVERRIDE_VALUE);
        assertEquals(OVERRIDE_VALUE, property.getValue());
        assertTrue(property.isOverridden());
    }

    @Test
    void test_override_withOtherModifiers() {
        property.addModifier(new AdditiveModifier(1, t -> "Add 1 mod"));
        property.setOverride(OVERRIDE_VALUE);
        assertEquals(OVERRIDE_VALUE, property.getValue());
        assertTrue(property.isOverridden());
    }

    @Test
    void test_override_staysLastWhenNewModifierAdded() {
        property.setOverride(OVERRIDE_VALUE);
        property.addModifier(new AdditiveModifier(1, t -> "Add 1 mod"));
        assertEquals(OVERRIDE_VALUE, property.getValue());
        assertTrue(property.isOverridden());
    }

    @Test
    void test_removeOverride() {
        property.setOverride(OVERRIDE_VALUE);
        property.removeOverride();
        assertEquals(TEST_VALUE, property.getValue());
        assertFalse(property.isOverridden());
    }

    @Test
    void test_removeOverride_withOtherModifiers() {
        property.setOverride(OVERRIDE_VALUE);
        property.addModifier(new AdditiveModifier(1, t -> "Add 1 mod"));
        property.removeOverride();
        assertEquals(TEST_VALUE + 1, property.getValue());
        assertFalse(property.isOverridden());
    }
}