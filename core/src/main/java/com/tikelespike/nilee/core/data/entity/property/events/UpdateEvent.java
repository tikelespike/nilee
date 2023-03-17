package com.tikelespike.nilee.core.data.entity.property.events;

import com.tikelespike.nilee.core.data.entity.property.PropertyBaseSupplier;
import com.tikelespike.nilee.core.events.Event;

/**
 * An UpdateEvent signals that something about a subject class changed in way that is of interest to observers.
 * For example, when a {@link PropertyBaseSupplier} changes the value it provides, it should fire an UpdateEvent to
 * notify corresponding properties that their result value may have changed.
 */
public class UpdateEvent extends Event {
}
