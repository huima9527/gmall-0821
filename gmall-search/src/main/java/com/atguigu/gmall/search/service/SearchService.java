package com.atguigu.gmall.search.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchParmVo;
import com.atguigu.gmall.search.pojo.SearchResponseAttrVo;
import com.atguigu.gmall.search.pojo.SearchResponseVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author huima9527
 * @create 2021-02-04 22:29
 */
@Service
public class SearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public SearchResponseVo search(SearchParmVo searchParmVo){
        try {
            SearchRequest searchRequest = new SearchRequest(new String[]{"goods"}, this.BuilderDsl(searchParmVo));
            SearchResponse search = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            return this.parseResult(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SearchResponseVo parseResult(SearchResponse response){
        SearchResponseVo responseVo = new SearchResponseVo();
        //1.解析hits
        SearchHits hits = response.getHits();

        responseVo.setPageNum(20);
        responseVo.setPageSize(5);

        //总记录数
        long totalHits = hits.getTotalHits();
        responseVo.setTotal(totalHits);
        //当前页的数据
        SearchHit[] hitsHits = hits.getHits();
        List<Goods> goodsList = Stream.of(hitsHits).map(hitsHit -> {
            String json = hitsHit.getSourceAsString();
            Goods goods = JSON.parseObject(json, Goods.class);
            //获取高亮标题，覆盖掉_source中的普通标题
            Map<String, HighlightField> highlightFields = hitsHit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("title");
            Text[] fragments = highlightField.getFragments();
            String title = fragments[0].string();
            goods.setTitle(title);
            return goods;
        }).collect(Collectors.toList());
        //TODO:具体解析
        responseVo.setGoodsList(goodsList);


        //2.解析aggregations，获取到品牌列表、分类列表、规格参数列表
        //  把聚合结果集以map的形式解析：key-聚合名称，value-聚合的内容
        Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
        //2.1获取品牌
        ParsedLongTerms brandIdAgg = (ParsedLongTerms)aggregationMap.get("brandIdAgg");
        List<? extends Terms.Bucket> buckets = brandIdAgg.getBuckets();

        if (!CollectionUtils.isEmpty(buckets)){
            List<BrandEntity> brandEntities = buckets.stream().map(bucket -> {
                BrandEntity brandEntity = new BrandEntity();
                //外层桶的key就是品牌的ID
                Long keyAsNumber = ((Terms.Bucket) bucket).getKeyAsNumber().longValue();
                brandEntity.setId(keyAsNumber);

                //获取到桶里的子聚合：品牌名自聚合，品牌logo子聚合
                Map<String, Aggregation> stringAggregationMap = ((Terms.Bucket) bucket).getAggregations().asMap();
                ParsedStringTerms brandNameAgg = (ParsedStringTerms) stringAggregationMap.get("brandNameAgg");
                //每个品牌子聚合中只有一个桶
                String brandname = brandNameAgg.getBuckets().get(0).getKeyAsString();
                brandEntity.setName(brandname);

                //获取品牌logo子聚合,每个logo的子聚合也是仅有一个桶
                ParsedStringTerms brandLogo = (ParsedStringTerms) stringAggregationMap.get("brandLogo");
                String brandlogo = brandLogo.getBuckets().get(0).getKeyAsString();
                brandEntity.setLogo(brandlogo);

                return brandEntity;
            }).collect(Collectors.toList());
            responseVo.setBrands(brandEntities);
        }


        //2.2获取分类
        ParsedLongTerms categoryIdAgg = (ParsedLongTerms)aggregationMap.get("categroyIdAgg");
        List<? extends Terms.Bucket> categoryIdAggBuckets = categoryIdAgg.getBuckets();
        if (!CollectionUtils.isEmpty(categoryIdAggBuckets)){
            List<CategoryEntity> categoryEntities = categoryIdAggBuckets.stream().map(bucket -> {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setId(((Terms.Bucket) bucket).getKeyAsNumber().longValue());
                //获取分类ID的子聚合获取分类名称
                ParsedStringTerms categroyNameAgg = (ParsedStringTerms)((Terms.Bucket) bucket).getAggregations().get("categroyNameAgg");
                categoryEntity.setName(categroyNameAgg.getBuckets().get(0).getKeyAsString());
                return categoryEntity;
            }).collect(Collectors.toList());
            responseVo.setCategories(categoryEntities);
        }

        //2.3获取规格参数
        //获取到规格参数的嵌套聚合
        ParsedNested attrAgg = (ParsedNested)aggregationMap.get("attrAgg");
        //获取嵌套聚合中的attrId聚合
        ParsedLongTerms attrIdAgg = (ParsedLongTerms)attrAgg.getAggregations().get("attrIdAgg");
        //获取attrId聚合的桶集合，获取所有检索类型的规格参数
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
        //有些商品或者是有些关键字可能没有检索类型的规格参数
        if (!CollectionUtils.isEmpty(attrIdAggBuckets)){
            //把attrId中的桶集合转化成List<SearchResponseAttrVo>
            List<SearchResponseAttrVo> SearchResponseVos = attrIdAggBuckets.stream().map(bucket -> {
                //把每个桶转化为SearchResponseAttrVo
                SearchResponseAttrVo ResponseAttrVo = new SearchResponseAttrVo();
                //每个桶中的key就是attrId
                ResponseAttrVo.setAttrId(((Terms.Bucket) bucket).getKeyAsNumber().longValue());
                //获取子聚合，获取attrName和attrValue
                Map<String, Aggregation> attrMap = ((Terms.Bucket) bucket).getAggregations().getAsMap();

                //获取attrName的子聚合
                ParsedStringTerms attrNameAgg = (ParsedStringTerms)attrMap.get("attrNameAgg");
                ResponseAttrVo.setAttrName(attrNameAgg.getBuckets().get(0).getKeyAsString());

                //获取attrvalue的子聚合
                ParsedStringTerms attrValueAgg = (ParsedStringTerms)attrMap.get("attrValueAgg");
                List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();
                if (!CollectionUtils.isEmpty(attrValueAggBuckets)){
                    List<String> attrvalues = attrValueAggBuckets.stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
                    ResponseAttrVo.setAttrValues(attrvalues);
                }
                return ResponseAttrVo;
            }).collect(Collectors.toList());
            responseVo.setFilters(SearchResponseVos);
        }



        return responseVo;
    }

    private SearchSourceBuilder BuilderDsl(SearchParmVo paramVo){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        String keyword = paramVo.getKeyword();
        if (StringUtils.isBlank(keyword)){

            //TODO：打广告
            return sourceBuilder;
        }
        //1.构建检索条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        sourceBuilder.query(boolQueryBuilder);
        //1.1构建匹配搜索条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("title",keyword).operator(Operator.AND));

        // 1.2. 构建过滤条件
        // 1.2.1. 构建品牌的过滤
        List<Long> brandId = paramVo.getBrandId();
        if (!CollectionUtils.isEmpty(brandId)){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", brandId));
        }

        // 1.2.2. 构建分类的过滤
        List<Long> categoryId = paramVo.getCategoryId();
        if (!CollectionUtils.isEmpty(categoryId)){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("categoryId", categoryId));
        }

        //1.2.3构建价格区间过滤
        Double priceFrom = paramVo.getPriceFrom();   //起始价格
        Double priceTo = paramVo.getPriceTo();       //终止价格
        if (priceFrom != null || priceTo != null){  //至少有一个不为空才需要添加价格区间的过滤
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            if (priceFrom != null){
                rangeQuery.gte(priceFrom);
            }
            if (priceTo != null){
                rangeQuery.lte(priceTo);
            }
            boolQueryBuilder.filter(rangeQuery);
        }

        //1.2.4构建是否有货的过滤
        Boolean store = paramVo.getStore();
        if (store != null && store){
            boolQueryBuilder.filter(QueryBuilders.termQuery("store",store));
        }

        //1.25构建规格参数的嵌套过滤
        //["4:8G-12G","5:128G-256G"]
        List<String> props = paramVo.getProps();
        if (!CollectionUtils.isEmpty(props)){
            props.forEach(prop -> {//每一个prop 4:8G-12G
                String[] attr = StringUtils.split(prop, ":");
                if (attr != null && attr.length == 2){
                    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                    //分割后的第一位就是attrID
                    boolQuery.must(QueryBuilders.termQuery("searchAtts.attrId",attr[0]));
                    //分割后的第二位是8G-12G
                    String[] attrValues = StringUtils.split(attr[1], "-");
                    boolQuery.must(QueryBuilders.termsQuery("searchAtts.attrValue",attrValues));

                    //每一个prop就会对应一个嵌套过滤：1-对应嵌套过滤中的path  2-嵌套过滤中的query  3-得分模式
                    boolQueryBuilder.filter(QueryBuilders.nestedQuery("searchAtts",boolQuery, ScoreMode.None));
                }
            });
        }

        //2.构建排序条件
        Integer sort = paramVo.getSort();
        switch (sort){
            case 1: sourceBuilder.sort("price", SortOrder.DESC); break;
            case 2: sourceBuilder.sort("price", SortOrder.ASC); break;
            case 3: sourceBuilder.sort("sales", SortOrder.DESC); break;
            case 4: sourceBuilder.sort("createTime", SortOrder.DESC); break;
            default: sourceBuilder.sort("_score",SortOrder.DESC); break;
        }

        //3.构建分页条件
        Integer pageNum = paramVo.getPageNum();
        Integer pageSize = paramVo.getPageSize();
        sourceBuilder.from((pageNum - 1) * pageSize);

        //4.构建高亮字段
        sourceBuilder.highlighter(new HighlightBuilder().field("title")
                .preTags("<font style='color=red;'>")
                .postTags("</font>"));

        //5.构建聚合
        //5.1构建品牌聚合
        sourceBuilder.aggregation(
                AggregationBuilders.terms("brandIdAgg").field("brandId")
                .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName"))
                .subAggregation(AggregationBuilders.terms("brandLogo").field("logo"))
        );
        //5.2构建分类聚合
        sourceBuilder.aggregation(
                AggregationBuilders.terms("categroyIdAgg").field("categoryId")
                .subAggregation(AggregationBuilders.terms("categroyNameAgg").field("categoryName"))
        );
        //5.3构建规格参数的聚合
        sourceBuilder.aggregation(
                AggregationBuilders.nested("attrAgg","searchAtts")
                .subAggregation(AggregationBuilders.terms("attrIdAgg").field("searchAtts.attrId")
                .subAggregation(AggregationBuilders.terms("attrNameAgg").field("searchAtts.attrName"))
                .subAggregation(AggregationBuilders.terms("attrValueAgg").field("searchAtts.attrValue"))
                ));

        //6.构建结果集过滤
        sourceBuilder.fetchSource(new String[]{"skuId","defaultImage","price","title","subTitle"},null);
        System.out.println(sourceBuilder);
        return sourceBuilder;
    }
}
