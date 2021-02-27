package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author huima9527
 * @create 2021-02-25 11:45
 */
@SpringBootTest
class CategoryControllerTest {
    @Autowired
    private CategoryController categoryController;

    @Test
    void queryCategoriesByPid() {
        ResponseVo<List<CategoryEntity>> listResponseVo = this.categoryController.queryCategoriesByPid(0l);
        System.out.println(listResponseVo.getData());
    }
}