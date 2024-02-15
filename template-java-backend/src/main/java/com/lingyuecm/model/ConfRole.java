package com.lingyuecm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ConfRole {
    private Integer roleId;
    private String roleName;
    private Integer status;
    private Long createdBy;
    private Date timeCreated;
    private Long updatedBy;
    private Date timeUpdated;
}
