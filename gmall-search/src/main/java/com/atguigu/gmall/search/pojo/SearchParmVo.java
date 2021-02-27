package com.atguigu.gmall.search.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author huima9527
 * @create 2021-02-04 22:08
 */
@Data
public class SearchParmVo {
    //检索关键字
    private String keyword;

    //品牌的过滤条件
    private List<Long> brandId;

    //分类的过滤条件
    private List<Long> categoryId;

    //规格参数的过滤["4:8G-12G","5:128G-256G"]
    private List<String> props;

    //排序字段：0-默认，依据得分降序排列，1-价格降序，2-价格圣墟，3-销量降序，4-新品降序
    private Integer sort = 0;

    //价格区间
    private Double priceFrom;
    private Double priceTo;

    //是否有货过滤
    private Boolean store;

    //分页参数
    private Integer pageNum = 1;
    private final Integer pageSize = 10;
}
