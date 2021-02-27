package com.atguigu.gmall.pms.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author huima9527
 * @create 2021-02-24 19:30
 */
@SpringBootTest
class SkuAttrValueMapperTest {
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Test
    void querySaleAttrsMappingSkuId() {
        List<Map<String, Object>> maps = this.skuAttrValueMapper.querySaleAttrsMappingSkuId(Arrays.asList(97l,98l,99l));
        System.out.println(maps);
    }
}