package com.ninetrees.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninetrees.musicapp.entity.Songs;
import com.ninetrees.musicapp.entity.vo.SongVo;
import com.ninetrees.musicapp.exception.ChanException;
import com.ninetrees.musicapp.mapper.SongsMapper;
import com.ninetrees.musicapp.service.SongsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hr_xin
 * @since 2025-04-01
 */
@Service
public class SongsServiceImpl extends ServiceImpl<SongsMapper, Songs> implements SongsService {
    @Override
    public List<SongVo> getMyUploadedSongs(String userId){
        List<Songs> uploadedSongs = baseMapper.selectList(new QueryWrapper<Songs>().eq("uploader_id", userId));
        List<SongVo>songVoList=new ArrayList<>();
        for(Songs song:uploadedSongs){
        SongVo songVo=new SongVo();
        songVo.setCoverUrl(song.getCoverUrl());
        songVo.setMusicId(song.getMusicId());
        songVo.setTitle(song.getTitle());
        songVoList.add(songVo);
        }
        if (uploadedSongs==null){
            throw new ChanException("用户上传列表为空");
        }


        return songVoList;


    }
}
