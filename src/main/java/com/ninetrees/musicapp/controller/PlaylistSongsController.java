package com.ninetrees.musicapp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.entity.PlaylistSongs;
import com.ninetrees.musicapp.service.PlayListsService;
import com.ninetrees.musicapp.service.PlaylistSongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-29
 */
@RestController
//@CrossOrigin(origins = "http://47.120.34.75")
@RequestMapping("/api/playlistsongs")
public class PlaylistSongsController {
    @Autowired
    private PlaylistSongsService playlistSongsService;

    @PostMapping("addSongs/{playListId}")
    public R batchAddSongsToPlayList(@PathVariable String playListId, @RequestBody String[] songIds) {
        System.out.println("歌单id:"+playListId+"歌曲ids:"+ Arrays.stream(songIds).toList());
        int length = songIds.length;
        List<PlaylistSongs>list=new ArrayList<>();
        for (int i = 0; i < length; i++) {
            PlaylistSongs playlistSongs = new PlaylistSongs();
            playlistSongs.setPlaylistId(playListId);
            playlistSongs.setSongId(songIds[i]);
            list.add(playlistSongs);
            playlistSongsService.save(playlistSongs);
        }
        for (PlaylistSongs p:list){
            System.out.println(p.toString());
        }
//        boolean b = playlistSongsService.saveBatch(list);
//        if(b){
        return R.ok().message("歌单添加歌曲成功");
//        }else{
//            return R.error().message("歌单添加歌曲成功");
//        }
    }

}

