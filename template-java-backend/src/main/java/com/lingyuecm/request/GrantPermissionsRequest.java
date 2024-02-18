package com.lingyuecm.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Request for Granting permissions to the role, including
 * <li>{@link #permissionIds}</li>
 */
@Setter
@Getter
public class GrantPermissionsRequest {
    private List<Integer> permissionIds;
}
