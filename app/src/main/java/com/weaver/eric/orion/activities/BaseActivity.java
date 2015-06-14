package com.weaver.eric.orion.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.SeekBar;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.fragments.Player;
import com.weaver.eric.orion.services.MusicService;

public abstract class BaseActivity extends ActionBarActivity implements Player.OnSeekBarChangeListener, Player.OnPlayerControlsListener,
        Player.OnPlayerStateListener, MusicService.OnPreparedListener
{
    protected MusicService musicSrv;
    protected Intent playIntent;
    protected boolean musicBound = false;

    private Player mPlayer;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if(mPlayer != null && mPlayer.isAdded())
            {
                updateProgress();
            }

            mHandler.postDelayed(this, 1000);
        }
    };

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            musicSrv.setOnPreparedListener(BaseActivity.this);
            mHandler.postDelayed(mRunnable, 1000);
            musicBound = true;
            setPlayerState();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment playerFragment = new Player();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_drawer_player, playerFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayer = (Player)getSupportFragmentManager().findFragmentById(R.id.container_drawer_player);
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            //startService(playIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPlayerState();
    }

    @Override
    protected void onDestroy() {
        //stopService(playIntent);
        unbindService(musicConnection);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(int progress) {
        musicSrv.seekToPosition(progress);
        updateProgress();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onPauseTouch() {
        musicSrv.pauseSong();
    }

    @Override
    public void onPrevTouch() {
        musicSrv.prevSong();
    }

    @Override
    public void onNextTouch() {
        musicSrv.nextSong();
    }

    @Override
    public void onPrepared(String songName, int songDuration, int songPosition) {
        if(mPlayer != null && mPlayer.isAdded())
        {
            mPlayer.setCurrentSong(songName, songDuration, songPosition);
            mPlayer.setPlayState(musicSrv.isPlaying());
        }
    }

    @Override
    public void requestPlayerState() {

    }

    @Override
    public void requestPauseState() {
        if(mPlayer != null && mPlayer.isAdded()) {
            mPlayer.setPlayState(musicSrv.isPlaying());
        }
    }

    @Override
    public void requestShuffleState () {
        if(mPlayer != null && mPlayer.isAdded()) {
            mPlayer.setShuffleState(musicSrv.isShuffle());
        }
    }

    @Override
    public void requestRepeatState() {
        if(mPlayer != null && mPlayer.isAdded()) {
            mPlayer.setRepeatState(musicSrv.getRepeat());
        }
    }

    @Override
    public void onRepeatTouch() {
        musicSrv.repeat();
    }

    @Override
    public void onShuffleTouch() {
        musicSrv.shuffle();
    }

    private void updateProgress(){
        if(musicSrv.getCurrentSong() != null){
            int time = musicSrv.getSongPosition();
            mPlayer.updateProgress(time);
        }
    }

    private void setPlayerState(){
        if(musicBound){
            if(mPlayer != null && mPlayer.isAdded()) {
                mPlayer.setCurrentSong(musicSrv.getCurrentSong(), musicSrv.getSongDuration(), musicSrv.getSongPosition());
                mPlayer.setPlayState(musicSrv.isPlaying());
                mPlayer.setShuffleState(musicSrv.isShuffle());
                mPlayer.setRepeatState(musicSrv.getRepeat());
            }
        }
    }
}
