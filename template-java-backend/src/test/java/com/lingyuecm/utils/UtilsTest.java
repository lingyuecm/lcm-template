package com.lingyuecm.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UtilsTest {
    /**
     * Generates UUIDs for further use
     */
    @Test
    public void generateUuid() {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
    }
}
