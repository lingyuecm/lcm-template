package com.lingyuecm.model;

import com.lingyuecm.common.PageData;
import com.lingyuecm.common.PagedList;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModelTest {
    @Test
    public void testBizUser() {
        BizUser u = new BizUser();

        assertDoesNotThrow(() -> u.setUserId(1L));
        assertDoesNotThrow(() -> u.setPhoneNo(""));
        assertDoesNotThrow(() -> u.setLoginPwd(""));
        assertDoesNotThrow(() -> u.setRealName(""));
        assertDoesNotThrow(() -> u.setStatus(1));
        assertDoesNotThrow(() -> u.setCreatedBy(1L));
        assertDoesNotThrow(() -> u.setTimeCreated(new Date()));
        assertDoesNotThrow(() -> u.setUpdatedBy(1L));
        assertDoesNotThrow(() -> u.setTimeUpdated(new Date()));

        assertNotNull(u.getUserId());
        assertNotNull(u.getPhoneNo());
        assertNotNull(u.getLoginPwd());
        assertNotNull(u.getRealName());
        assertNotNull(u.getStatus());
        assertNotNull(u.getCreatedBy());
        assertNotNull(u.getTimeCreated());
        assertNotNull(u.getUpdatedBy());
        assertNotNull(u.getTimeUpdated());
    }

    @Test
    public void testPageData() {
        int pageNo = 2;
        int pageSize = 10;
        PageData p = new PageData();

        assertDoesNotThrow(() -> p.setPageNo(pageNo));
        assertDoesNotThrow(() -> p.setPageSize(pageSize));

        assertEquals(pageSize, p.getPageSize());
        assertEquals((pageNo - 1) * pageSize, p.getOffset());
    }

    @Test
    public void testPagedList() {
        long totalCount = 12L;
        int pageSize = 10;
        String dataItemPrefix = "dataItem";
        List<String> dataList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("00");
        for (int m = 0; m < pageSize; m++) {
            dataList.add(dataItemPrefix + df.format(m));
        }

        PagedList<String> list = PagedList.paginated(totalCount, dataList);

        assertEquals(totalCount, list.getTotalCount());
        for (int m = 0; m < pageSize; m++) {
            assertEquals(dataList.get(m), list.getDataList().get(m));
        }

        PagedList<String> emptyList = PagedList.empty();
        assertEquals(0L, emptyList.getTotalCount());
        assertNotNull(emptyList.getDataList());
        assertEquals(0, emptyList.getDataList().size());
    }

    @Test
    public void testConfRole() {
        ConfRole r = new ConfRole();

        assertDoesNotThrow(() -> r.setRoleId(1));
        assertDoesNotThrow(() -> r.setRoleName(""));
        assertDoesNotThrow(() -> r.setStatus(1));
        assertDoesNotThrow(() -> r.setCreatedBy(1L));
        assertDoesNotThrow(() -> r.setTimeCreated(new Date()));
        assertDoesNotThrow(() -> r.setUpdatedBy(1L));
        assertDoesNotThrow(() -> r.setTimeUpdated(new Date()));

        assertNotNull(r.getRoleId());
        assertNotNull(r.getRoleName());
        assertNotNull(r.getStatus());
        assertNotNull(r.getCreatedBy());
        assertNotNull(r.getTimeCreated());
        assertNotNull(r.getUpdatedBy());
        assertNotNull(r.getTimeUpdated());
    }

    @Test
    public void testConfPermission() {
        ConfPermission p = new ConfPermission();

        assertDoesNotThrow(() -> p.setPermissionId(1));
        assertDoesNotThrow(() -> p.setHttpMethod(""));
        assertDoesNotThrow(() -> p.setPermissionUrl(""));
        assertDoesNotThrow(() -> p.setStatus(1));
        assertDoesNotThrow(() -> p.setCreatedBy(1L));
        assertDoesNotThrow(() -> p.setTimeCreated(new Date()));
        assertDoesNotThrow(() -> p.setUpdatedBy(1L));
        assertDoesNotThrow(() -> p.setTimeUpdated(new Date()));

        assertNotNull(p.getPermissionId());
        assertNotNull(p.getHttpMethod());
        assertNotNull(p.getPermissionUrl());
        assertNotNull(p.getStatus());
        assertNotNull(p.getCreatedBy());
        assertNotNull(p.getTimeCreated());
        assertNotNull(p.getUpdatedBy());
        assertNotNull(p.getTimeUpdated());
    }
}
