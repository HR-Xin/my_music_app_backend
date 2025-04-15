package com.ninetrees.musicapp.service;

import com.ninetrees.musicapp.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ninetrees.musicapp.entity.vo.RegisterVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-21
 */
public interface UsersService extends IService<Users> {


    String login(Users user);

    void register(RegisterVo registerVo);
}
