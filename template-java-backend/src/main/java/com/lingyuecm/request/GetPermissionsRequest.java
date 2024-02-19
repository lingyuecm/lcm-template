package com.lingyuecm.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request for getting permissions, including
 * <li>{@link #httpMethod}</li>
 * <li>{@link #permissionUrl}</li>
 */
@Setter
@Getter
public class GetPermissionsRequest {
    private String httpMethod;
    private String permissionUrl;
}
