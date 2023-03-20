package com.tikelespike.nilee.core.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    public void test_singleListenerSingleEvent() {
        bus.registerListener(Event.class, simpleListener);
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called");
    }

    @Test
    public void test_notCalledOnUnsubscribedEvent() {
        bus.registerListener(TestEvent.class, simpleListener);
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 0, "Listener should not have been called by unsubscribed event type");
    }

    @Test
    public void test_calledOnSubclassEvent() {
        bus.registerListener(Event.class, simpleListener);
        bus.fireEvent(new TestEvent("test"));
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called because it subscribed to all events, specifically the subclass");
    }

    @Test
    public void test_multipleListeners() {
        bus.registerListener(TestEvent.class, simpleListener);
        bus.registerListener(TestEvent.class, messageListener);
        bus.fireEvent(new TestEvent("test"));
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called because it subscribed to all events");
        assertEquals(messageListener.getNumCalls(), 1, "Listener should have been called because it subscribed to the specific event");
        assertEquals(messageListener.getLastMessage(), "test", "Listener should have received the correct message");
    }

    @Test
    public void test_multipleRegistration() {
        bus.registerListener(Event.class, simpleListener);
        bus.registerListener(Event.class, simpleListener);
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 2, "Listener should have been called twice because it was registered twice");
    }

    @Test
    public void test_unsubscribe() {
        Registration registration = bus.registerListener(Event.class, simpleListener);
        registration.unregister();
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 0, "Listener should not have been called because unregistered before event");
    }

    @Test
    public void test_unsubscribeMultiple() {
        Registration registration = bus.registerListener(Event.class, simpleListener);
        Registration registration2 = bus.registerListener(Event.class, simpleListener);
        registration.unregister();
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should have been called once because one of two registrations was unregistered");
        registration2.unregister();
        bus.fireEvent(new Event());
        assertEquals(simpleListener.getNumCalls(), 1, "Listener should not have been called because unregistered before event");
    }

    @Test
    public void test_registrationValidity() {
        Registration registration = bus.registerListener(Event.class, simpleListener);
        assertTrue(registration.isActive(), "Registration should be active before unregistering");
        boolean success = registration.unregister();
        assertTrue(success, "Unregistering should have been successful");
        assertFalse(registration.isActive(), "Registration should not be active after unregistering");
        success = registration.unregister();
        assertFalse(success, "Unregistering should not have been successful because already unregistered");
    }

    @Test
    public void test_nullValues() {
        assertThrows(NullPointerException.class, () -> bus.registerListener(null, simpleListener));
        assertThrows(NullPointerException.class, () -> bus.registerListener(Event.class, null));
        assertThrows(NullPointerException.class, () -> bus.fireEvent(null));
    }

    private static class TestEvent extends Event {
        private final String message;

        public TestEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private static class MessageListener implements EventListener<TestEvent> {
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