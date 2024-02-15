package com.lingyuecm.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request for getting role list, including
 * <li>{@link #criteria}</li>
 */
@Setter
@Getter
public class GetRolesRequest {
    private String criteria;
}
