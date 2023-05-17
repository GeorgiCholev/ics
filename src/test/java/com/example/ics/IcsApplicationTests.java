package com.example.ics;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IcsApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("Test case Log");
    }

    @Test
    public void testBoundToFail() {
        Assertions.assertEquals(2, 6);
    }

}
