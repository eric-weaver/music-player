package com.weaver.eric.orion.models;

import android.graphics.Bitmap;

/**
 * Created by Eric on 5/30/2015.
 */
public class Song {

    private long songId;
    private String songName;
    private String songArtist;
    private String songAlbum;
    private long songDuration;
    private Bitmap albumCover;

    public Song(long songId, String songName, String songArtist, String songAlbum, long songDuration) {
        this.songId = songId;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songAlbum = songAlbum;
        this.songDuration = songDuration;
    }

    public long getId() {
        return songId;
    }

    public void setId(long id) {
        this.songId = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Bitmap getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(Bitmap albumCover) {
        this.albumCover = albumCover;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }
}
