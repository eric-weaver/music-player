package com.weaver.eric.orion.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.models.Song;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_SINGLE = 1;
    public static final int REPEAT_ALL = 2;

    public static final int SHUFFLE_OFF = 0;
    public static final int SHUFFLE_ON = 1;

    public static final int PAUSED = 0;
    public static final int PLAYING = 1;

    //tag for logs
    private static final String TAG = "MusicService";

    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> mSongs;
    //current position
    private int mSongPos;
    //length of song time
    private int songDuration;
    //is shuffle enabled
    private int mShuffle;
    //state of mRepeat
    private int mRepeat;

    private int mSeekPos;

    private OnPreparedListener mPreparedListener;

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public interface OnPreparedListener{
        void onPrepared(String songName, int songDuration, int songPosition);
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
        savePlayerState();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences pref = getSharedPreferences((getString(R.string.preferences_music_service)), Context.MODE_PRIVATE);
        //initialize position
        mSongPos = pref.getInt(getString(R.string.pref_song_pos), 0);
        mSeekPos = pref.getInt(getString(R.string.pref_seek_pos),0);
        mShuffle = pref.getInt(getString(R.string.pref_shuffle_mode), SHUFFLE_OFF);
        mRepeat = pref.getInt(getString(R.string.pref_repeat_mode), REPEAT_NONE);

        //create player
        player = new MediaPlayer();

        initMusicPlayer();
    }

    @Override
    public void onDestroy() {
        savePlayerState();
        super.onDestroy();
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
        mPreparedListener.onPrepared(mSongs.get(mSongPos).getSongName(), getSongDuration(), getSongPosition());
    }

    public void setShuffle(int shuffle){
        if(shuffle == SHUFFLE_ON) {
            mShuffle = SHUFFLE_ON;
        }
        if(shuffle == SHUFFLE_OFF) {
            mShuffle = SHUFFLE_OFF;
        }
    }

    public int isShuffle(){
        return mShuffle;
    }

    public void shuffle(){
        if(mShuffle == SHUFFLE_OFF){
            setShuffle(SHUFFLE_ON);
        }else {
            setShuffle(SHUFFLE_OFF);
        }
    }

    public void setRepeat(int repeat){
        if(repeat == REPEAT_NONE) {
            mRepeat = REPEAT_NONE;
        }
        if(repeat == REPEAT_SINGLE) {
            mRepeat = REPEAT_SINGLE;
        }
        if(repeat == REPEAT_ALL) {
            mRepeat = REPEAT_ALL;
        }
    }

    public int getRepeat(){
        return mRepeat;
    }

    public void repeat(){
        if(mRepeat == REPEAT_NONE){
            setRepeat(REPEAT_ALL);
        }else if(mRepeat == REPEAT_ALL){
            setRepeat(REPEAT_SINGLE);
        }else{
            setRepeat(REPEAT_NONE);
        }
    }

    public int isPlaying(){
        //Override the is playing boolean with our own constants
        if(player.isPlaying()){
            return PLAYING;
        }else{
            return PAUSED;
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
        if(mSongs == null || player == null){
            Log.w(TAG, "Tried to play song with empty playlist");
            return;
        }
        //get song
        Song playSong = mSongs.get(mSongPos);
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
        if(mSongs == null || player == null){
            Log.w(TAG, "Tried to play song with empty playlist");
            return;
        }
        if(mRepeat == REPEAT_SINGLE){
            playSong();
            return;
        }
        int lastPosn = mSongs.size() - 1;
        if(mSongPos == lastPosn){
            if(mRepeat == REPEAT_ALL){
                mSongPos = 0;
                playSong();
            }else{
                player.reset();
            }
        }else{
            mSongPos++;
            playSong();
        }
    }

    public void prevSong(){
        if(mSongPos != 0){
            mSongPos--;
        }

        playSong();
    }

    public void setList(ArrayList<Song> theSongs){
        //TODO Figure out better way for setList
        mSongs=theSongs;
    }

    public void setSong(int songIndex){
        mSongPos = songIndex;
    }

    public void seekToPosition(int position)
    {
        if(mSongs == null || player == null){
            return;
        }
        player.seekTo(position * 1000);
    }

    public int getSongDuration(){
        //Return the duration in seconds rather than milliseconds
        return songDuration / 1000;
    }

    public int getSongPosition()
    {
        if (mSongs == null || player == null) {
            return 0;
        }
        return player.getCurrentPosition() / 1000;

    }

    public String getCurrentSong(){
        if(mSongs == null){
            return null;
        }
        return mSongs.get(mSongPos).getSongName();
    }

    public void setOnPreparedListener(OnPreparedListener listener){
        this.mPreparedListener = listener;
    }

    private void savePlayerState(){
        SharedPreferences pref = getSharedPreferences((getString(R.string.preferences_music_service)), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(getString(R.string.pref_song_pos), mSongPos);
        editor.putInt(getString(R.string.pref_seek_pos), mSeekPos);
        editor.putInt(getString(R.string.pref_shuffle_mode), mShuffle);
        editor.putInt(getString(R.string.pref_repeat_mode), mRepeat);
        editor.commit();
    }
}
