package com.weaver.eric.orion.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.listeners.OnPlayerPreparedListener;
import com.weaver.eric.orion.listeners.OnSongCompletionListener;
import com.weaver.eric.orion.listeners.OnSongQueuedListener;
import com.weaver.eric.orion.managers.MediaPlayerManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class PlayerFragment extends Fragment implements OnSongCompletionListener, OnSeekBarChangeListener, OnSongQueuedListener, OnPlayerPreparedListener
{
	private static final String TAG = "PlayerFragment";
	
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
	
	private CurrentPlayListFragment playListFragment;
	
	public static final String PREFS_NAME = "MyPrefsFile";
	static final String key = "songName";
	private MediaPlayerManager mpManager;
	private String songTitle = "";
	private boolean playing = false;
	private String repeatState = "none";
	private String playerState = "stop";
	private boolean shuffleState = false;
	private boolean landscape = false;
	private int songDuration = 0;
	private boolean playlistShowing = false;

	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {

	    @Override
	    public void run() {
	        if(mpManager != null)
	        {
	        	if(songProgressBar != null)
	        	{
	        		int time = mpManager.getSongPosition();
	        		songProgressBar.setProgress(time);
	        		tvStartTime.setText(formatTime(time));
	        		tvEndTime.setText(formatTime(songDuration - time));
	        	}
	            
	        }
	        mHandler.postDelayed(this, 1000);
	    }
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_player, container, false);
		mView = rootView;
		
		if(savedInstanceState != null)
		{
			playing = savedInstanceState.getBoolean("playing");
			songTitle = savedInstanceState.getString("songTitle");
			playerState = savedInstanceState.getString("playerState", playerState);
			repeatState = savedInstanceState.getString("repeatState", repeatState);
		}
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			landscape = true;
		}
		
		startPlayer();
		
		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			getFragmentManager().popBackStack();
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
			return true;
		}
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
		savedInstanceState.putBoolean("playing", playing);
		savedInstanceState.putString("songTitle", songTitle);
		savedInstanceState.putString("playerState", playerState);
		savedInstanceState.putString("repeatState", repeatState);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onResume() 
	{
		mpManager.OnSongCompletionListener(this);
		mpManager.OnSongQueuedListener(this);
		mpManager.OnPlayerPreparedListener(this);
		
		if(mpManager.isPlaying())
		{
			prepProgressBar();
			mRunnable.run();
			displaySongTitle();
			if(landscape)
			{
				btnPlay.setImageResource(R.drawable.btn_pause_large);
				btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
			}
			else
			{
				btnPlay.setImageResource(R.drawable.btn_pause);
				btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
			}
		}
		else if(mpManager.isPaused())
		{
			prepProgressBar();
			mRunnable.run();
			displaySongTitle();
			if(landscape)
			{
				btnPlay.setImageResource(R.drawable.btn_play_large);
				btnPlayMini.setImageResource(R.drawable.btn_play_mini);
			}
			else
			{
				btnPlay.setImageResource(R.drawable.btn_play);
				btnPlayMini.setImageResource(R.drawable.btn_play_mini);
			}
		}
		if(mpManager.isShuffle())
		{
			btnShuffle.setImageResource(R.drawable.btn_shuffle_on);
		}
		else
		{
			btnShuffle.setImageResource(R.drawable.btn_shuffle_off);
		}
		
		String state = mpManager.getRepeat();
		if(state == null)
		{			
			btnRepeat.setImageResource(R.drawable.btn_repeat_none);
		}
		else if(state == "none")
		{
			btnRepeat.setImageResource(R.drawable.btn_repeat_none);
		}
		else if(state == "single")
		{
			btnRepeat.setImageResource(R.drawable.btn_repeat_one);
		}
		else if(state == "all")
		{
			btnRepeat.setImageResource(R.drawable.btn_repeat_all);
		}
		
		super.onResume();
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}

	private void startPlayer() 
	{
		mpManager = MediaPlayerManager.getInstance();
		
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
		slidingDrawer.setPanelSlideListener(new PanelSlideListener() 
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
		
		if(mpManager.isSongUpdated())
		{
			mpManager.setSongUpdated(false);
			mpManager.playSong();
			prepProgressBar();
			mRunnable.run();
			playerState = "play";
			displaySongTitle();
		}
		
		mpManager.OnSongCompletionListener(this);
		mpManager.OnSongQueuedListener(this);
		mpManager.OnPlayerPreparedListener(this);
		
		if(landscape)
		{
			if(playerState == "play")
			{
				btnPlay.setImageResource(R.drawable.btn_pause_large);
				btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
			}
			else
			{
				btnPlay.setImageResource(R.drawable.btn_play_large);
				btnPlayMini.setImageResource(R.drawable.btn_play_mini);
			}
		}
		else
		{
			if(playerState == "play")
			{
				btnPlay.setImageResource(R.drawable.btn_pause);
				btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
			}
			else
			{
				btnPlay.setImageResource(R.drawable.btn_play);
				btnPlayMini.setImageResource(R.drawable.btn_play_mini);
			}
		}
		
		btnPlayMini.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View arg0) 
            {
            	if(mpManager.getPlaylist() != null)
            	{
            		playSong();
            	}
            	else
            	{
            		Log.w(TAG, "Attempted to start music player with no songs selected");
            	}
            }
        });
		
		btnNextMini.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(mpManager.getPlaylist() != null)
            	{
					nextSong();
            	}
				else
				{
					Log.w(TAG, "Attempted to start music player with no songs selected");
				}
			}
		});
		
		btnPrevMini.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(mpManager.getPlaylist() != null)
            	{
					prevSong();
            	}
				else
				{
					Log.w(TAG, "Attempted to start music player with no songs selected");
				}
			}
		});
		
		btnPlay.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View arg0) {
            	if(mpManager.getPlaylist() != null)
            	{
            		playSong();
            	}
            	else
            	{
            		Log.w(TAG, "Attempted to start music player with no songs selected");
            	}
            }
        });
		
		btnNext.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				if(mpManager.getPlaylist() != null)
            	{
					nextSong();
            	}
				else
				{
					Log.w(TAG, "Attempted to start music player with no songs selected");
				}
			}
		});
		
		btnPrev.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				if(mpManager.getPlaylist() != null)
            	{
					prevSong();
            	}
				else
				{
					Log.w(TAG, "Attempted to start music player with no songs selected");
				}
			}
		});
		
		btnRepeat.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				repeatSong();
			}
		});
		
		btnShuffle.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				shuffleSong();
			}
		});
		
		btnPlayList.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				FragmentTransaction ft = getChildFragmentManager().beginTransaction();
				if(playlistShowing == false)
				{
					playlistShowing = true;
					ivAlbumArt.setVisibility(View.GONE);
					playListFragment = new CurrentPlayListFragment();
					ft.add(R.id.player_middle_bg, playListFragment).commit();
				}
				else
				{
					playlistShowing = false;
					ft.remove(playListFragment).commit();
					ivAlbumArt.setVisibility(View.VISIBLE);
				}
				
				
			}
		});
	}
	
	private void displaySongTitle()
	{
		songTitle = mpManager.getCurrentSongFriendly();
		tvSongTitle.setText(songTitle);
	}
	
	private void prepProgressBar()
	{
		songDuration = mpManager.getSongDuration();
		tvStartTime.setText("0:00");
		tvEndTime.setText(formatTime(songDuration));
		songProgressBar.setProgress(0);
		songProgressBar.setMax(songDuration);
	}
	
	private String formatTime(int time)
	{
		String formattedTime = "";
		formattedTime = String.format("%01d:%02d", time / 60, time % 60);
		return formattedTime;
	}
	
	private boolean prepNextSong()
	{
		return mpManager.nextSong(true);		
	}
	
	private void prepPrevSong()
	{
		mpManager.prevSong();
	}
	
	private void playSong()
	{
		// check for already playing
        if(mpManager.isPlaying()){
            if(mpManager!=null){
                mpManager.pauseSong();
                // Changing button image to play button
                if(landscape)
        		{
                	btnPlay.setImageResource(R.drawable.btn_play_large);
                	btnPlayMini.setImageResource(R.drawable.btn_play_mini); 
                	
        		}
                else
                {
                	btnPlay.setImageResource(R.drawable.btn_play);
                	btnPlayMini.setImageResource(R.drawable.btn_play_mini); 
                }

                playerState = "pause";
            }
        }else{
            // Resume song
            if(mpManager!=null){
                mpManager.resumeSong();
                // Changing button image to pause button
                if(landscape)
        		{
                	btnPlay.setImageResource(R.drawable.btn_pause_large);
                	btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
        		}
                else
                {
                	btnPlay.setImageResource(R.drawable.btn_pause);
                	btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
                }
                
                
                playerState = "play";
            }
        }
	}
	
	private void nextSong()
	{
		if(prepNextSong())
		{
			displaySongTitle();
			prepProgressBar();
		}
		if(landscape)
 		{
         	btnPlay.setImageResource(R.drawable.btn_pause_large);
         	btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
 		}
        else
        {
        	btnPlay.setImageResource(R.drawable.btn_pause);
        	btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
        }
	}
	
	private void prevSong()
	{
		prepPrevSong();
		displaySongTitle();
		prepProgressBar();
		if(landscape)
 		{
         	btnPlay.setImageResource(R.drawable.btn_pause_large);
         	btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
 		}
        else
        {
        	btnPlay.setImageResource(R.drawable.btn_pause);
        	btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
        }
	}
	
	private void repeatSong()
	{
		repeatState = mpManager.getRepeat();
		if(repeatState == null)
		{
			repeatState = "none";
			
			btnRepeat.setImageResource(R.drawable.btn_repeat_none);
		}
		else if(repeatState == "none")
		{
			repeatState = "single";
			btnRepeat.setImageResource(R.drawable.btn_repeat_one);
		}
		else if(repeatState == "single")
		{
			repeatState = "all";
			btnRepeat.setImageResource(R.drawable.btn_repeat_all);
		}
		else
		{
			repeatState = "none";
			btnRepeat.setImageResource(R.drawable.btn_repeat_none);
		}
		mpManager.setRepeat(repeatState);
	}
	
	private void shuffleSong()
	{
		shuffleState = mpManager.isShuffle();
		if(shuffleState == false)
		{
			shuffleState = true;
			btnShuffle.setImageResource(R.drawable.btn_shuffle_on);
		}
		else
		{
			shuffleState = false;
			btnShuffle.setImageResource(R.drawable.btn_shuffle_off);
		}
		mpManager.setShuffle(shuffleState);
	}

	@Override
	public void OnSongCompletion(boolean nextSong) 
	{
		if(nextSong)
		{
			displaySongTitle();
			prepProgressBar();
		}
		else
		{
			if(landscape)
    		{
            	btnPlay.setImageResource(R.drawable.btn_play_large);
            	btnPlayMini.setImageResource(R.drawable.btn_play_mini); 
    		}
            else
            {
            	btnPlay.setImageResource(R.drawable.btn_play);
            	btnPlayMini.setImageResource(R.drawable.btn_play_mini); 
            }
		}
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) 
	{
		if(mpManager != null && fromUser)
		{
			mpManager.SeekToPosition(position);
			int time = mpManager.getSongPosition();
			tvStartTime.setText(formatTime(time));
    		tvEndTime.setText(formatTime(songDuration - time));
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) 
	{

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) 
	{
		
	}

	@Override
	public void SongQueued() 
	{
		if(mpManager.isSongUpdated())
		{
			mpManager.setSongUpdated(false);
			mpManager.playSong();
			prepProgressBar();
			mRunnable.run();
			playerState = "play";
			displaySongTitle();
			if(landscape)
			{
				if(playerState == "play")
				{
					btnPlay.setImageResource(R.drawable.btn_pause_large);
					btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
				}
				else
				{
					btnPlay.setImageResource(R.drawable.btn_play_large);
					btnPlayMini.setImageResource(R.drawable.btn_play_mini);
				}
			}
			else
			{
				if(playerState == "play")
				{
					btnPlay.setImageResource(R.drawable.btn_pause);
					btnPlayMini.setImageResource(R.drawable.btn_pause_mini);
				}
				else
				{
					btnPlay.setImageResource(R.drawable.btn_play);
					btnPlayMini.setImageResource(R.drawable.btn_play_mini);
				}
			}
		}
	}

	@Override
	public void OnPlayerPrepared(String songName) 
	{
		displaySongTitle();
		prepProgressBar();
	
		if(landscape)
		{
        	btnPlay.setImageResource(R.drawable.btn_pause_large);
        	btnPlayMini.setImageResource(R.drawable.btn_pause_mini); 
		}
        else
        {
        	btnPlay.setImageResource(R.drawable.btn_pause);
        	btnPlayMini.setImageResource(R.drawable.btn_pause_mini); 
        }
	}
}