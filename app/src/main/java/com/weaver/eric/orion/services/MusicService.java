package com.weaver.eric.orion.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

import com.weaver.eric.orion.objects.Song;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //possible repeat states
    public enum RepeatState{ALL, ONE, NONE}
    //tag for logs
    private static final String TAG = "MusicService";

    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    //is shuffle enabled
    private boolean isShuffle;
    //state of repeat
    private RepeatState repeat;

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize position
        songPosn=0;
        isShuffle = false;
        repeat = RepeatState.NONE;
        //create player
        player = new MediaPlayer();

        initMusicPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    public void setShuffle(boolean shuffle){
        isShuffle = shuffle;
    }

    public boolean isShuffle(){
        return isShuffle;
    }

    public void setRepeat(RepeatState repeat){
        this.repeat = repeat;
    }

    public RepeatState getRepeat(){
        return repeat;
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void playSong(){
        player.reset();

        //get song
        Song playSong = songs.get(songPosn);
        //get id
        long currSong = playSong.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e(TAG, "Error setting data source", e);
        }
        finally {
            player.prepareAsync();
        }

    }

    public void setList(ArrayList<Song> theSongs){
        //TODO Figure out better way for setList
        songs=theSongs;
    }

    public void setSong(int songIndex){
        songPosn = songIndex;
    }
}
