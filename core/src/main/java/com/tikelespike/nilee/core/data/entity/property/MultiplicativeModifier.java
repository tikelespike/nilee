package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.property.events.UpdateEvent;
import com.tikelespike.nilee.core.events.EventBus;

public class MultiplicativeModifier implements PropertyModifier<Integer> {

    private int factor;
    private String source;
    private final EventBus eventBus = new EventBus();

    public MultiplicativeModifier() {
        this(1, "");
    }

    public MultiplicativeModifier(int factor, String source) {
        this.factor = factor;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value * factor;
    }

    @Override
    public String getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public String getConcreteDescription() {
        return "* " + factor;
    }

    @Override
    public String getSourceName() {
        return source;
    }

    public void setFactor(int factor) {
        this.factor = factor;
        eventBus.fireEvent(new UpdateEvent());
    }

    public int getFactor() {
        return factor;
    }

    public void setSource(String source) {
        this.source = source;
        eventBus.fireEvent(new UpdateEvent());
    }

    public String getSource() {
        return source;
    }
}
