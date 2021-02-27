package com.atguigu.gmall.pms.vo;

import lombok.Data;

import java.util.Set;

/**
 * @author huima9527
 * @create 2021-02-24 14:54
 */
@Data
public class SaleAttrValueVo {
    private Long attrId;
    private String attrName;
    private Set<String> attrValues;
}
