package com.tikelespike.nilee.app;

import com.tikelespike.nilee.core.character.classes.ClassArchetype;
import com.tikelespike.nilee.core.character.classes.ClassArchetypeManager;
import com.tikelespike.nilee.core.character.classes.ClassInstance;
import com.tikelespike.nilee.core.character.classes.ClassInstanceEntity;
import com.tikelespike.nilee.core.character.classes.ClassInstanceMapper;
import com.tikelespike.nilee.core.character.classes.ClassManager;
import com.tikelespike.nilee.core.character.classes.ExampleClassArchetype;
import com.tikelespike.nilee.core.character.classes.ExampleClassInstance;
import com.tikelespike.nilee.core.character.classes.ExampleClassInstanceEntity;
import com.tikelespike.nilee.core.character.classes.UnifiedClassInstanceMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Wraps the {@link ClassManager} to provide a Spring-managed bean.
 */
@Component
public class ClassManagerWrapper implements ClassArchetypeManager, UnifiedClassInstanceMapper {

    private final ClassArchetypeManager classArchetypeManager;
    private final UnifiedClassInstanceMapper unifiedClassInstanceMapper;

    /**
     * Creates a new ClassManagerWrapper.
     */
    public ClassManagerWrapper() {
        ClassManager wrapped = new ClassManager();
        this.classArchetypeManager = wrapped;
        this.unifiedClassInstanceMapper = wrapped;
        ExampleClassArchetype exampleArchetype = new ExampleClassArchetype();
        registerClass(exampleArchetype, ExampleClassInstanceEntity.class, ExampleClassInstance.class,
                ExampleClassInstance.getMapper(exampleArchetype));
    }

    @Override
    public <EntityType extends ClassInstanceEntity, InstanceType extends ClassInstance> void registerClass(
            ClassArchetype<InstanceType> archetype, Class<EntityType> instanceEntityType,
            Class<InstanceType> instanceType, ClassInstanceMapper<EntityType, InstanceType> entityMapper) {
        classArchetypeManager.registerClass(archetype, instanceEntityType, instanceType, entityMapper);
    }

    @Override
    public List<ClassArchetype<?>> getRegisteredClasses() {
        return classArchetypeManager.getRegisteredClasses();
    }

    @Override
    public ClassInstance toBusinessObject(ClassInstanceEntity entity) {
        return unifiedClassInstanceMapper.toBusinessObject(entity);
    }

    @Override
    public ClassInstanceEntity toEntity(ClassInstance businessObject) {
        return unifiedClassInstanceMapper.toEntity(businessObject);
    }
}
