package com.atguigu.gmall.ums.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import com.atguigu.gmall.ums.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表
 *
 * @author athuima
 * @email tianhaosike@163.com
 * @date 2021-02-26 14:39:56
 */
@Api(tags = "用户表 管理")
@RestController
@RequestMapping("ums/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/query")
    @ApiOperation("用户登录")
    public ResponseVo<UserEntity> queryUser(@RequestParam("loginName") String loginName,@RequestParam("password")String password){
        UserEntity userEntity = this.userService.queryUser(loginName,password);
        return ResponseVo.ok(userEntity);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public ResponseVo register(UserEntity userEntity,@RequestParam("code")String code){
        this.userService.register(userEntity,code);
        return ResponseVo.ok();
    }


    /**
     *  校验信息
     */
    @GetMapping("check/{data}/{type}")
    @ApiOperation("检查用户是否存在")
    public ResponseVo<Boolean> checkData(@PathVariable("data") String data,@PathVariable("type") Integer type){
        Boolean bool = this.userService.checkData(data,type);
        return ResponseVo.ok(bool);
    }

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUserByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = userService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UserEntity> queryUserById(@PathVariable("id") Long id){
		UserEntity user = userService.getById(id);

        return ResponseVo.ok(user);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UserEntity user){
		userService.save(user);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UserEntity user){
		userService.updateById(user);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		userService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
