package com.lingyuecm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ConfPermission {
    private Integer permissionId;
    private String httpMethod;
    private String permissionUrl;
    private Integer status;
    private Long createdBy;
    private Date timeCreated;
    private Long updatedBy;
    private Date timeUpdated;
}
