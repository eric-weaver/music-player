package com.weaver.eric.orion.fragments;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.adapters.CustomArrayAdapter;
import com.weaver.eric.orion.managers.MediaPlayerManager;

public class CurrentPlayListFragment extends Fragment implements OnItemClickListener
{
	private static final String TAG = "CurrentPlayListFragment";
	
	private View mView;
	
	private MediaPlayerManager mpManager;
	
	private ListView musicList;
	
	private ArrayAdapter<String> arrayAdapter;
	private CustomArrayAdapter adapter;
	private ArrayList<String> songList;
	private ArrayList<String> songPathList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		mView = inflater.inflate(R.layout.fragment_current_playlist, container, false);

		initialize();
		return mView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		String value = ((TextView) v.findViewById(android.R.id.text1)).getText().toString();
		
		mpManager.setFirstSong(value);
		mpManager.setSongUpdated(true);
		mpManager.playSong();
	}
	
	private void initialize()
	{
		mpManager = MediaPlayerManager.getInstance();
		adapter = new CustomArrayAdapter();
		songList = new ArrayList<String>();
		
		musicList = (ListView) mView.findViewById(R.id.fragment_current_playlist);
		
		songList = mpManager.getPlaylist();
		songPathList = mpManager.getPlaylistPaths();
		if(songList != null)
		{
			arrayAdapter = adapter.CreateArrayAdapter(songList, getActivity(), Color.WHITE);
			musicList.setAdapter(arrayAdapter);
			musicList.setOnItemClickListener(this);
		}
		else
		{
			Log.w(TAG, "Couldn't populate list current playlist is empty");
		}
		
	}
}
