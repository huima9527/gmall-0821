package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.mapper.SkuAttrValueMapper;
import com.atguigu.gmall.pms.service.SkuAttrValueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author huima9527
 * @create 2021-02-24 20:11
 */
@SpringBootTest
class SkuAttrValueServiceImplTest {

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Test
    void queryPage() {
    }

    @Test
    void querySearchAttrValuesByCidAndSkuId() {
    }

    @Test
    void querySkuAttrValuesBySpuId() {
    }

    @Test
    void querySaleAttrsMappingSkuIdBySpuId() {
        System.out.println(this.skuAttrValueService.querySaleAttrsMappingSkuIdBySpuId(20l));
    }
}