package com.atguigu.gmall.pms.api;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huima9527
 * @create 2021-02-02 14:47
 */
public interface GmallPmsApi {
    @PostMapping("pms/spu/json")
    public ResponseVo<List<SpuEntity>> querySpuByPageJson(@RequestBody PageParamVo paramVo);

    @GetMapping("pms/sku/spu/{pid}")
    public ResponseVo<List<SkuEntity>> querySkusBySpuId(@PathVariable("pid") Long pid);

    @GetMapping("pms/brand/{id}")
    public ResponseVo<BrandEntity> queryBrandById(@PathVariable("id") Long id);

    @GetMapping("pms/category/{id}")
    public ResponseVo<CategoryEntity> queryCategoryById(@PathVariable("id") Long id);

    @GetMapping("pms/category/parent/{parentId}")
    public ResponseVo<List<CategoryEntity>> queryCategoriesByPid(@PathVariable("parentId")Long pid);

    @GetMapping("pms/category/parent/withsubs/{pid}")
    public ResponseVo<List<CategoryEntity>> queryLvl2CatesWithSubsByPid(@PathVariable("pid")Long pid);

    @GetMapping("pms/skuattrvalue/search/{cid}")
    public ResponseVo<List<SkuAttrValueEntity>> querySearchAttrValuesByCidAndSkuId(
            @PathVariable("cid") long cid, @RequestParam("skuId") long skuId);

    @GetMapping("pms/skuattrvalue/sku/{skuId}")
    public ResponseVo<List<SkuAttrValueEntity>> querySkuAttrValueBySkuId(@PathVariable("skuId")Long skuId);

    @GetMapping("pms/skuattrvalue/spu/{spuId}")
    public ResponseVo<List<SaleAttrValueVo>> querySkuAttrValuesBySpuId(@PathVariable("spuId")Long spuId);

    @GetMapping("pms/skuattrvalue/spu/mapping/{spuId}")
    public ResponseVo<String> querySaleAttrsMappingSkuIdBySpuId(@PathVariable("spuId")Long spuId);

    @GetMapping("pms/spuattrvalue/search/{cid}")
    public ResponseVo<List<SpuAttrValueEntity>> querySearchAttrValuesByCidAndSpuId(
            @PathVariable("cid") Long cid, @RequestParam("spuId") Long spuId);

    @GetMapping("pms/spu/{id}")
    public ResponseVo<SpuEntity> querySpuById(@PathVariable("id") Long id);

    @GetMapping("pms/attrgroup/withattrvalues")
    public ResponseVo<List<ItemGroupVo>> queryGroupsBySpuIdAndCid(
            @RequestParam("spuId")Long spuId,
            @RequestParam("skuId")Long skuId,
            @RequestParam("cid")Long cid
    );

}
