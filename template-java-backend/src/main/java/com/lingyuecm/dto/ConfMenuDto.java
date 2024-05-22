package com.lingyuecm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfMenuDto {
    private Integer menuId;
    private String menuTitle;
    private String menuUrl;
    private List<ConfMenuDto> children;
}
