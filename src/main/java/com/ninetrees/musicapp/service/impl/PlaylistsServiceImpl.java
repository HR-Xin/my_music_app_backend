package com.ninetrees.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninetrees.musicapp.entity.Playlists;
import com.ninetrees.musicapp.entity.Songs;
import com.ninetrees.musicapp.entity.vo.PlaylistVo;
import com.ninetrees.musicapp.exception.ChanException;
import com.ninetrees.musicapp.mapper.PlaylistsMapper;
import com.ninetrees.musicapp.service.PlayListsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ninetrees.musicapp.service.PlaylistSongsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-24
 */
@Service
@Slf4j
public class PlaylistsServiceImpl extends ServiceImpl<PlaylistsMapper, Playlists> implements PlayListsService {
    @Autowired
    private PlaylistSongsService playlistSongsService;
    @Autowired
    private PlaylistsMapper playlistsMapper;

    @Override
    public void savePlayList(Playlists playList) {
        System.out.println("前端传入对象" + playList);
        QueryWrapper<Playlists> wrapper = new QueryWrapper<>();
        wrapper.eq("creator_id", playList.getCreatorId());
        int insert = baseMapper.insert(playList);
        if (insert == 0) {
            throw new ChanException("歌单添加失败");
        }
    }

    @Override
    public List<Playlists> getFrontPlayLists(String userId) {
        QueryWrapper<Playlists> wrapper = new QueryWrapper<>();
        wrapper.eq("creator_id", userId);
        List<Playlists> playlists = baseMapper.selectList(wrapper);
        if (playlists == null) {
            throw new ChanException("该用户不存在歌单");
        }
        return playlists;
    }

    @Override
    public PlaylistVo getListInfo(String playlistId) {
        //查询歌单信息
//        QueryWrapper<Playlists>wrapper=new QueryWrapper<>();
//        wrapper.eq("id",playlistId);
        Playlists playlists = baseMapper.selectById(playlistId);
        String name = playlists.getName();
        String description = playlists.getDescription();
        String coverUrl = playlists.getCoverUrl();
        Date createdAt = playlists.getCreatedAt();
        log.info("歌单信息: {}", playlists);

//        log.info("playlistsMapper 是否为空: {}", playlistsMapper);

        List<Songs> songsInPlaylist = null;
        try {
            songsInPlaylist = playlistsMapper.getSongsInPlaylist(Long.valueOf(playlistId));
        } catch (NumberFormatException e) {
            throw new ChanException("类型转化错误");
        }catch (Exception e){
            throw new ChanException("抛出全局异常");
        }
        log.info("查询到的歌曲列表: {}", songsInPlaylist); // 打印返回的列表
        if (songsInPlaylist==null){
            log.error("歌单内无歌曲或查询失败, 歌单ID: {}", playlistId);
            throw new ChanException("获取歌单内歌曲失败");
        }
        PlaylistVo playlistVo=new PlaylistVo();
        playlistVo.setList(songsInPlaylist);
        playlistVo.setPlaylistId(playlistId);
        playlistVo.setName(name);
        playlistVo.setCoverUrl(coverUrl);
        playlistVo.setDescription(description);
        playlistVo.setCreatedAt(createdAt);
        log.info("返回的歌单详情: {}", playlistVo);

        return playlistVo;
    }


}
