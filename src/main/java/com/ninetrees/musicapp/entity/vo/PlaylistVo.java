package com.ninetrees.musicapp.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ninetrees.musicapp.entity.Songs;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classname:PlaylistVo
 * Description:
 */
@Data
public class PlaylistVo {
    private String playlistId;

    private String name;

    private String description;

    private String coverUrl;

    private String creatorName;

    private Date createdAt;

    private Integer songCount; // 歌曲总数

    List<Songs>list=new ArrayList<>();
}
