package com.aigc.vrmetal.controller;


import com.aigc.vrmetal.context.BaseContext;
import com.aigc.vrmetal.pojo.dto.PageQueryDto;
import com.aigc.vrmetal.pojo.dto.PhoneLoginDto;
import com.aigc.vrmetal.pojo.dto.UserDto;
import com.aigc.vrmetal.pojo.entity.User;
import com.aigc.vrmetal.pojo.vo.LoginVO;
import com.aigc.vrmetal.pojo.vo.Result;
import com.aigc.vrmetal.pojo.vo.UserVo;
import com.aigc.vrmetal.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gws
 * @since 2024-03-10
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口")
public class UserController {
    @Resource
    private IUserService userService;
    @ApiOperation("验证码接口")
    @PostMapping("getCode")
    public Result getCode(@RequestBody String phone){
        userService.sendCode(phone);
        return Result.success();
    }
    @ApiOperation("登录接口")
    @PostMapping("login")
    public Result<LoginVO> login(@RequestBody @ApiParam(name = "phoneLoginDto",value = "账号密码",required = true) PhoneLoginDto phoneLoginDto){
        LoginVO loginVO = userService.login(phoneLoginDto);
        if (loginVO == null) {
            return Result.error("账号或密码错误");
        }
        return Result.success(loginVO);
    }
    @ApiOperation("分页获取用户信息")
    @GetMapping("getAll")
    public Result<UserVo> getAll(PageQueryDto pageQueryDto){
        UserVo userVo=userService.userPageQuery(pageQueryDto);
        return Result.success(userVo);
    }
    @ApiOperation("添加用户")
    @GetMapping("add")
    public Result addUser(User user){
        user.setRegisterTime(LocalDateTime.now());
        userService.save(user);
        return Result.success();
    }
    @ApiOperation("用户个人信息")
    @GetMapping("getOne")
    public Result getUser(){
        Long userId = BaseContext.getCurrentId();
        User userById = userService.getById(userId);
        return Result.success(userById);
    }
    @GetMapping("/{id}")
    @ApiOperation("管理员根据id查询回显用户")
    public Result<User> getById(@PathVariable Long id){
        User user = userService.getById(id);
        return Result.success(user);
    }

    @ApiOperation("管理员修改信息")
    @PostMapping("change/{id}")
    public Result changeUser(@RequestBody UserDto userDto,@PathVariable String id){
        userDto.setId(Integer.parseInt(id));
        userService.change(userDto);
        return Result.success();
    }
    @ApiOperation("用户修改信息")
    @PostMapping("update")
    public Result updateUser(@RequestBody UserDto userDto){
        Long userId = BaseContext.getCurrentId();
        userDto.setId(userId.intValue());
        userService.change(userDto);
        return Result.success();
    }
    @ApiOperation("管理员删除")
    @PostMapping("deleteUser/{id}")
    public Result deleteOne(@PathVariable String id){
        userService.removeById(Integer.parseInt(id));
        return Result.success();
    }




}
