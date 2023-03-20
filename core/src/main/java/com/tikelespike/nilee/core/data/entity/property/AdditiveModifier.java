package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.property.events.UpdateEvent;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;

public class AdditiveModifier implements PropertyModifier<Integer> {

    private int offset;
    private String source;

    private final EventBus eventBus = new EventBus();

    public AdditiveModifier() {
        this(0, "");
    }

    public AdditiveModifier(int offset, String source) {
        this.offset = offset;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value + offset;
    }

    @Override
    public String getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public String getConcreteDescription() {
        return "+ " + offset;
    }

    @Override
    public String getSourceName() {
        return source;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        eventBus.fireEvent(new UpdateEvent());
    }

    public int getOffset() {
        return offset;
    }

    public void setSource(String source) {
        this.source = source;
        eventBus.fireEvent(new UpdateEvent());
    }

    public String getSource() {
        return source;
    }

    @Override
    public Registration addUpdateListener(EventListener<UpdateEvent> listener) {
        return eventBus.registerListener(UpdateEvent.class, listener);
    }
}
