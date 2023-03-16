package com.tikelespike.nilee.core.data.entity.property.events;

import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;

public interface UpdateSubject {
    default Registration addUpdateListener(EventListener<UpdateEvent> listener) {
        // this default implementation is a convenience implementation for classes that would never fire update events
        // anyway, so they don't have to implement this method
        return new EventBus().registerListener(UpdateEvent.class, listener);
    }
}
