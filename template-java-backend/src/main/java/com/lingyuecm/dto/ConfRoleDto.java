package com.lingyuecm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The details of a role, including
 * <li>{@link #roleId}</li>
 * <li>{@link #roleName}</li>
 */
@Setter
@Getter
public class ConfRoleDto {
    /**
     * The primary key
     */
    private Integer roleId;
    /**
     * The role name
     */
    private String roleName;
}
