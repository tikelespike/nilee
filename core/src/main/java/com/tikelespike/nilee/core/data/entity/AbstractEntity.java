package com.tikelespike.nilee.core.data.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;

/**
 * Base class for all database entities. Provides an id and version field. By default, entities are equal if and only if
 * they have the same id.
 */
@MappedSuperclass
public abstract class AbstractEntity {

    private static final int INITIAL_ID = 1000;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    // The initial value is to account for data.sql demo data ids
    @SequenceGenerator(name = "idgenerator", initialValue = INITIAL_ID)
    private Long id;

    @Version
    private int version;

    /**
     * @return the unique id of this entity. May be null if the entity has not been persisted yet.
     */
    public Long getId() {
        return id;
    }

    /**
     * Do not use this method. Should only be used by JPA.
     *
     * @param id the new id of this entity.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Should be used by JPA only.
     *
     * @return the version of this entity
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the new version of this entity
     */
    public final void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractEntity other)) {
            return false; // null or other class
        }

        if (getId() != null) {
            return getId().equals(other.getId());
        }
        return super.equals(other);
    }
}
