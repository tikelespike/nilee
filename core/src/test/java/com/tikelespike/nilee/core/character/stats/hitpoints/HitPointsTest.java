package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.events.SimpleListener;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HitPointsTest {
    //CHECKSTYLE.OFF: MagicNumber

    private static final int TEST_CONSTITUTION = 16;

    // CUT
    private HitPoints hitPoints;

    private int maxHitPoints;

    private SimpleListener hpListener;
    private SimpleListener tempHpListener;

    @BeforeEach
    void setUp() {
        hitPoints = new HitPoints(
                new AbilityScore(TEST_CONSTITUTION, Ability.CONSTITUTION, new ConstantBaseProperty<>(0, null)));
        hitPoints.setCurrentHitPoints(0);
        maxHitPoints = hitPoints.getMaxHitPoints().getValue();
        hpListener = new SimpleListener();
        tempHpListener = new SimpleListener();
        hitPoints.registerCurrentHPChangeListener(hpListener);
        hitPoints.registerTempHPChangeListener(tempHpListener);
    }

    @Test
    void testGetMaxHitPoints() {
        assertEquals((TEST_CONSTITUTION - 10) / 2, maxHitPoints);
    }

    @Test
    void testGetSetCurrentHitPoints() {
        hitPoints.setCurrentHitPoints(maxHitPoints);
        assertEquals(maxHitPoints, hitPoints.getCurrentHitPoints());
        assertTrue(hpListener.wasCalled());
    }

    @Test
    void testGetSetTemporaryHitPoints() {
        hitPoints.setTemporaryHitPoints(5);
        assertEquals(5, hitPoints.getTemporaryHitPoints());
        assertTrue(tempHpListener.wasCalled());
    }

    @Test
    void testTakeDamage() {
        hitPoints.setCurrentHitPoints(maxHitPoints);
        hitPoints.setTemporaryHitPoints(5);
        hpListener.reset();
        tempHpListener.reset();
        hitPoints.takeDamage(6);
        assertEquals(maxHitPoints - 1, hitPoints.getCurrentHitPoints());
        assertEquals(0, hitPoints.getTemporaryHitPoints());
        assertTrue(hpListener.wasCalled());
        assertTrue(tempHpListener.wasCalled());
        hitPoints.takeDamage(maxHitPoints);
        assertEquals(0, hitPoints.getCurrentHitPoints());
    }

    @Test
    void testHeal() {
        hitPoints.setCurrentHitPoints(maxHitPoints - 2);
        hpListener.reset();
        hitPoints.heal(1);
        assertEquals(maxHitPoints - 1, hitPoints.getCurrentHitPoints());
        assertTrue(hpListener.wasCalled());
        hitPoints.heal(1);
        assertEquals(maxHitPoints, hitPoints.getCurrentHitPoints());
        hitPoints.setCurrentHitPoints(0);
        hitPoints.heal(2 * maxHitPoints);
        assertEquals(maxHitPoints, hitPoints.getCurrentHitPoints());
    }

    //CHECKSTYLE.ON: MagicNumber
}
