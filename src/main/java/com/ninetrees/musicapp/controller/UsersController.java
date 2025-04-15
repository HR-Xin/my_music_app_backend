package com.ninetrees.musicapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.entity.Users;
import com.ninetrees.musicapp.entity.vo.RegisterVo;
import com.ninetrees.musicapp.exception.ChanException;
import com.ninetrees.musicapp.service.EmailService;
import com.ninetrees.musicapp.service.UsersService;
import com.ninetrees.musicapp.utils.CodeUtils;
import com.ninetrees.musicapp.utils.JwtUtils;
import com.ninetrees.musicapp.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-21
 */
@RestController
//@CrossOrigin(origins = "http://47.120.34.75")
@RequestMapping("/api/user")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("用户id:" + userId);
        wrapper.eq("id", userId);
        Users user = usersService.getOne(wrapper);
        user.setPassword("");
        System.out.println("前端查询用户id:" + user.getId());
        return R.ok().data("user", user);
    }

    @GetMapping("getUserInfo/{userId}")
    public R getUserInfo(HttpServletRequest request, String userId) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId);
        Users user = usersService.getOne(wrapper);
        user.setPassword("");
        return R.ok().data("user", user);
    }

    //根据id进行个人资料的更新
    @PostMapping("/updateProfile")
    public R updateProfile(@RequestBody Users user) {
        String id = user.getId();
        String password = user.getPassword();
        String encrypt = "";//不赋值null,放置password为空时,后续encrypt为空
        if (password != null) {
            encrypt = MD5.encrypt(password);
            user.setPassword(encrypt);
        }
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        System.out.println("更新资料的id" + Long.valueOf(id));
        wrapper.eq("id", Long.valueOf(id));
        if (user == null) {
            return R.ok();
        }
        boolean result = usersService.update(user, wrapper);

        return R.ok();
    }

    @DeleteMapping("{userId}")
    public R deleteUserByUserId(@PathVariable String userId){

        Users user = usersService.getOne(new QueryWrapper<Users>().eq("id", userId));
        if (user==null||user.getId()==null||user.getIsDeleted()!=0){
            return R.error().message("用户不存在");
        }

        user.setIsDeleted(1);
        usersService.updateById(user);
        return R.ok();
    }

    @PostMapping("updateEmail/{userId}")
    public R updateEmail(@RequestBody Users user,@PathVariable String userId){
        System.out.println("user:"+user+"       userId:"+userId);
        if (user==null){
            return R.error().message("更新信息无效");
        }
        Users one = usersService.getOne(new QueryWrapper<Users>().eq("id", Long.valueOf(userId)));
        if (one==null){
            throw new ChanException("获取用户信息失败");
        }

        if (!(MD5.encrypt(user.getPassword()).equals(one.getPassword()))){
            throw new ChanException("密码错误,修改失败");
        }
        one.setEmail(user.getEmail());
        one.setId(userId);
        if (one!=null){
        System.out.println("待更新的user对象:"+one.toString());
        }
        boolean saved = usersService.updateById(one);
        if (!saved){
            throw new ChanException("邮箱更新失败");
        }else {
        return R.ok().message("邮箱更新成功");
        }
    }

    @PutMapping("updateEM/{userId}/{newEmail}")
    public R updateEm(@PathVariable String userId,@PathVariable String newEmail){

//        QueryWrapper<Users>wrapper=new QueryWrapper<>();
//        wrapper.eq("id",userId);
//        Users one = usersService.getOne(wrapper);

        Users users = new Users();
        users.setEmail(newEmail);
        users.setId(userId);
        usersService.updateById(users);
        return R.ok();

    }


}

