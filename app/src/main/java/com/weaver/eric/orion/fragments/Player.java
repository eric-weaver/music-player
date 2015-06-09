package com.weaver.eric.orion.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.weaver.eric.orion.R;
import com.weaver.eric.orion.services.MusicService.PlayState;
import com.weaver.eric.orion.services.MusicService.RepeatState;
import com.weaver.eric.orion.services.MusicService.ShuffleState;

public class Player extends Fragment implements SeekBar.OnSeekBarChangeListener {
    //tag for logs
    private static final String TAG = "Player";

    private View mView;
    private ImageButton btnPlayMini;
    private ImageButton btnNextMini;
    private ImageButton btnPrevMini;
    private ImageButton btnPlay;
    private ImageButton btnNext;
    private ImageButton btnPrev;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private ImageButton btnPlayList;
    private ImageView ivAlbumArt;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvSongTitle;
    private SeekBar songProgressBar;
    private SlidingUpPanelLayout slidingDrawer;
    private LinearLayout layoutMiniPlayer;

    private OnSeekBarChangeListener mSeekBarListener;
    private OnPlayerControlsListener mPlayerControlsListener;
    private OnPlayerStateListener mPlayerStateListener;

    private int songDuration;
    private boolean landscape = false;

    private View.OnClickListener onPlay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPlayerControlsListener.onPauseTouch();
            mPlayerStateListener.requestPlayState();
        }
    };

    private View.OnClickListener onNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPlayerControlsListener.onNextTouch();
        }
    };

    private View.OnClickListener onPrev = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPlayerControlsListener.onPrevTouch();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_player, container, false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landscape = true;
        }
        startPlayer();
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mSeekBarListener = (OnSeekBarChangeListener) activity;
            mPlayerControlsListener = (OnPlayerControlsListener) activity;
            mPlayerStateListener = (OnPlayerStateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + e.getLocalizedMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSeekBarListener = null;
        mPlayerStateListener = null;
        mPlayerControlsListener = null;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(int progress);
        void onStartTrackingTouch(SeekBar seekBar);
        void onStopTrackingTouch(SeekBar seekBar);
    }

    public interface OnPlayerControlsListener{
        void onPauseTouch();
        void onNextTouch();
        void onPrevTouch();
        void onRepeatTouch();
        void onShuffleTouch();

    }

    public interface OnPlayerStateListener{
        void requestPlayState();
        void requestShuffleState();
        void requestRepeatState();
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mSeekBarListener != null && fromUser) {
            mSeekBarListener.onProgressChanged(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void startPlayer() {
        //initialize mini player controls
        btnPrevMini = (ImageButton) mView.findViewById(R.id.player_handle_button_previous);
        btnPlayMini = (ImageButton) mView.findViewById(R.id.player_handle_button_play);
        btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
        btnNextMini = (ImageButton) mView.findViewById(R.id.player_handle_button_next);

        //initialize player controls
        ivAlbumArt = (ImageView) mView.findViewById(R.id.player_album_art);
        btnPlay = (ImageButton) mView.findViewById(R.id.btnPlay);
        btnNext = (ImageButton) mView.findViewById(R.id.btnNext);
        btnPrev = (ImageButton) mView.findViewById(R.id.btnPrevious);
        btnRepeat = (ImageButton) mView.findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) mView.findViewById(R.id.btnShuffle);
        btnPlayList = (ImageButton) mView.findViewById(R.id.btnPlaylist);
        tvStartTime = (TextView) mView.findViewById(R.id.songCurrentDurationLabel);
        tvEndTime = (TextView) mView.findViewById(R.id.songTotalDurationLabel);
        tvSongTitle = (TextView) mView.findViewById(R.id.songTitle);
        songProgressBar = (SeekBar) mView.findViewById(R.id.songProgressBar);
        songProgressBar.setOnSeekBarChangeListener(this);

        layoutMiniPlayer = (LinearLayout) mView.findViewById(R.id.player_handle_container);


        slidingDrawer = (SlidingUpPanelLayout) getActivity().findViewById(R.id.fragment_sliding_drawer);
        slidingDrawer.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener()
        {
            @Override
            public void onPanelSlide(View panel, float slideOffset)
            {
            }

            @Override
            public void onPanelHidden(View panel)
            {
            }

            @Override
            public void onPanelExpanded(View panel)
            {
                layoutMiniPlayer.setVisibility(View.GONE);
                btnPlayList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelCollapsed(View panel)
            {
                layoutMiniPlayer.setVisibility(View.VISIBLE);
                btnPlayList.setVisibility(View.GONE);
            }

            @Override
            public void onPanelAnchored(View panel)
            {
            }
        });

        btnPlay.setOnClickListener(onPlay);
        btnPlayMini.setOnClickListener(onPlay);

        btnNext.setOnClickListener(onNext);
        btnNextMini.setOnClickListener(onNext);

        btnPrev.setOnClickListener(onPrev);
        btnPrevMini.setOnClickListener(onPrev);

        btnRepeat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPlayerControlsListener.onRepeatTouch();
                mPlayerStateListener.requestRepeatState();
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPlayerControlsListener.onShuffleTouch();
                mPlayerStateListener.requestShuffleState();
            }
        });
    }

    public void setCurrentSong(String name, int duration){
        songDuration = duration;
        tvSongTitle.setText(name);
        tvStartTime.setText("0:00");
        tvEndTime.setText(formatTime(songDuration));
        songProgressBar.setProgress(0);
        songProgressBar.setMax(songDuration);
    }

    public void updateProgress(int progress){
        songProgressBar.setProgress(progress);
        tvStartTime.setText(formatTime(progress));
        tvEndTime.setText(formatTime(songDuration - progress));
    }

    private String formatTime(int time)
    {
        String formattedTime = "";
        formattedTime = String.format("%01d:%02d", time / 60, time % 60);
        return formattedTime;
    }

    public void setPlayState(PlayState state) {
        if(state == PlayState.PLAYING){
            if(landscape){
                btnPlay.setImageResource(R.drawable.btn_pause_large);
            }else{
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
            btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
        }else{
            if(landscape){
                btnPlay.setImageResource(R.drawable.btn_play_large);
            }else{
                btnPlay.setImageResource(R.drawable.btn_play);
            }
            btnPlayMini.setImageResource(R.drawable.btn_play_mini);
        }
    }

    public void setShuffleState(ShuffleState state) {
        if(state == ShuffleState.ON){
            btnShuffle.setImageResource(R.drawable.btn_shuffle_on);
        }else{
            btnShuffle.setImageResource(R.drawable.btn_shuffle_off);
        }
    }

    public void setRepeatState(RepeatState state) {
        if(state == RepeatState.ALL){
            btnRepeat.setImageResource(R.drawable.btn_repeat_all);
        }else if(state == RepeatState.ONE){
            btnRepeat.setImageResource(R.drawable.btn_repeat_one);
        }else if(state == RepeatState.NONE){
            btnRepeat.setImageResource(R.drawable.btn_repeat_none);
        }
        else{
            Log.w(TAG,"Received unexpected repeat state");
        }
    }
}
