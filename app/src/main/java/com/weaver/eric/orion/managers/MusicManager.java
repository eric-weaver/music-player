package com.weaver.eric.orion.managers;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

public class MusicManager extends Application {
	
	private static Context mContext;

	public MusicManager(Context mContext) {
		MusicManager.mContext = mContext;
	}

	public static Context getContext() {
		return mContext;
	}

	public static void setContext(Context mContext) {
		MusicManager.mContext = mContext;
	}
	
	public List<String> getAllArtists()
	{
		Cursor cursor = null;
		MusicQueries query = new MusicQueries();
		cursor = query.getArtistCursor(mContext, cursor);
		List<String> listItems = new ArrayList<String>();
		String value = null;
		cursor.moveToFirst();
		while(cursor.moveToNext())
		{
			value = cursor.getString(cursor.getColumnIndexOrThrow("ARTIST"));
			if (value.length() > 0) {
		        
				// check if value already exists
		        if (!listItems.contains(value)) {
		            // doesn't exist, add it
		            listItems.add(value);
		        }
		    }
		}
		cursor.close();
		return listItems;
	}
	
	public List<String> getAllAlbums()
	{
		Cursor cursor = null;
		MusicQueries query = new MusicQueries();
		cursor = query.getAllAlbumCursor(mContext, cursor);
		List<String> listItems = new ArrayList<String>();
		
		String value = null;
		cursor.moveToFirst();
		while(cursor.moveToNext())
		{
			value = cursor.getString(cursor.getColumnIndexOrThrow("ALBUM"));
			if (value.length() > 0) {
		        
				// check if value already exists
		        if (!listItems.contains(value)) {
		            // doesn't exist, add it
		            listItems.add(value);
		        }
		    }
		}
		cursor.close();
		return listItems;
	}

	public List<String> getArtistAlbums(String artist)
	{
		Cursor cursor = null;		
		MusicQueries query = new MusicQueries();
		cursor = query.getArtistAlbumsCursor(mContext, cursor, artist);
		List<String> listItems = new ArrayList<String>();
		
		String value = null;
		cursor.moveToFirst();
		while(cursor.moveToNext())
		{
			value = cursor.getString(cursor.getColumnIndexOrThrow("ALBUM"));
			if (value.length() > 0) {
		        
				// check if value already exists
		        if (!listItems.contains(value)) {
		            // doesn't exist, add it
		            listItems.add(value);
		        }
		    }
		}
		cursor.close();
		return listItems;
	}
	
	public List<String> getAlbumSongs(String album)
	{
		Cursor cursor = null;		
		MusicQueries query = new MusicQueries();
		cursor = query.getAlbumSongsCursor(mContext, cursor, album);
		List<String> listItems = new ArrayList<String>();
		String value = null;
		cursor.moveToFirst();
		while(cursor.moveToNext())
		{
			value = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
			if (value.length() > 0) {
		        
				// check if value already exists
		        if (!listItems.contains(value)) {
		            // doesn't exist, add it
		            listItems.add(value);
		        }
		    }
		}
		cursor.close();
		return listItems;
	}

	public List<String> getAllSongs()
	{
		Cursor cursor = null;
		MusicQueries query = new MusicQueries();
		cursor = query.getAllSongsCursor(mContext, cursor);
		List<String> listItems = new ArrayList<String>();
		String value = null;
		cursor.moveToFirst();
		while(cursor.moveToNext())
		{
			value = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
			if (value.length() > 0) {
		        
				// check if value already exists
		        if (!listItems.contains(value)) {
		            // doesn't exist, add it
		            listItems.add(value);
		        }
		    }
		}
		cursor.close();
		return listItems;
	}
	
	public String getSongData(String songName)
	{
		Cursor cursor = null;
		MusicQueries query = new MusicQueries();
		cursor = query.playSongCursor(mContext, cursor, songName);
		String value = null;
		cursor.moveToFirst();
		value = cursor.getString(cursor.getColumnIndexOrThrow("_DATA"));
		cursor.close();
		return value;
	}
	
	public ArrayList<String> getPlaylistData(ArrayList<String> playlist)
	{
		ArrayList<String> playlistData = new ArrayList<String>();
		String value = null;
		for(int i = 0; i < playlist.size(); i++)
		{
			value = getSongData(playlist.get(i));
			if(value != null)
			{
				playlistData.add(value);
			}
		}
		return playlistData;
	}
}