package com.atguigu.gmall.search.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author huima9527
 * @create 2021-02-03 20:21
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
