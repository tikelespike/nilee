package com.tikelespike.nilee.core.data.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * The highest level of abstraction for any database entities part of a polymorphic relationship. A database entity
 * cannot have a member of type {@link AbstractEntity}, since it is not its own entity, but it can have a member of
 * type {@link GameEntity}, which is its own entity. Think of it as the {@link Object} class for database entities.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class GameEntity extends AbstractEntity {
}
