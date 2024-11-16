package com.tikelespike.nilee.core.character.classes;

/**
 * Example 5e class with dexterity and wisdom saving throw proficiencies.
 */
public class ExampleClassInstance extends AbstractClassInstance {

    private String exampleState;

    /**
     * Creates a new example class instance.
     *
     * @param classType the archetype of this class specifying its rules and abilities that are not
     *         character-specific.
     */
    protected ExampleClassInstance(AbstractClassArchetype<ExampleClassInstance> classType) {
        super(classType);
    }

    /**
     * Creates a database entity mapper for this class instance.
     *
     * @param archetype the archetype of this class specifying its rules and abilities that are not
     *         character-specific
     *
     * @return a new mapper mapping between instances of this D&D 5e class instance and its database entity
     */
    public static ClassInstanceMapper<ExampleClassInstanceEntity, ExampleClassInstance> getMapper(
            AbstractClassArchetype<ExampleClassInstance> archetype) {
        return new ClassInstanceMapper<>() {
            @Override
            public ExampleClassInstance toBusinessObject(ExampleClassInstanceEntity entity) {
                ExampleClassInstance exampleClass = new ExampleClassInstance(archetype);
                exampleClass.exampleState = entity.getExampleState();
                return exampleClass;
            }

            @Override
            public ExampleClassInstanceEntity toEntity(ExampleClassInstance businessObject) {
                ExampleClassInstanceEntity exampleClassEntity = new ExampleClassInstanceEntity();
                exampleClassEntity.setExampleState(businessObject.exampleState);
                return exampleClassEntity;
            }
        };
    }

}
