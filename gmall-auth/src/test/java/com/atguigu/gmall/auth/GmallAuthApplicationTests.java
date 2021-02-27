package com.atguigu.gmall.auth;

import com.atguigu.gmall.auth.feign.GmallUmsClient;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallAuthApplicationTests {

    @Autowired
    private GmallUmsClient umsClient;

    @Test
    void contextLoads() {
        ResponseVo<UserEntity> userEntityResponseVo = this.umsClient.queryUser("loser", "123456");
        UserEntity data = userEntityResponseVo.getData();
        System.out.println(data);
    }

}
