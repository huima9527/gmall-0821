package com.atguigu.gmall.ums.service.impl;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import com.atguigu.gmall.ums.mapper.UserMapper;
import com.atguigu.gmall.ums.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UserEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UserEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public Boolean checkData(String data, Integer type) {
        QueryWrapper<UserEntity> QueryWrapper = new QueryWrapper<>();
        switch (type){
            case 1: QueryWrapper.eq("username",data);break;
            case 2: QueryWrapper.eq("phone",data);break;
            case 3: QueryWrapper.eq("email",data);break;
            default: return null;
        }
        return this.count(QueryWrapper) == 0;
    }

    @Override
    public void register(UserEntity userEntity, String code) {
        //TODO: 1.根据手机号发送验证码  根据手机号查询reids中的code

        //2.生成盐salt
        String salt = StringUtils.substring(UUID.randomUUID().toString(), 0, 6);
        userEntity.setSalt(salt);

        //3.对密码加盐加密
        String password = DigestUtils.md5Hex(userEntity.getPassword()+salt);
        userEntity.setPassword(password);

        //4.新增用户
        userEntity.setNickname(userEntity.getUsername());
        userEntity.setLevelId(1L);
        userEntity.setIntegration(1000);
        userEntity.setGrowth(1000);
        userEntity.setStatus(1);
        userEntity.setCreateTime(new Date());
        this.save(userEntity);
        //TODO：5.删除redis中的短信验证码
    }

    @Override
    public UserEntity queryUser(String loginName, String password) {
        //先根据登录用户名查询用户，（查询盐）
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<UserEntity>().eq("username", loginName)
                .or().eq("phone", loginName)
                .or().eq("email", loginName);

        List<UserEntity> userEntities = this.list(wrapper);

        //判断用户是否为空
        if (CollectionUtils.isEmpty(userEntities)){
            return null;
        }

        //对用户输入的密码进行加盐
         for (UserEntity userEntity : userEntities) {
            password = DigestUtils.md5Hex(password+userEntity.getSalt());
            //比较数据库和加盐后的用户提交的密码
            if (StringUtils.equals(password,userEntity.getPassword())){
                return userEntity;
            }
        }
        return null;
    }

}