package com.ninetrees.musicapp.service;

import com.ninetrees.musicapp.entity.Playlists;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ninetrees.musicapp.entity.vo.PlaylistVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-24
 */
public interface PlayListsService extends IService<Playlists> {

    void savePlayList(Playlists playList);

    List<Playlists> getFrontPlayLists(String userId);

    PlaylistVo getListInfo(String playlistId);
}
