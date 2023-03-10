package com.tikelespike.nilee.events;

public record TypedEventListener<T extends Event>(Class<? extends T> eventType, EventListener<T> listener) {
}
