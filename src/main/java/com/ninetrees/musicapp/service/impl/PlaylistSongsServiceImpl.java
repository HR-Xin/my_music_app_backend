package com.ninetrees.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninetrees.musicapp.entity.PlaylistSongs;
import com.ninetrees.musicapp.exception.ChanException;
import com.ninetrees.musicapp.mapper.PlaylistSongsMapper;
import com.ninetrees.musicapp.service.PlaylistSongsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-29
 */
@Service
public class PlaylistSongsServiceImpl extends ServiceImpl<PlaylistSongsMapper, PlaylistSongs> implements PlaylistSongsService {
    @Override
    public List<PlaylistSongs> getAllPlaylistSongs(String playlistId) {
        List<PlaylistSongs> playlistSongsList = baseMapper.selectList(new QueryWrapper<PlaylistSongs>().eq("playlist_id", playlistId).eq("is_deleted", 0).select(""));
        if (playlistSongsList == null) {
            return null;
        }
        return playlistSongsList;
    }

    @Override
    // 添加音乐到歌单
    public void addMusicToPlaylist(String playlistId, String musicId) {
        int insert = baseMapper.insert(new PlaylistSongs().setPlaylistId(playlistId).setSongId(musicId));
        if (insert == 0) {
            throw new ChanException("添加失败");
        }
    }

    @Override
    // 移除音乐
    public void removeMusicFromPlaylist(String playlistId, String musicId) {
        baseMapper.delete(new QueryWrapper<PlaylistSongs>()
                .eq("playlist_id", playlistId)
                .eq("song_id", musicId));

    }

    @Override
    // 查询歌单中的音乐
    public List<PlaylistSongs> getMusicInPlaylist(String playlistId) {
        return baseMapper.selectList(new QueryWrapper<PlaylistSongs>().eq("playlist_id", playlistId));
    }

    @Override
    public void deleteAllMusicInPlaylists(String playlistId) {
        // 查询歌单中的所有音乐
        QueryWrapper<PlaylistSongs> wrapper = new QueryWrapper<PlaylistSongs>().eq("playlist_id", Long.valueOf(playlistId));
        List<PlaylistSongs> playlistSongsList = baseMapper.selectList(wrapper);
//        if (playlistSongsList == null) {      selectList不返回null而是空数组
        if(playlistSongsList.isEmpty()){
            return;
        }
        // 提取所有音乐的id到一个集合中
//        List<Long> songIds = new ArrayList<>();
//        for (PlaylistSongs song : playlistSongsList) {
//            songIds.add(Long.valueOf(song.getSongId())); // 假设PlaylistSongs类中有一个getId()方法来获取id
//        }
//
//        // 使用baseMapper的deleteBatchIds方法进行批量删除
//        int count = baseMapper.deleteBatchIds(songIds);
//        if (count == 0) {
//            throw new ChanException("歌单中音乐删除失败");
//        }

        // 直接删除 playlist_music 表中的这些记录
        int count = baseMapper.delete(wrapper);
        if (count == 0) {
            throw new ChanException("歌单中音乐删除失败");
        }
    }

}
