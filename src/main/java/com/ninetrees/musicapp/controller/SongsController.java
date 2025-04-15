package com.ninetrees.musicapp.controller;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ninetrees.alyoss.vod.ConstantVodUtils;
import com.ninetrees.alyoss.vod.VodService;
import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.entity.Songs;
import com.ninetrees.musicapp.exception.ChanException;
import com.ninetrees.musicapp.service.SongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hr_xin
 * @since 2025-04-01
 */
@RestController
//@CrossOrigin(origins = "http://47.120.34.75")
@RequestMapping("/api/songs")
public class SongsController {
    @Autowired
    private VodService vodService;
    @Autowired
    private SongsService songsService;

    @PostMapping("uploadMusic")
    public R uploadMusic(@RequestBody Songs song) {
//        String musicId = vodService.uploadMusicToVod(file);

//        song.setMusicId(musicId);
        System.out.println("song:" + song);
//        song.setUploaderId(song.getUploaderId());
        boolean saved = songsService.save(song);

        if (!saved) {
            return R.error().message("歌曲信息报存失败");
        }
        return R.ok();
//        .data("musicId", musicId)
    }

    @GetMapping("listAllSongs/{userId}")
    public R getAllSongsByUserId(@PathVariable String userId) throws ClientException, InterruptedException {
        QueryWrapper<Songs> wrapper = new QueryWrapper<>();
        wrapper.eq("uploader_id", userId);
        List<Songs> songs = songsService.list(wrapper);
        for (Songs song : songs) {
            Map<String, String> map = waitForVideoPlayable(song.getMusicId());

            song.setMusicUrl(map.get("playUrl"));
            song.setCoverUrl(map.get("coverUrl"));
        }
        return R.ok().data("songs", songs);
    }

    //    @GetMapping("test/{musicId}")
    public R getPlayUrl(@PathVariable String musicId) {
        try {
            Map<String, String> map = waitForVideoPlayable("20056407145071f0bfed5420848c0102");

            waitForVideoPlayable("20056407145071f0bfed5420848c0102");
            return R.ok().data("playURL", map.get("playUrl")).data("coverUrl", map.get("coverUrl"));
        } catch (ClientException e) {
            throw new ChanException("客户端异常");
        } catch (InterruptedException e) {
            throw new ChanException("中断异常");
        }

    }

    private Map<String, String> waitForVideoPlayable(String videoId) throws ClientException, InterruptedException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);

        GetVideoInfoRequest getVideoInfoRequest = new GetVideoInfoRequest();
        getVideoInfoRequest.setVideoId(videoId);
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        //获取音频封面
        GetVideoInfoResponse getVideoInfoResponse = client.getAcsResponse(getVideoInfoRequest);
        String coverURL = getVideoInfoResponse.getVideo().getCoverURL();
        // 根据阿里云 VOD 的文档，判断视频是否处于可播放状态
        GetPlayInfoResponse getPlayInfoResponse = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = getPlayInfoResponse.getPlayInfoList();
        Map<String, String> map = new HashMap<>();
        if (playInfoList != null && !playInfoList.isEmpty()) {
            map.put("playUrl", playInfoList.get(0).getPlayURL());
            map.put("coverUrl", coverURL);
            return map;
        } else {
            return null; // 获取播放链接失败
        }
    }

    @GetMapping("getAllSongs/{page}/{pageSize}/{userId}")
    public R getAllSongs(@PathVariable Long page, @PathVariable Long pageSize
            , @PathVariable String userId) {
        Page<Songs> Ipage = new Page<>(page, pageSize);
        songsService.page(Ipage, new QueryWrapper<Songs>().eq("uploader_id", userId));
        List<Songs>songsListlist= Ipage.getRecords();
        for (Songs song : songsListlist) {
            Map<String, String> map = null;
            try {
                map = waitForVideoPlayable(song.getMusicId());
                song.setMusicUrl(map.get("playUrl"));
                song.setCoverUrl(map.get("coverUrl"));
            } catch (ClientException e) {
                throw new ChanException("vod客户端异常");
            } catch (InterruptedException e) {
                throw new ChanException("中断异常");
            }
        }
        return R.ok().data("records", Ipage.getRecords()).data("total", Ipage.getTotal());
    }
}


