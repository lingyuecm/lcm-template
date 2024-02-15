package com.lingyuecm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BizUser {
    private Long userId;
    private String phoneNo;
    private String loginPwd;
    private String realName;
    private Integer status;
    private Long createdBy;
    private Date timeCreated;
    private Long updatedBy;
    private Date timeUpdated;
}
