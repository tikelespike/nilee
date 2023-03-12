package com.tikelespike.nilee.core.events;

public record TypedEventListener<T extends Event>(Class<? extends T> eventType, EventListener<T> listener) {
}
