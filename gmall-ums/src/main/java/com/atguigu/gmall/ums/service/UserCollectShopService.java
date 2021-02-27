package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.ums.entity.UserCollectShopEntity;

import java.util.Map;

/**
 * 关注店铺表
 *
 * @author athuima
 * @email tianhaosike@163.com
 * @date 2021-02-26 14:39:56
 */
public interface UserCollectShopService extends IService<UserCollectShopEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

