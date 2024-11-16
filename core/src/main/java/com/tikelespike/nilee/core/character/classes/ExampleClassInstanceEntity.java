package com.tikelespike.nilee.core.character.classes;

import jakarta.persistence.Entity;

/**
 * An example database entity storing the persistent state of a D&D 5e ClassInstance.
 */
@Entity
public class ExampleClassInstanceEntity extends ClassInstanceEntity {
    private String exampleState;

    /**
     * @return the example state
     */
    public String getExampleState() {
        return exampleState;
    }

    /**
     * @param exampleState the example state to set
     */
    public void setExampleState(String exampleState) {
        this.exampleState = exampleState;
    }
}
