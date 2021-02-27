package com.atguigu.gmall.index.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author huima9527
 * @create 2021-02-20 14:48
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
