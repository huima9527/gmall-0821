package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuEntity;
import com.atguigu.gmall.pms.mapper.SkuMapper;
import com.atguigu.gmall.pms.service.SkuAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class GmallPmsApplicationTests {
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Test
    void contextLoads() {
        //查询出spu下所有的sku
        List<SkuEntity> skuEntities = this.skuMapper.selectList(new QueryWrapper<SkuEntity>().eq("spu_id", 22));

        List<Long> skuIds = skuEntities.stream().map(SkuEntity::getId).collect(Collectors.toList());

        //查询skuid对应的销售属性
        List<SkuAttrValueEntity> skuAttrValueEntities = skuAttrValueService.list(new QueryWrapper<SkuAttrValueEntity>().in("sku_id", skuIds));

        Map<Long, List<SkuAttrValueEntity>> map = skuAttrValueEntities.stream().collect(Collectors.groupingBy(t -> t.getAttrId()));

        System.out.println(map);
//        {3=[SkuAttrValueEntity(id=88, skuId=30, attrId=3, attrName=机身颜色, attrValue=金色, sort=null), SkuAttrValueEntity(id=91, skuId=31, attrId=3, attrName=机身颜色, attrValue=金色, sort=null)],
//        4=[SkuAttrValueEntity(id=89, skuId=30, attrId=4, attrName=运行内存, attrValue=12G, sort=null), SkuAttrValueEntity(id=92, skuId=31, attrId=4, attrName=运行内存, attrValue=12G, sort=null)],
//        5=[SkuAttrValueEntity(id=90, skuId=30, attrId=5, attrName=机身存储, attrValue=128G, sort=null), SkuAttrValueEntity(id=93, skuId=31, attrId=5, attrName=机身存储, attrValue=256G, sort=null)]}

    }

}
