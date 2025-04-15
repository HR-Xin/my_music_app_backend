package com.ninetrees.musicapp.service;

import com.ninetrees.musicapp.entity.PlaylistSongs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-29
 */
public interface PlaylistSongsService extends IService<PlaylistSongs> {

    List<PlaylistSongs> getAllPlaylistSongs(String playlistId);

    // 添加音乐到歌单
    void addMusicToPlaylist(String playlistId, String musicId);

    // 移除音乐
    void removeMusicFromPlaylist(String playlistId, String musicId);

    // 查询歌单中的音乐
    List<PlaylistSongs> getMusicInPlaylist(String playlistId);

    void deleteAllMusicInPlaylists(String playlistId);
}
