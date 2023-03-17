package com.tikelespike.nilee.core.data.entity.property.events;

import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;

/**
 * A subject that can fire {@link UpdateEvent UpdateEvents}. If a class implements this interface, it means that it
 * should accept listeners for update events, and fire update events when appropriate.
 * <p>
 * If a class is constant and never changes returned values, it may implement this interface without overriding
 * its default implementation, which is to simply ignore any observers (assuming no events will be fired anyway).
 */
public interface UpdateSubject {

    /**
     * Registers a listener for {@link UpdateEvent UpdateEvents} fired by this subject. The implementation should
     * use an {@link EventBus} to register the listener, and return a corresponding {@link Registration} for the
     * listener. The default implementation of this method is a convenience implementation for classes that would
     * never fire update events anyway (because the values returned by their methods don't change) which simply
     * ignores the listener.
     *
     * @param listener the event listener to register to update events sent by this subject
     * @return a registration for the listener that can be used to unregister the listener
     */
    default Registration addUpdateListener(EventListener<UpdateEvent> listener) {
        // this default implementation is a convenience implementation for classes that would never fire update events
        // anyway, so they don't have to implement this method
        return new EventBus().registerListener(UpdateEvent.class, listener);
    }
}
