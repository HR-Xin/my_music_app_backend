package com.ninetrees.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninetrees.musicapp.entity.Users;
import com.ninetrees.musicapp.entity.vo.RegisterVo;
import com.ninetrees.musicapp.exception.ChanException;
import com.ninetrees.musicapp.mapper.UsersMapper;
import com.ninetrees.musicapp.service.EmailService;
import com.ninetrees.musicapp.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ninetrees.musicapp.utils.JwtUtils;
import com.ninetrees.musicapp.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-21
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @Autowired
    private EmailService emailService;

    @Override
    public String login(Users user) {

        String password = user.getPassword();
        String email = user.getEmail();
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new ChanException("登录失败");
        }
        QueryWrapper<Users> wrapper = new QueryWrapper<>();

        wrapper.eq("email", email);
        Users users = baseMapper.selectOne(wrapper);
        System.out.println(users.getId());
        if (users == null) {
            throw new ChanException("登录失败,用户未注册");
        }

        String encrypt = MD5.encrypt(password);
        if (!encrypt.equals(users.getPassword())) {
            throw new ChanException("密码错误,重新登录");
        }


        return JwtUtils.getJwtToken(users.getId(), users.getUsername());
    }

    @Override
    public void register(RegisterVo registerVo) {
        String password = registerVo.getPassword();
        String email = registerVo.getEmail();
        String username = registerVo.getUsername();
        String code = registerVo.getCode();

//        验证码是否正确,不正确就不用后面操作了
        String redisCode;
//        redisCode = (String) template.opsForValue().get(email);
//        if (redisCode == null) {//redis生成验证码已过期
//            emailService.sendEmail(email);//重新发送email
//            redisCode=(String) template.opsForValue().get(email);
//        }
//
//        if (!redisCode.equals(code)) {
//            throw new ChanException("验证码错误,注册失败");
//        }

        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        Integer count = baseMapper.selectCount(wrapper);
        if (count >= 1) {
            throw new ChanException("用户已存在,注册失败");
        }

        Users user = new Users();
        user.setEmail(email);
        String encrypt = MD5.encrypt(password);
        user.setPassword(encrypt);
        user.setUsername(username);
        user.setAvatar("https://edu-2233.oss-cn-beijing.aliyuncs.com/600.webp");

        int result = baseMapper.insert(user);
        if (result == 0) {
            throw new ChanException("数据库插入用户数据失败");
        }

    }

}
