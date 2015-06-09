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
    public enum ShuffleState{ON, OFF}
    public enum PlayState{PLAYING, PAUSED}

    //tag for logs
    private static final String TAG = "MusicService";

    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    //length of song time
    private int songDuration;
    //is shuffle enabled
    private ShuffleState isShuffle;
    //state of repeat
    private RepeatState repeat;

    private OnPreparedListener mPreparedListener;

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public interface OnPreparedListener{
        void onPrepared(String songName, int songDuration);
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
        isShuffle = ShuffleState.OFF;
        repeat = RepeatState.NONE;
        //create player
        player = new MediaPlayer();

        initMusicPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        songDuration = mp.getDuration();
        //start playback
        mp.start();
        mPreparedListener.onPrepared(songs.get(songPosn).getSongName(), getSongDuration());
    }

    public void setShuffle(ShuffleState shuffle){
        isShuffle = shuffle;
    }

    public ShuffleState isShuffle(){
        return isShuffle;
    }

    public void shuffle(){
        if(isShuffle == ShuffleState.OFF){
            setShuffle(ShuffleState.ON);
        }else {
            setShuffle(ShuffleState.OFF);
        }
    }

    public void setRepeat(RepeatState repeat){
        this.repeat = repeat;
    }

    public RepeatState getRepeat(){
        return repeat;
    }

    public void repeat(){
        if(repeat == RepeatState.NONE){
            setRepeat(RepeatState.ALL);
        }else if(repeat == RepeatState.ALL){
            setRepeat(RepeatState.ONE);
        }else{
            setRepeat(RepeatState.NONE);
        }
    }

    public PlayState isPlaying(){
        //Override the is playing boolean with our own constants
        if(player.isPlaying()){
            return PlayState.PLAYING;
        }else{
            return PlayState.PAUSED;
        }
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

    public void pauseSong(){
        try{
            if (player.isPlaying())
            {
                player.pause();
            } else{
                player.start();
            }
        }catch(IllegalStateException e){
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
    }

    public void nextSong(){
        if(repeat == RepeatState.ONE){
            playSong();
            return;
        }
        int lastPosn = songs.size() - 1;
        if(songPosn == lastPosn){
            if(repeat == RepeatState.ALL){
                songPosn = 0;
                playSong();
            }else{
                player.reset();
            }
        }else{
            songPosn++;
            playSong();
        }
    }

    public void prevSong(){
        if(songPosn != 0){
            songPosn--;
        }

        playSong();
    }

    public void setList(ArrayList<Song> theSongs){
        //TODO Figure out better way for setList
        songs=theSongs;
    }

    public void setSong(int songIndex){
        songPosn = songIndex;
    }

    public void seekToPosition(int position)
    {
        player.seekTo(position * 1000);
    }

    public int getSongDuration(){
        //Return the duration in seconds rather than milliseconds
        return songDuration / 1000;
    }

    public int getSongPosition()
    {
        if (player == null) {
            return 0;
        }
        return player.getCurrentPosition() / 1000;

    }

    public String getCurrentSong(){
        if(songs == null){
            return null;
        }
        return songs.get(songPosn).getSongName();
    }

    public void setOnPreparedListener(OnPreparedListener listener){
        this.mPreparedListener = listener;
    }

}
