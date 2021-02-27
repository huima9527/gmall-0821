package com.atguigu.gmall.search.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author huima9527
 * @create 2021-02-03 20:22
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
