package com.tikelespike.nilee.core.events;

/**
 * An event listener that counts how often it was called and does nothing else. Useful mainly for testing and debugging
 * purposes.
 */
public class SimpleListener implements EventListener<Event> {
    private int numCalls = 0;

    @Override
    public void onEvent(Event event) {
        numCalls++;
    }

    /**
     * Returns the amount of times this listener's {@link #onEvent(Event)} method was called.
     *
     * @return the amount of times this listener's {@link #onEvent(Event)} method was called
     */
    public int getNumCalls() {
        return numCalls;
    }

    /**
     * Convenience method that returns true if this listener's {@link #onEvent(Event)} method was called at least once.
     *
     * @return true iff. this listener was called at least once
     */
    public boolean wasCalled() {
        return numCalls > 0;
    }

    /**
     * Sets the counter of this listener back to 0, as if it had never been called before.
     */
    public void reset() {
        numCalls = 0;
    }
}
