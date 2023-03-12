package com.tikelespike.nilee.core.events;

public interface EventListener<T extends Event> {

    void onEvent(T event);

}
