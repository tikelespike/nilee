package com.tikelespike.nilee.core.events;

/**
 * A registration stores the information about a listener that is registered to an event bus. It is received upon
 * registering a listener on an event bus using {@link EventBus#registerListener(Class, EventListener)}.
 * A registration can be used to unregister the listener, or to check if it is still registered.
 *
 * @see EventBus#registerListener(Class, EventListener)
 */
public class Registration {
    private final EventBus bus;
    private final TypedEventListener<?> listener;

    /**
     * Creates a new registration object. Should only be called by the event bus. Use the event bus to register
     * listeners, which will return a corresponding registration object.
     *
     * @param bus      the event bus the listener is registered to
     * @param listener the listener that is registered
     */
    protected Registration(EventBus bus, TypedEventListener<?> listener) {
        this.bus = bus;
        this.listener = listener;
    }

    /**
     * Unregisters the listener (the registration of which returned this object) from the corresponding event bus.
     *
     * @return true if the listener was removed, false if it was not registered
     */
    public boolean unregister() {
        return bus.unregister(listener);
    }

    public boolean unregisterAll() {
        return bus.unregisterAll(listener);
    }

    /**
     * Checks if the listener (the registration of which returned this object) is still subscribed to the corresponding
     * event bus.
     *
     * @return true if the listener is still subscribed, false if it is not no longer subscribed
     */
    public boolean isActive() {
        return bus.isSubscribed(listener);
    }

    public static Registration getInvalid() {
        return new InvalidRegistration();
    }

    private static class InvalidRegistration extends Registration {
        public InvalidRegistration() {
            super(null, null);
        }

        @Override
        public boolean unregister() {
            return false;
        }

        @Override
        public boolean unregisterAll() {
            return false;
        }

        @Override
        public boolean isActive() {
            return false;
        }
    }
}
