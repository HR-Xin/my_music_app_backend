package com.ninetrees.musicapp.mapper;

import com.ninetrees.musicapp.entity.Playlists;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ninetrees.musicapp.entity.Songs;
import org.apache.ibatis.annotations.Mapper;

import javax.mail.MailSessionDefinition;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-24
 */
@Mapper
public interface PlaylistsMapper extends BaseMapper<Playlists> {
    public List<Songs> getSongsInPlaylist(Long playlistId);
}
