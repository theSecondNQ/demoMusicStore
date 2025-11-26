package com.music_store.Dao;

import com.music_store.Entity.Playlist;
import com.music_store.Entity.PlaylistTrack;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlaylistDao {

    // Playlists
    @Insert("INSERT INTO playlist (user_id, name, description, visibility, create_time, update_time) " +
            "VALUES (#{userId}, #{name}, #{description}, #{visibility}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertPlaylist(Playlist playlist);

    @Update("UPDATE playlist SET name=#{name}, description=#{description}, visibility=#{visibility}, update_time=NOW() WHERE id=#{id}")
    int updatePlaylist(Playlist playlist);

    @Delete("DELETE FROM playlist WHERE id=#{id}")
    int deletePlaylist(Integer id);

    @Select("SELECT * FROM playlist WHERE id=#{id}")
    Playlist selectById(Integer id);

    @Select("SELECT * FROM playlist WHERE user_id=#{userId} ORDER BY update_time DESC")
    List<Playlist> selectByUserId(Integer userId);

    @Select("SELECT * FROM playlist WHERE visibility='shared' ORDER BY update_time DESC")
    List<Playlist> selectShared();

    // Tracks
    @Insert("INSERT INTO playlist_track (playlist_id, music_id, position, add_time) VALUES (#{playlistId}, #{musicId}, #{position}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertTrack(PlaylistTrack track);

    @Delete("DELETE FROM playlist_track WHERE id=#{id}")
    int deleteTrack(Integer id);

    @Delete("DELETE FROM playlist_track WHERE playlist_id=#{playlistId} AND music_id=#{musicId}")
    int deleteTrackByPlaylistAndMusic(@Param("playlistId") Integer playlistId, @Param("musicId") Integer musicId);

    @Delete("DELETE FROM playlist_track WHERE music_id=#{musicId}")
    int deleteTracksByMusicId(Integer musicId);

    @Select("SELECT * FROM playlist_track WHERE playlist_id=#{playlistId} ORDER BY position ASC, add_time ASC")
    List<PlaylistTrack> selectTracksByPlaylistId(Integer playlistId);

    @Update("UPDATE playlist_track SET position=#{position} WHERE id=#{id}")
    int updateTrackPosition(@Param("id") Integer id, @Param("position") Integer position);

    @Select("SELECT COUNT(*) FROM playlist_track WHERE playlist_id=#{playlistId} AND music_id=#{musicId}")
    int existsMusicInPlaylist(@Param("playlistId") Integer playlistId, @Param("musicId") Integer musicId);
}