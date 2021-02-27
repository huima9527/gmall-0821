package com.atguigu.gmall.search.pojo;

import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryBrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author huima9527
 * @create 2021-02-05 15:00
 */
@Data
public class SearchResponseVo {
    //品牌的渲染
    private List<BrandEntity> brands;

    //分类列表的渲染
    private List<CategoryEntity> categories;

    //规格参数的列表渲染
    private List<SearchResponseAttrVo> filters;

    //分页所需数据
    private Integer pageNum;
    private Integer pageSize;

    //总记录数
    private Long total;

    //当前页的具体数据
    private List<Goods> goodsList;
}
