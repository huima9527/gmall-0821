package com.atguigu.gmall.item.service;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.item.feign.GmallPmsClient;
import com.atguigu.gmall.item.feign.GmallSmsClient;
import com.atguigu.gmall.item.feign.GmallWmsClient;
import com.atguigu.gmall.item.vo.ItemVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author huima9527
 * @create 2021-02-25 10:27
 */
@Service
public class ItemService {

    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private GmallWmsClient wmsClient;

    @Autowired
    private GmallSmsClient smsClient;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private TemplateEngine templateEngine;

//    public ItemVo load(Long skuId) {
//
//        ItemVo itemVo = new ItemVo();
//
//        // 获取sku相关信息
//        CompletableFuture<SkuEntity> skuFuture = CompletableFuture.supplyAsync(() -> {
//            ResponseVo<SkuEntity> skuEntityResponseVo = this.pmsClient.querySkuById(skuId);
//            SkuEntity skuEntity = skuEntityResponseVo.getData();
//            if (skuEntity == null) {
//                return null;
//            }
//            itemVo.setSkuId(skuId);
//            itemVo.setTitle(skuEntity.getTitle());
//            itemVo.setSubTitle(skuEntity.getSubtitle());
//            itemVo.setDefaultImage(skuEntity.getDefaultImage());
//            itemVo.setPrice(skuEntity.getPrice());
//            itemVo.setWeight(skuEntity.getWeight());
//            return skuEntity;
//        }, threadPoolExecutor);
//
//        // 设置分类信息
//        CompletableFuture<Void> catesFuture = skuFuture.thenAcceptAsync(skuEntity -> {
//            ResponseVo<List<CategoryEntity>> catesResponseVo = this.pmsClient.queryCategoriesByPid(skuEntity.getCategoryId());
//            List<CategoryEntity> categoryEntities = catesResponseVo.getData();
//            itemVo.setCategories(categoryEntities);
//        }, threadPoolExecutor);
//
//        // 品牌信息
//        CompletableFuture<Void> brandFuture = skuFuture.thenAcceptAsync(skuEntity -> {
//            ResponseVo<BrandEntity> brandEntityResponseVo = this.pmsClient.queryBrandById(skuEntity.getBrandId());
//            BrandEntity brandEntity = brandEntityResponseVo.getData();
//            if (brandEntity != null) {
//                itemVo.setBrandId(brandEntity.getId());
//                itemVo.setBrandName(brandEntity.getName());
//            }
//        }, threadPoolExecutor);
//
//        // spu信息
//        CompletableFuture<Void> spuFuture = skuFuture.thenAcceptAsync(skuEntity -> {
//            ResponseVo<SpuEntity> spuEntityResponseVo = this.pmsClient.querySpuById(skuEntity.getSpuId());
//            SpuEntity spuEntity = spuEntityResponseVo.getData();
//            if (spuEntity != null) {
//                itemVo.setSpuId(spuEntity.getId());
//                itemVo.setSpuName(spuEntity.getName());
//            }
//        }, threadPoolExecutor);
//
//        // sku的图片列表
//        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
//            ResponseVo<List<SkuImagesEntity>> imagesResponseVo = this.pmsClient.queryImagesBySkuId(skuId);
//            List<SkuImagesEntity> skuImagesEntities = imagesResponseVo.getData();
//            itemVo.setImages(skuImagesEntities);
//        }, threadPoolExecutor);
//
//        // sku营销信息
//        CompletableFuture<Void> salesFuture = CompletableFuture.runAsync(() -> {
//            ResponseVo<List<ItemSaleVo>> saleResponseVo = this.smsClient.querySalesBySkuId(skuId);
//            List<ItemSaleVo> sales = saleResponseVo.getData();
//            itemVo.setSales(sales);
//        }, threadPoolExecutor);
//
//        // 库存信息
//        CompletableFuture<Void> storeFuture = CompletableFuture.runAsync(() -> {
//            ResponseVo<List<WareSkuEntity>> wareResponseVo = this.wmsClient.queryWareSkusBySkuId(skuId);
//            List<WareSkuEntity> wareSkuEntities = wareResponseVo.getData();
//            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
//                itemVo.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
//            }
//        }, threadPoolExecutor);
//
//        // 所有销售属性
//        CompletableFuture<Void> saleAttrsFuture = skuFuture.thenAcceptAsync(skuEntity -> {
//            ResponseVo<List<SaleAttrValueVo>> saleAttrsResponseVo = this.pmsClient.querySkuAttrValuesBySpuId(skuEntity.getSpuId());
//            List<SaleAttrValueVo> saleAttrValueVos = saleAttrsResponseVo.getData();
//            itemVo.setSaleAttrs(saleAttrValueVos);
//        }, threadPoolExecutor);
//
//        // 当前sku的销售属性：{4: '暗夜黑', 5: '8G', 6: '128G'}
//        CompletableFuture<Void> saleAttrFuture = CompletableFuture.runAsync(() -> {
//            ResponseVo<List<SkuAttrValueEntity>> saleAttrResponseVo = this.pmsClient.querySkuAttrValueBySkuId(skuId);
//            List<SkuAttrValueEntity> skuAttrValueEntities = saleAttrResponseVo.getData();
//            if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
//                itemVo.setSaleAttr(skuAttrValueEntities.stream().collect(Collectors.toMap(SkuAttrValueEntity::getAttrId, SkuAttrValueEntity::getAttrValue)));
//            }
//        }, threadPoolExecutor);
//
//        // skuId和销售属性组合的映射关系
//        CompletableFuture<Void> mappingFuture = skuFuture.thenAcceptAsync(skuEntity -> {
//            ResponseVo<String> stringResponseVo = this.pmsClient.querySaleAttrsMappingSkuIdBySpuId(skuEntity.getSpuId());
//            String json = stringResponseVo.getData();
//            itemVo.setSkuJsons(json);
//        }, threadPoolExecutor);
//
//        // 海报信息
//        CompletableFuture<Void> descFuture = skuFuture.thenAcceptAsync(skuEntity -> {
//            ResponseVo<SpuDescEntity> spuDescEntityResponseVo = this.pmsClient.querySpuDescById(skuEntity.getSpuId());
//            SpuDescEntity spuDescEntity = spuDescEntityResponseVo.getData();
//            if (spuDescEntity != null && StringUtils.isNotBlank(spuDescEntity.getDecript())) {
//                itemVo.setSpuImages(Arrays.asList(StringUtils.split(spuDescEntity.getDecript(), ",")));
//            }
//        }, threadPoolExecutor);
//
//        // 分组及规格参数信息
//        CompletableFuture<Void> groupFuture = skuFuture.thenAcceptAsync(skuEntity -> {
//            ResponseVo<List<ItemGroupVo>> groupResponseVo = this.pmsClient.queryGroupsBySpuIdAndCid(skuEntity.getCategoryId(), skuEntity.getSpuId(), skuId);
//            List<ItemGroupVo> groupVos = groupResponseVo.getData();
//            itemVo.setGroups(groupVos);
//        }, threadPoolExecutor);
//
//        // 等待所有子任务执行完成，才能返回
//        CompletableFuture.allOf(catesFuture, brandFuture, spuFuture, imagesFuture, salesFuture, storeFuture,
//                saleAttrsFuture, saleAttrFuture, mappingFuture, descFuture, groupFuture).join();
//
//        return itemVo;
//    }

    public ItemVo load(Long skuId) {

        ItemVo itemVo = new ItemVo();

        // 根据skuId查询sku的信息1
        CompletableFuture<SkuEntity> skuFuture = CompletableFuture.supplyAsync(() -> {
            ResponseVo<SkuEntity> skuEntityResponseVo = this.pmsClient.querySkuById(skuId);
            SkuEntity skuEntity = skuEntityResponseVo.getData();
            if (skuEntity == null) {
                return null;
            }
            itemVo.setSkuId(skuId);
            itemVo.setTitle(skuEntity.getTitle());
            itemVo.setSubTitle(skuEntity.getSubtitle());
            itemVo.setPrice(skuEntity.getPrice());
            itemVo.setWeight(skuEntity.getWeight());
            itemVo.setDefaultImage(skuEntity.getDefaultImage());
            return skuEntity;
        }, threadPoolExecutor);

        // 根据cid3查询分类信息2
        CompletableFuture<Void> catesFuture = skuFuture.thenAcceptAsync(skuEntity -> {
            ResponseVo<List<CategoryEntity>> categoryResponseVo = this.pmsClient.queryCategoriesByCid3(skuEntity.getCategoryId());
            List<CategoryEntity> categoryEntities = categoryResponseVo.getData();
            itemVo.setCategories(categoryEntities);
        }, threadPoolExecutor);

        // 根据品牌的id查询品牌3
        CompletableFuture<Void> brandFuture = skuFuture.thenAcceptAsync(skuEntity -> {
            ResponseVo<BrandEntity> brandEntityResponseVo = this.pmsClient.queryBrandById(skuEntity.getBrandId());
            BrandEntity brandEntity = brandEntityResponseVo.getData();
            if (brandEntity != null) {
                itemVo.setBrandId(brandEntity.getId());
                itemVo.setBrandName(brandEntity.getName());
            }
        }, threadPoolExecutor);

        // 根据spuId查询spu4
        CompletableFuture<Void> spuFuture = skuFuture.thenAcceptAsync(skuEntity -> {
            ResponseVo<SpuEntity> spuEntityResponseVo = this.pmsClient.querySpuById(skuEntity.getSpuId());
            SpuEntity spuEntity = spuEntityResponseVo.getData();
            if (spuEntity != null) {
                itemVo.setSpuId(spuEntity.getId());
                itemVo.setSpuName(spuEntity.getName());
            }
        }, threadPoolExecutor);

        // 跟据skuId查询图片5
        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            ResponseVo<List<SkuImagesEntity>> skuImagesResponseVo = this.pmsClient.queryImagesBySkuId(skuId);
            List<SkuImagesEntity> skuImagesEntities = skuImagesResponseVo.getData();
            itemVo.setImages(skuImagesEntities);
        }, threadPoolExecutor);

        // 根据skuId查询sku营销信息6
        CompletableFuture<Void> salesFuture = CompletableFuture.runAsync(() -> {
            ResponseVo<List<ItemSaleVo>> salesResponseVo = this.smsClient.querySalesBySkuId(skuId);
            List<ItemSaleVo> sales = salesResponseVo.getData();
            itemVo.setSales(sales);
        }, threadPoolExecutor);

        // 根据skuId查询sku的库存信息7
        CompletableFuture<Void> storeFuture = CompletableFuture.runAsync(() -> {
            ResponseVo<List<WareSkuEntity>> wareSkuResponseVo = this.wmsClient.queryWareSkusBySkuId(skuId);
            List<WareSkuEntity> wareSkuEntities = wareSkuResponseVo.getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                itemVo.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
            }
        }, threadPoolExecutor);

        // 根据spuId查询spu下的所有sku的销售属性8
        CompletableFuture<Void> saleAttrsFuture = skuFuture.thenAcceptAsync(skuEntity -> {
            ResponseVo<List<SaleAttrValueVo>> saleAttrValueVoResponseVo = this.pmsClient.querySkuAttrValuesBySpuId(skuEntity.getSpuId());
            List<SaleAttrValueVo> saleAttrValueVos = saleAttrValueVoResponseVo.getData();
            itemVo.setSaleAttrs(saleAttrValueVos);
        }, threadPoolExecutor);


        // 当前sku的销售属性：{4: '暗夜黑', 5: '8G', 6: '128G'}
        CompletableFuture<Void> saleAttrFuture = CompletableFuture.runAsync(() -> {
            ResponseVo<List<SkuAttrValueEntity>> saleAttrResponseVo = this.pmsClient.querySkuAttrValueBySkuId(skuId);
            List<SkuAttrValueEntity> skuAttrValueEntities = saleAttrResponseVo.getData();
            if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
                itemVo.setSaleAttr(skuAttrValueEntities.stream().collect(Collectors.toMap(SkuAttrValueEntity::getAttrId, SkuAttrValueEntity::getAttrValue)));
            }
        }, threadPoolExecutor);

        // 根据spuId查询spu下的所有sku及销售属性的映射关系10
        CompletableFuture<Void> mappingFuture = skuFuture.thenAcceptAsync(skuEntity -> {
            ResponseVo<String> skusJsonResponseVo = this.pmsClient.querySaleAttrsMappingSkuIdBySpuId(skuEntity.getSpuId());
            String skusJson = skusJsonResponseVo.getData();
            itemVo.setSkuJsons(skusJson);
        }, threadPoolExecutor);

        // 根据spuId查询spu的海报信息11
        CompletableFuture<Void> descFuture = skuFuture.thenAcceptAsync(skuEntity -> {
            ResponseVo<SpuDescEntity> spuDescEntityResponseVo = this.pmsClient.querySpuDescById(skuEntity.getSpuId());
            SpuDescEntity spuDescEntity = spuDescEntityResponseVo.getData();
            if (spuDescEntity != null && StringUtils.isNotBlank(spuDescEntity.getDecript())) {
                String[] images = StringUtils.split(spuDescEntity.getDecript(), ",");
                itemVo.setSpuImages(Arrays.asList(images));
            }
        }, threadPoolExecutor);

        // 根据cid3 spuId skuId查询组及组下的规格参数及值 12
        CompletableFuture<Void> groupFuture = skuFuture.thenAcceptAsync(skuEntity -> {
            ResponseVo<List<ItemGroupVo>> groupResponseVo = this.pmsClient.queryGroupsBySpuIdAndCid(skuEntity.getCategoryId(), skuEntity.getSpuId(), skuId);
            List<ItemGroupVo> itemGroupVos = groupResponseVo.getData();
            itemVo.setGroups(itemGroupVos);
        }, threadPoolExecutor);

        // 等待所有子任务执行完成，才能返回
        CompletableFuture.allOf(catesFuture, brandFuture, spuFuture, imagesFuture, salesFuture, storeFuture,
                saleAttrsFuture, saleAttrFuture, mappingFuture, descFuture, groupFuture).join();

        return itemVo;
    }

    public void generateHtml(ItemVo itemVo){
        // 初始化上下对象，通过该对象给模板传递渲染所需要的数据
        Context context = new Context();
        context.setVariable("itemVo", itemVo);
        // 初始化文件流：jdk1.8的新语法
        try (PrintWriter printWriter = new PrintWriter("D:\\JAVA_dev\\code\\html\\" + itemVo.getSkuId() + ".html")) {
            // 通过模板引擎生成静态页面，1-模板的名称， 2-上下文对象  3-文件流
            this.templateEngine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
