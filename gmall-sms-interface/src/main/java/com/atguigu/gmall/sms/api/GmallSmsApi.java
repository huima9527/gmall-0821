package com.atguigu.gmall.sms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author huima9527
 * @create 2021-01-21 14:20
 */
public interface GmallSmsApi {
    @PostMapping("sms/skubounds/save")
    public ResponseVo saveSales(@RequestBody SkuSaleVo skuSaleVo);
}
