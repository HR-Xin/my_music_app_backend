package com.ninetrees.musicapp.service.impl;

import com.ninetrees.musicapp.service.EmailService;
import com.ninetrees.musicapp.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Classname:EmailServiceImpl
 * Description:
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @Override
    public void sendEmail(String to) {
        //生成验证码
        String code = CodeUtils.generateCode();
        //// 将验证码存储到 Redis，有效期 5 分钟
        template.opsForValue().set(to, code, 5, TimeUnit.MINUTES);


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("3264259292@qq.com"); // 发件人邮箱
        message.setTo(to); // 收件人邮箱
        message.setSubject("禅音登录验证"); // 邮件主题
        message.setText("您的登录验证码是:" + code); // 邮件内容
        mailSender.send(message);
    }
}
