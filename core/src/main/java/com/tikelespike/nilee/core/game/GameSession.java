package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a temporary time window where players come together play a game. A game session is a container for
 * short-lived game state shared between players. It thus enables communication between players currently playing
 * together.
 */
public class GameSession {

    private final UUID id = java.util.UUID.randomUUID();
    private final RollBus rollBus = new RollBus();
    private final EventBus eventBus = new EventBus();
    private final List<User> participants = new ArrayList<>();

    /**
     * Creates a new game session. Should only be used by implementations of {@link GameSessionManager}. Use
     * {@link GameSessionManager#newSession()} to create a new session.
     */
    protected GameSession() {
    }

    /**
     * @return the default bus on which rolls visible to all players sharing this session are made
     */
    public RollBus getRollBus() {
        return rollBus;
    }

    /**
     * @return the unique identifier of this game session
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param participant the user to add to this session
     */
    protected void addParticipant(User participant) {
        participants.add(participant);
    }

    /**
     * Notifies all listeners that a user has joined this session. Separate from {@link #addParticipant(User)} because
     * consistency invariants need to be assured by the current {@link GameSessionManager} before calling listeners.
     *
     * @param participant the user added to this session
     */
    protected void onParticipantAdded(User participant) {
        eventBus.fireEvent(new UserJoinedEvent(this, participant));
    }

    /**
     * Removes a user from this session. Will do nothing if the user is not in this session in the first place.
     *
     * @param participant the user to remove from this session
     * @return true if and only if the participant was in this session before calling this method
     */
    protected boolean removeParticipant(User participant) {
        return participants.remove(participant);
    }

    /**
     * Notifies all listeners that a user has left this session. Separate from {@link #removeParticipant(User)} because
     * consistency invariants need to be assured by the current {@link GameSessionManager} before calling listeners.
     *
     * @param participant the user removed from this session
     */
    protected void onParticipantRemoved(User participant) {
        eventBus.fireEvent(new UserLeftEvent(this, participant));
    }

    /**
     * Registers a listener to be notified when a user joins this session. At the time this listener will be called,
     * the user will have left their old session, joined this session, and listeners of the old session have
     * been notified of the user leaving.
     *
     * @param listener the listener to register
     * @return a registration object that can be used to unregister the listener
     */
    public Registration addUserJoinedListener(EventListener<UserJoinedEvent> listener) {
        return eventBus.registerListener(UserJoinedEvent.class, listener);
    }

    /**
     * Registers a listener to be notified when a user leaves this session. This listener will be notified
     * after the user has left the session (and joined their next session), but before listeners listening to the new
     * session of the user are notified of them joining.
     *
     * @param listener the listener to register
     * @return a registration object that can be used to unregister the listener
     */
    public Registration addUserLeftListener(EventListener<UserLeftEvent> listener) {
        return eventBus.registerListener(UserLeftEvent.class, listener);
    }

    /**
     * @return an unmodifiable copy of the list of all participants in this session
     */
    public List<User> getParticipants() {
        return List.copyOf(participants);
    }

    @Override
    public String toString() {
        return getId().toString();
    }

    /**
     * Event fired when a user joins a session.
     */
    public static class UserJoinedEvent extends Event {
        private final User newUser;
        private final GameSession session;

        /**
         * Creates a new event informing about a new user having joined a session.
         *
         * @param newUser the user that joined the session
         * @param session the session the user joined
         */
        public UserJoinedEvent(GameSession session, User newUser) {
            this.newUser = newUser;
            this.session = session;
        }

        /**
         * @return the user that joined the session
         */
        public User getNewUser() {
            return newUser;
        }

        /**
         * @return the session the user joined
         */
        public GameSession getSession() {
            return session;
        }
    }

    /**
     * Event fired when a user leaves a session.
     */
    public static class UserLeftEvent extends Event {
        private final User user;
        private final GameSession session;

        /**
         * Creates a new event informing about a user having left a session.
         *
         * @param newUser the user that left the session
         */
        public UserLeftEvent(GameSession session, User newUser) {
            this.user = newUser;
            this.session = session;
        }

        /**
         * @return the user that left the session
         */
        public User getUser() {
            return user;
        }

        /**
         * @return the session the user left
         */
        public GameSession getSession() {
            return session;
        }
    }
}
