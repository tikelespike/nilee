package com.tikelespike.nilee.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventBusTest {
    private EventBus bus;

    @BeforeEach
    public void setUp() {
        bus = new EventBus();
    }

    @Test
    public void testSingleListenerSingleEvent() {
        GenTestListener listener = new GenTestListener();
        bus.registerListener(Event.class, listener);
        bus.fireEvent(new Event());
        assertEquals(listener.getNumCalls(), 1, "Listener should have been called");
    }

    private class TestEvent extends Event {
        private String message;

        public TestEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private class GenTestListener implements EventListener<Event> {
        private int numCalls = 0;

        @Override
        public void onEvent(Event event) {
            numCalls++;
        }

        public int getNumCalls() {
            return numCalls;
        }
    }

    private class TestEventListener implements EventListener<TestEvent> {
        private int numCalls = 0;
        private String lastMessage;

        @Override
        public void onEvent(TestEvent event) {
            numCalls++;
            lastMessage = event.getMessage();
        }

        public int getNumCalls() {
            return numCalls;
        }

        public String getLastMessage() {
            return lastMessage;
        }
    }
}