package com.atguigu.gmall.ums;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallUmsApplicationTests {

    @Test
    void contextLoads() {
        String password = DigestUtils.md5Hex(7788+"d0f363");
        System.out.println(password);
    }

}
