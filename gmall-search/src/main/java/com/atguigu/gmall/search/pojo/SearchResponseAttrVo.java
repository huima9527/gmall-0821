package com.atguigu.gmall.search.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author huima9527
 * @create 2021-02-05 15:04
 */
@Data
public class SearchResponseAttrVo {
    private Long attrId;

    private String attrName;

    private List<String> attrValues;
}
