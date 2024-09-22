/**
 * Contains the generic property system. A property models the dynamic calculation of a value for a character, which in
 * D&D is often based on some sort of base value and a number of modifiers and bonuses applied to it.
 * <p>
 * In nilee, a property is made up of different base value suppliers, a selector that determines which of these to use,
 * and a list of modifiers that further alter the final value.
 *
 * @see com.tikelespike.nilee.core.property.Property
 */
package com.tikelespike.nilee.core.property;
