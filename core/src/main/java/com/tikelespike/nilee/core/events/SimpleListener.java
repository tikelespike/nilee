package com.tikelespike.nilee.core.events;

public class SimpleListener implements EventListener<Event> {
    private int numCalls = 0;

    @Override
    public void onEvent(Event event) {
        numCalls++;
    }

    public int getNumCalls() {
        return numCalls;
    }

    public boolean wasCalled() {
        return numCalls > 0;
    }

    public void reset() {
        numCalls = 0;
    }
}
