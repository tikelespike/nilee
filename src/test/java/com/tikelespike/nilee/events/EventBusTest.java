package com.tikelespike.nilee.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventBusTest {

    // CUT
    private EventBus bus;

    private SimpleListener simpleListener;
    private MessageListener messageListener;

    @BeforeEach
    public void setUp() {
        bus = new EventBus();
        simpleListener = new SimpleListener();
        messageListener = new MessageListener();
    }

    @Test
    public void testSingleListenerSingleEvent() {
        bus.registerListener(Event.class, simpleListener);
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called");
    }

    @Test
    public void testNotCalledOnUnsubscribedEvent() {
        bus.registerListener(TestEvent.class, simpleListener);
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 0, "Listener should not have been called by unsubscribed event type");
    }

    @Test
    public void testCalledOnSubclassEvent() {
        bus.registerListener(Event.class, simpleListener);
        bus.fireEvent(new TestEvent("test"));
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called because it subscribed to all events, specifically the subclass");
    }

    @Test
    public void testMultipleListeners() {
        bus.registerListener(TestEvent.class, simpleListener);
        bus.registerListener(TestEvent.class, messageListener);
        bus.fireEvent(new TestEvent("test"));
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called because it subscribed to all events");
        assertEquals(messageListener.getNumCalls(), 1, "Listener should have been called because it subscribed to the specific event");
        assertEquals(messageListener.getLastMessage(), "test", "Listener should have received the correct message");
    }

    @Test
    public void testMultipleRegistration() {
        bus.registerListener(Event.class, simpleListener);
        bus.registerListener(Event.class, simpleListener);
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 2, "Listener should have been called twice because it was registered twice");
    }

    @Test
    public void testUnsubscribe() {
        Registration registration = bus.registerListener(Event.class, simpleListener);
        registration.unregister();
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 0, "Listener should not have been called because unregistered before event");
    }

    @Test
    public void testUnsubscribeMultiple() {
        Registration registration = bus.registerListener(Event.class, simpleListener);
        Registration registration2 = bus.registerListener(Event.class, simpleListener);
        registration.unregister();
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called once because one of two registrations was unregistered");
        registration2.unregister();
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should not have been called because unregistered before event");
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

    private class SimpleListener implements EventListener<Event> {
        private int numCalls = 0;

        @Override
        public void onEvent(Event event) {
            numCalls++;
        }

        public int getNumCalls() {
            return numCalls;
        }
    }

    private class MessageListener implements EventListener<TestEvent> {
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