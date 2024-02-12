package com.lingyuecm.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Request for granting roles to the user, including
 * <li>{@link #roleIds}</li>
 */
@Setter
@Getter
public class GrantRolesRequest {
    /**
     * The primary keys to the roles to grant to the user
     */
    @NotNull
    private List<Integer> roleIds;
}
