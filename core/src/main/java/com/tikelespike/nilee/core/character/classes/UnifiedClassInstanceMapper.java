package com.tikelespike.nilee.core.character.classes;

/**
 * Maps between any class instance business object and its database entity used for persisting its permanent state.
 */
public interface UnifiedClassInstanceMapper {

    /**
     * Converts a database entity to a business object.
     *
     * @param entity the database entity to convert to a business object
     *
     * @return the business object
     */
    ClassInstance toBusinessObject(ClassInstanceEntity entity);

    /**
     * Converts a business object to a database entity.
     *
     * @param businessObject the business object to convert to a database entity
     *
     * @return the database entity
     */
    ClassInstanceEntity toEntity(ClassInstance businessObject);

}
