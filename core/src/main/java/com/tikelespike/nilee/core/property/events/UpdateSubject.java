package com.tikelespike.nilee.core.property.events;

import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A subject that can fire {@link UpdateEvent UpdateEvents} and tracks dependencies to other UpdateSubjects. Update
 * events from dependencies will be forwarded to listeners of this subject. This creates a directed acyclic graph of
 * dependencies whose changes are propagated to all children.
 */
public abstract class UpdateSubject {

    private final EventBus bus = new EventBus();
    private final Map<UpdateSubject, Registration> dependencyRegistrations = new HashMap<>();

    /**
     * Initialize this update subject with the given dependencies. Dependencies can also be added later using
     * {@link #addDependency(UpdateSubject)}.
     *
     * @param dependencies update subjects that this subject depends on. These subjects may not themselves
     *         depend on this subject, or a cyclic dependency would be created.
     */
    protected UpdateSubject(UpdateSubject... dependencies) {
        for (UpdateSubject dependency : dependencies) {
            addDependency(dependency);
        }
    }

    /**
     * Tests if this subject has registered the passed UpdateSubject as a dependency.
     *
     * @param dependency update subject to check
     *
     * @return true if and only if this update subject has the other update subject as a dependency
     */
    protected final boolean dependsOn(UpdateSubject dependency) {
        Objects.requireNonNull(dependency);
        return dependencyRegistrations.containsKey(dependency);
    }

    /**
     * Registers the passed UpdateSubject as a dependency of this subject, which means its UpdateEvents will be
     * forwarded to listeners of this subject. Alternatively, the dependency can be passed to the constructor if it is
     * known at construction time.
     *
     * @param dependency the dependency to add. Must not be null and may not depend on this subject (because
     *         that would create a cyclic dependency)
     */
    protected final void addDependency(UpdateSubject dependency) {
        Objects.requireNonNull(dependency);
        if (dependency.dependsOn(this)) {
            throw new IllegalStateException(
                    "Cyclic dependency detected! Cyclic dependencies are impossible since they would result in an"
                            + " endless loop.");
        }
        Registration registration = dependency.addUpdateListener(this::update);
        dependencyRegistrations.put(dependency, registration);
        update();
    }

    /**
     * Removes the passed dependency from this subject. This subject will no longer forward UpdateEvents from the
     * dependency to its listeners.
     *
     * @param dependency the dependency to remove
     */
    protected final void removeDependency(UpdateSubject dependency) {
        Objects.requireNonNull(dependency);
        if (!dependsOn(dependency)) {
            return;
        }
        dependencyRegistrations.get(dependency).unregisterAll();
        dependencyRegistrations.remove(dependency);
        update();
    }

    /**
     * Registers a listener for {@link UpdateEvent UpdateEvents} fired by this subject. Update events are usually fired
     * by properties and their components when something about the calculation chain of a property changes.
     *
     * @param listener the event listener to register to update events sent by this subject
     *
     * @return a registration for the listener that can be used to unregister the listener
     * @see UpdateEvent
     */
    public Registration addUpdateListener(EventListener<UpdateEvent> listener) {
        return bus.registerListener(UpdateEvent.class, listener);
    }

    /**
     * Fires an {@link UpdateEvent} on the {@link EventBus} of this subject.
     */
    protected void update() {
        bus.fireEvent(new UpdateEvent());
    }

    /**
     * Forwards an {@link UpdateEvent} to the {@link EventBus} of this subject.
     *
     * @param event the event to fire
     */
    protected void update(UpdateEvent event) {
        bus.fireEvent(event);
    }
}
