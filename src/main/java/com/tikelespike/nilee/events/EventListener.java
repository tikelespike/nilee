package com.tikelespike.nilee.events;

public interface EventListener<T extends Event> {

    void onEvent(T event);

}
