package com.ninetrees.musicapp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.entity.Playlists;
import com.ninetrees.musicapp.entity.vo.PlaylistVo;
import com.ninetrees.musicapp.exception.ChanException;
import com.ninetrees.musicapp.service.PlayListsService;
import com.ninetrees.musicapp.service.PlaylistSongsService;
import com.ninetrees.musicapp.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Writer;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-24
 */
@RestController
//@CrossOrigin(origins = "http://47.120.34.75")
@RequestMapping("/api/playlists")
@Slf4j
public class PlaylistsController {

    @Autowired
    private PlayListsService listsService;
    @Autowired
    private PlaylistSongsService playlistSongsService;

    @PostMapping("saveList/{userId}")
    public R savePlayList(@RequestBody Playlists playlist, @PathVariable String userId) {
        System.out.println("请求对象和id" + playlist + userId);
        playlist.setCreatorId(userId);
//        playlist.setCoverUrl()
        listsService.savePlayList(playlist);
        return R.ok();
    }

    //获取歌单列表
    @GetMapping("")
    public R getLists(HttpServletRequest request) {
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        List<Playlists> playlistsList = listsService.list(new QueryWrapper<Playlists>().eq("creator_id", userId));
        return R.ok().data("list", playlistsList);
    }

    @GetMapping("getAllPlayLists/{userId}")
    public R getFrontPlayLists(@PathVariable String userId) {
        List<Playlists> frontPlayLists = listsService.getFrontPlayLists(userId);
        return R.ok().data("list", frontPlayLists);
    }


    @GetMapping("/getByPage/{pageCurrent}/{pageSize}")
    public R pagePlayLists(@PathVariable long pageCurrent,
                           @PathVariable long pageSize) {
        Page<Playlists> page = new Page<>(pageCurrent, pageSize);
        listsService.page(page, null);
        return R.ok().data("list", page.getRecords()).data("total", page.getTotal());
    }

    //更新歌单信息
    @PutMapping("updateById")
    public R updatePlaylistById(@RequestBody Playlists playlist) {
        //        listsService.update(playlist, new QueryWrapper<Playlists>().eq("id", Long.valueOf(playlist.getId())));
        listsService.updateById(playlist);
        return R.ok();
    }

    //删除歌单,如果歌单内很有音乐,先删除歌单内音乐再删除歌单
    @DeleteMapping("{playlistId}")
    public R deleteListById(@PathVariable String playlistId) {
        log.info("Attempting to delete playlist with id: {}", playlistId);
        int count = listsService.count(new QueryWrapper<Playlists>().eq("id", Long.valueOf(playlistId)));
        if (count == 0) {
            log.error("Playlist with id {} does not exist", playlistId);
            throw new ChanException("删除歌单失败");
        }
        log.info("Deleting all music in playlist with id: {}", playlistId);
        playlistSongsService.deleteAllMusicInPlaylists(playlistId);
        log.info("Deleting playlist with id: {}", playlistId);
        listsService.removeById(playlistId);
        return R.ok();
    }


    //获取歌单详情
    @GetMapping("getPlaylistInfo/{playlistId}")
    public R getPlaylistInfo(@PathVariable String playlistId) {
        PlaylistVo songList = listsService.getListInfo(playlistId);

            return R.ok().data("songList", songList);
    }

}

