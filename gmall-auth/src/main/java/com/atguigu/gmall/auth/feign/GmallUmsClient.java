package com.atguigu.gmall.auth.feign;

import com.atguigu.gmall.ums.api.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author huima9527
 * @create 2021-02-26 20:32
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {
}