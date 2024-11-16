package com.tikelespike.nilee.core.character.classes;

import java.util.List;

/**
 * Since the available 5e classes are usually determined at runtime, this class is responsible for managing the class
 * archetypes (5e classes) that are available in the system. New classes can be registered, and the list of available
 * classes can be retrieved.
 *
 * @see ClassArchetype
 */
public interface ClassArchetypeManager {

    /**
     * Adds a new class archetype to the system. A D&D 5e class is always represented as a combination of multiple Java
     * objects:
     * <ul>
     *     <li>a java object describing the generic rules, name, etc. of the D&D class (called a class archetype)</li>
     *     <li>a java class used to hold the player-specific state of the D&D class (called a class instance, since
     *     it is instantiated once for each character) and</li>
     *     <li>a java class used as a database entity for persisting the state of the class
     *     instance that is required to be stored on the database permanently (called a class instance entity)</li>
     * </ul>
     * Additionally, a mapper is required that can convert between the class instance entity and the class instance.
     *
     * @param archetype the 5e class to be added (also called an archetype)
     * @param instanceEntityType the java class used as a database entity for storing persistent,
     *         player-specific state of the class instance
     * @param instanceType the java class used to represent player-specific state of the 5e class
     * @param entityMapper a mapper that can convert between the database entity and the class instance
     * @param <EntityType> the type of the database entity
     * @param <InstanceType> the type of the class instance
     */
    <EntityType extends ClassInstanceEntity, InstanceType extends ClassInstance> void registerClass(
            ClassArchetype<InstanceType> archetype, Class<EntityType> instanceEntityType,
            Class<InstanceType> instanceType, ClassInstanceMapper<EntityType, InstanceType> entityMapper);

    /**
     * Returns a list of all registered class archetypes. An archetype can then be used to create new instances for a
     * player character.
     *
     * @return a list of all registered class archetypes
     */
    List<ClassArchetype<?>> getRegisteredClasses();
}
