package com.ninetrees.musicapp.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classname:SongVo
 * Description:
 */
@Data
@AllArgsConstructor//lombok默认提供无参构造方法,只要不是自己定义的,
// 都不需要自行定义@NoArgsConstructor
@NoArgsConstructor
public class SongVo {
    private String id;
    private String title;
    private String coverUrl;
    private String musicId;
}
