package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.service.AttrGroupService;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author huima9527
 * @create 2021-02-24 21:24
 */
@SpringBootTest
class AttrGroupServiceImplTest {

    @Autowired
    private AttrGroupService attrGroupService;

    @Test
    void queryGroupsBySpuIdAndCid() {
        List<ItemGroupVo> itemGroupVos = this.attrGroupService.queryGroupWithAttrValuesBy(225l, 20l, 27l);
        System.out.println(itemGroupVos);
    }
}