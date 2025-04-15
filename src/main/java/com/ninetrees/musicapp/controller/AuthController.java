package com.ninetrees.musicapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.entity.Users;
import com.ninetrees.musicapp.entity.vo.RegisterVo;
import com.ninetrees.musicapp.service.EmailService;
import com.ninetrees.musicapp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Classname:AuthController
 * Description:
 */
@RestController
//@CrossOrigin(origins = "http://47.120.34.75")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private EmailService emailService;


    @Autowired
    private UsersService usersService;

    @PostMapping("login")
    public R login(@RequestBody Users user) {

        String token = usersService.login(user);
        String email = user.getEmail();
        Users currentUser = usersService.getOne(new QueryWrapper<Users>().eq("email", email));
        currentUser.setPassword("");
        currentUser.setId(currentUser.getId());
        return R.ok().data("token", token).data("user",currentUser);
    }

    @PostMapping("register")
    public R register(RegisterVo registerVo) {
        usersService.register(registerVo);
        return R.ok().message("用户注册成功");
    }

    @GetMapping("sendcode/{email}")
    public R sendCode(@PathVariable String email) {
        if (email == null) {
            return R.error().message("邮箱为空");
        }

        emailService.sendEmail(email);

        return R.ok();
    }


}
