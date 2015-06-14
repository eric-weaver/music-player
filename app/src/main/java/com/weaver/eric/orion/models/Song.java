package com.weaver.eric.orion.models;

import android.graphics.Bitmap;

/**
 * Created by Eric on 5/30/2015.
 */
public class Song {

    private long id;
    private String songName;
    private Bitmap albumCover;

    public Song(long id, String songName, Bitmap albumCover) {
        this.id = id;
        this.songName = songName;
        this.albumCover = albumCover;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
