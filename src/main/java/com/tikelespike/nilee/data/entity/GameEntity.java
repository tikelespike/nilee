package com.tikelespike.nilee.data.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class GameEntity extends AbstractEntity {
}
