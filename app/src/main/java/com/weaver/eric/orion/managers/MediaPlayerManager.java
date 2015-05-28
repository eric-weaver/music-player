package com.weaver.eric.orion.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import com.weaver.eric.orion.listeners.OnPlayerPreparedListener;
import com.weaver.eric.orion.listeners.OnSongCompletionListener;
import com.weaver.eric.orion.listeners.OnSongQueuedListener;


public class MediaPlayerManager implements MediaPlayer.OnCompletionListener, OnPreparedListener
{

	private static final String TAG = "MediaPlayerManager";
	
	private static MediaPlayerManager instance;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private String currentSong = "";
	private Integer currentSongIndex = 0;
	private int playlistCount;
	private int songsPlayedCount = 0;
	private int songDuration = 0;
	private int currentPosition = 0;
	private ArrayList<String> playlistPaths;
	private ArrayList<String> playlistFriendly;
	private OnSongCompletionListener onSongCompletionListener;
	private OnSongQueuedListener onSongQueuedListener;
	private OnPlayerPreparedListener onPlayerPreparedListener;
	private String repeat;
	private String firstSong;
	private boolean shuffle = false;
	private boolean songUpdated = false;
	private boolean paused = false;
	
	static {
		instance = new MediaPlayerManager();
	}

	private MediaPlayerManager() 
	{
		 repeat = "none";
		 shuffle = false;
	}

	public static MediaPlayerManager getInstance() 
	{
		return instance;
	}

	public boolean isPlaying() 
	{
		return mediaPlayer.isPlaying();
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public MediaPlayer getMediaPlayer()
	{
		return mediaPlayer;
	}
	
	public OnSongCompletionListener getOnSongCompletionListener() 
	{
		return onSongCompletionListener;
	}

	public void OnSongCompletionListener(OnSongCompletionListener listener) 
	{
		this.onSongCompletionListener = listener;
	}

	public OnSongQueuedListener getOnSongQueuedListener() 
	{
		return onSongQueuedListener;
	}

	public void OnSongQueuedListener(OnSongQueuedListener listener) 
	{
		this.onSongQueuedListener = listener;
	}
	
	public OnPlayerPreparedListener getOnPlayerPreparedListener() 
	{
		return onPlayerPreparedListener;
	}

	public void OnPlayerPreparedListener(OnPlayerPreparedListener listener) 
	{
		this.onPlayerPreparedListener = listener;
	}
	
	public ArrayList<String> getPlaylistPaths() 
	{
		return playlistPaths;
	}
	
	public ArrayList<String> getPlaylist() 
	{
		return playlistFriendly;
	}

	public void setPlaylist(ArrayList<String> playlist, ArrayList<String> playlistPaths) 
	{
		playlistCount = playlist.size();
		this.playlistFriendly = playlist;
		this.playlistPaths = playlistPaths;
		
		onSongQueuedListener.SongQueued();
		
	}
	
	public void setFirstSong(String song)
	{
		this.firstSong = song;
	}
	
	public String getFirstSong()
	{
		return firstSong;
	}
	
	public boolean isSongUpdated() 
	{
		return songUpdated;
	}

	public void setSongUpdated(boolean songUpdated) 
	{
		this.songUpdated = songUpdated;
	}
	
	public int getCurrentSongIndex() 
	{
		return currentSongIndex;
	}
	
	public String getCurrentSongPath() 
	{
		return currentSong;
	}
	
	public String getCurrentSongFriendly() 
	{
		return playlistFriendly.get(currentSongIndex);
	}
	
	public String getRepeat() 
	{
		return repeat;
	}

	public void setRepeat(String repeat) 
	{
		this.repeat = repeat;
	}

	public boolean isShuffle() 
	{
		return shuffle;
	}

	public void setShuffle(boolean shuffle) 
	{
		this.shuffle = shuffle;
	}
	
	public int getSongDuration() 
	{
		return songDuration / 1000;
	}
	
	public int getSongPosition()
	{
		if(mediaPlayer != null)
		{
			currentPosition = mediaPlayer.getCurrentPosition() / 1000;
			return currentPosition;
		}
		return 0;
	}
	
	public void SeekToPosition(int position)
	{
		mediaPlayer.seekTo(position * 1000);
	}

	private boolean songHandler(boolean userChanged)
	{
		String nextSong = null;
		try
		{
			if(repeat == "single")
			{
				nextSong = playlistPaths.get(currentSongIndex);
				playSong(nextSong);
			}
			else
			{
				songsPlayedCount++;
				if(shuffle == true)
				{
					Random rand = new Random();
					int nextIndex = rand.nextInt(playlistCount);
					currentSongIndex = nextIndex;
				}
				else
				{
					currentSongIndex = currentSongIndex + 1;
				}
			
				if(currentSongIndex < playlistCount)
				{
					nextSong = playlistPaths.get(currentSongIndex);
					playSong(nextSong);
				}
				else if(repeat == "all" || userChanged == true)
				{
					songsPlayedCount = 0;
					currentSongIndex = 0;
					nextSong = playlistPaths.get(currentSongIndex);
					playSong(nextSong);
				}
				else
				{
					return false;
				}
			}
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean nextSong(boolean userChanged)
	{
		return songHandler(userChanged);
	}
	
	public void prevSong()
	{
		String nextSong = null;
		currentSongIndex = currentSongIndex - 1;
		if (currentSongIndex > 0) {
			nextSong = playlistPaths.get(currentSongIndex);
			
			playSong(nextSong);
			
		} else {
			// play first song
			currentSongIndex = 0;
			nextSong = playlistPaths.get(currentSongIndex);
		    playSong(nextSong);
			
		}
	}

	public void playSong(String song) 
	{
		currentSong = song;
		
		currentSongIndex = playlistPaths.indexOf(currentSong);
		
		try 
		{
			releasePlayer();
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(currentSong);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(this);
			paused = false;
			songDuration = mediaPlayer.getDuration();
		} 
		catch (IllegalStateException | IllegalArgumentException | SecurityException | IOException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Media Player exception: ", e);
		}
	}
	
	public void playSong() 
	{
		if(firstSong != null)
		{
			currentSong = firstSong;
			currentSongIndex = playlistFriendly.indexOf(currentSong);
		}
		else if(playlistPaths != null)
		{
			currentSong = playlistFriendly.get(0);
		}
		else
		{
			Log.e(TAG, "Error starting player no playlist or song selected");
			return;
		}
		
		try 
		{
			releasePlayer();
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(playlistPaths.get(currentSongIndex));
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(this);
			paused = false;
			
		} 
		catch (IllegalStateException | IllegalArgumentException | SecurityException | IOException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Media Player exception: ", e);
		}
	}

	public void pauseSong() 
	{
		try 
		{
			if (mediaPlayer.isPlaying()) 
			{
				mediaPlayer.pause();
				paused = true;
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}

	public void resumeSong() 
	{
		if (mediaPlayer.isPlaying() == false) 
		{
			mediaPlayer.start();
			paused = false;
		}
	}
	
	private void releasePlayer() 
	{
        if( mediaPlayer == null) 
        {
            return;
        }
        
        if (mediaPlayer.isPlaying()) 
        {
            mediaPlayer.stop();
            paused = false;
        }
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		boolean success = nextSong(false);
		onSongCompletionListener.OnSongCompletion(success);
	}

	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		songDuration = mediaPlayer.getDuration();
		onPlayerPreparedListener.OnPlayerPrepared(currentSong);
	}
}