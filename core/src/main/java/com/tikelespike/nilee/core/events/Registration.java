package com.tikelespike.nilee.core.events;

/**
 * A registration stores the information about a listener that is registered to an event bus. It is received upon
 * registering a listener on an event bus using {@link EventBus#registerListener(Class, EventListener)}. A registration
 * can be used to unregister the listener, or to check if it is still registered.
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
     * @param bus the event bus the listener is registered to
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

    /**
     * Unregisters all registrations of the listener (the registration of which returned this object) from the
     * corresponding event bus. This is useful because a listener can be registered multiple times (resulting in it
     * getting called multiple times upon an event), and this method removes all registrations.
     *
     * @return true if the listener was removed at least once, false if it was not registered
     */
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

    /**
     * Factory method for a dummy registration (as in the design pattern null object) that does nothing and is used as a
     * default guard object.
     *
     * @return placeholder registration that is never active and does nothing when {@link #unregister()} is called
     */
    public static Registration getInvalid() {
        return new InvalidRegistration();
    }

    private static final class InvalidRegistration extends Registration {
        private InvalidRegistration() {
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
