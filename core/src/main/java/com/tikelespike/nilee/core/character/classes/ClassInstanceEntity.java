package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * A database entity storing the persistent state of a D&D 5e {@link ClassInstance}. Different D&D classes will
 * implement their own subclasses of this class depending on the specific state they need to store.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "class_type")
public abstract class ClassInstanceEntity extends AbstractEntity {
}
