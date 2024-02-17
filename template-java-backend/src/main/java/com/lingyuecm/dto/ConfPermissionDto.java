package com.lingyuecm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Details of a permission, including
 * <li>{@link #httpMethod}</li>
 * <li>{@link #permissionUrl}</li>
 */
@Setter
@Getter
public class ConfPermissionDto {
    private String httpMethod;
    private String permissionUrl;
}
