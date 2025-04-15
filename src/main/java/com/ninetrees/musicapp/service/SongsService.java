package com.ninetrees.musicapp.service;

import com.ninetrees.musicapp.entity.Songs;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ninetrees.musicapp.entity.vo.SongVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hr_xin
 * @since 2025-04-01
 */
public interface SongsService extends IService<Songs> {

    List<SongVo> getMyUploadedSongs(String userId);
}
