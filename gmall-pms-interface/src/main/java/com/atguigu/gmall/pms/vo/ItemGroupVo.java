package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.vo.AttrValueVo;
import lombok.Data;

import java.util.List;

/**
 * @author huima9527
 * @create 2021-02-24 14:53
 */
@Data
public class ItemGroupVo {
    private Long id;
    private String name;
    private List<AttrValueVo> attrValue;
}
