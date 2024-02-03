package com.lingyuecm.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request for getting users, including
 * <li>{@link #criteria}</li>
 */
@Setter
@Getter
public class GetUsersRequest {
    /**
     * The criteria for searching by phone number, first name or last name
     */
    private String criteria;
}
