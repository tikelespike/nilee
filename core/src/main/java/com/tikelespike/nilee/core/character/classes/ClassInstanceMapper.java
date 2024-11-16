package com.tikelespike.nilee.core.character.classes;

/**
 * Maps between a class instance (the character-specific part of a D&D 5e class) business object and its database entity
 * used for persisting its permanent state.
 *
 * @param <EntityType> the type of the database entity
 * @param <BusinessObjectType> the type of the class instance business object
 */
public interface ClassInstanceMapper<EntityType extends ClassInstanceEntity, BusinessObjectType extends ClassInstance> {

    /**
     * Converts a database entity to a business object.
     *
     * @param entity the database entity to convert to a business object
     *
     * @return the business object
     */
    BusinessObjectType toBusinessObject(EntityType entity);

    /**
     * Converts a business object to a database entity.
     *
     * @param businessObject the business object to convert to a database entity
     *
     * @return the database entity
     */
    EntityType toEntity(BusinessObjectType businessObject);
}
