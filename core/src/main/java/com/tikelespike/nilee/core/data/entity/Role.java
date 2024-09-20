package com.tikelespike.nilee.core.data.entity;

/**
 * Permission group that is assigned to users, granting them different sets of permissions.
 */
public enum Role {
    /**
     * Default user role, no special permissions.
     */
    USER,

    /**
     * Admin role with additional permissions.
     */
    ADMIN
}
