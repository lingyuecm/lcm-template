package com.lingyuecm.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * A paged list containing one page of data
 * @param <T> The data type carried
 */
@Getter
public final class PagedList<T> {
    /**
     * The total count of items
     */
    private final long totalCount;
    /**
     * The list of data contained in this page
     */
    private final List<T> dataList;

    private PagedList() {
        this(0L, new ArrayList<>());
    }

    private PagedList(long totalCount, List<T> dataList) {
        this.totalCount = totalCount;
        this.dataList = dataList;
    }

    /**
     * Called when there is data fetched
     * @param totalCount The total count of the data items found
     * @param dataList The data on this page
     */
    public static <T> PagedList<T> paginated(long totalCount, List<T> dataList) {
        return new PagedList<>(totalCount, dataList);
    }

    /**
     * Called when no data fetched
     */
    public static <T> PagedList<T> empty() {
        return new PagedList<>();
    }
}
