package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author huima9527
 * @create 2021-02-24 15:34
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
