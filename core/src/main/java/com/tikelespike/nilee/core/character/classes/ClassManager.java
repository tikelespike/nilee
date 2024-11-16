package com.tikelespike.nilee.core.character.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages available D&D 5e classes and provides a unified way to convert between arbitrary class instances of
 * registered class archetypes and their database entities.
 */
public class ClassManager implements ClassArchetypeManager, UnifiedClassInstanceMapper {
    // maps entity type to mapper
    private final Map<Class<? extends ClassInstanceEntity>, ClassInstanceMapper<?, ?>> entityTypeToMapper =
            new HashMap<>();

    // maps instance object type to mapper
    private final Map<Class<? extends ClassInstance>, ClassInstanceMapper<?, ?>> instanceTypeToMapper = new HashMap<>();

    private final List<ClassArchetype<?>> registeredClasses = new ArrayList<>();


    @Override
    public <EntityType extends ClassInstanceEntity, InstanceType extends ClassInstance> void registerClass(
            ClassArchetype<InstanceType> archetype, Class<EntityType> instanceEntityType,
            Class<InstanceType> instanceType, ClassInstanceMapper<EntityType, InstanceType> entityMapper) {
        entityTypeToMapper.put(instanceEntityType, entityMapper);
        instanceTypeToMapper.put(instanceType, entityMapper);
        registeredClasses.add(archetype);
    }

    @Override
    public List<ClassArchetype<?>> getRegisteredClasses() {
        return new ArrayList<>(registeredClasses);
    }

    @Override
    public ClassInstance toBusinessObject(ClassInstanceEntity entity) {
        if (!entityTypeToMapper.containsKey(entity.getClass())) {
            throw new IllegalArgumentException("No mapper registered for " + entity.getClass());
        }
        // Since the register method is generic in such a way that only permits key-value pairs of the correct type, the
        // cast is safe (we know that the mapper entity type matches entity.getClass()).
        //noinspection unchecked
        return ((ClassInstanceMapper<ClassInstanceEntity, ClassInstance>) entityTypeToMapper.get(
                entity.getClass())).toBusinessObject(entity);
    }

    @Override
    public ClassInstanceEntity toEntity(ClassInstance businessObject) {
        if (!instanceTypeToMapper.containsKey(businessObject.getClass())) {
            throw new IllegalArgumentException("No mapper registered for " + businessObject.getClass());
        }
        // Since the register method is generic in such a way that only permits key-value pairs of the correct type, the
        // cast is safe (we know that the mapper business object type matches businessObject.getClass()).
        //noinspection unchecked
        return ((ClassInstanceMapper<ClassInstanceEntity, ClassInstance>) instanceTypeToMapper.get(
                businessObject.getClass())).toEntity(businessObject);
    }
}
