package com.atguigu.gmall.index.service;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author huima9527
 * @create 2021-02-25 11:36
 */
@SpringBootTest
class IndexServiceTest {
    @Autowired
    private IndexService indexService;

    @Test
    void queryLv1Categories() {
        List<CategoryEntity> categoryEntities = this.indexService.queryLv1Categories();
        System.out.println(categoryEntities);
    }
}