package com.atguigu.gmall.ums.mapper;

import com.atguigu.gmall.ums.entity.UserLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户登陆记录表
 * 
 * @author athuima
 * @email tianhaosike@163.com
 * @date 2021-02-26 14:39:56
 */
@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLogEntity> {
	
}
