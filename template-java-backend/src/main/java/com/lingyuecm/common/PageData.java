package com.lingyuecm.common;

import lombok.Getter;
import lombok.Setter;

/**
 * The pagination data
 */
@Setter
public class PageData {
    private int pageNo;
    @Getter
    private int pageSize;

    /**
     * Calculates the offset that will be used in the SQLs
     */
    public int getOffset() {
        return (this.pageNo - 1) * this.pageSize;
    }
}
