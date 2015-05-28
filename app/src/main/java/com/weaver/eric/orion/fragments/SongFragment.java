package com.weaver.eric.orion.fragments;


import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.weaver.eric.orion.managers.MusicManager;

public class SongFragment extends Fragment implements OnItemClickListener
{
	private MusicManager musicManager;
	private MediaPlayerManager mpManager;
	private View mView;
	private ListView musicList;
	
	private ArrayAdapter<String> arrayAdapter;
	private CustomArrayAdapter adapter;
	private ArrayList<String> songList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
		mView = rootView;
		String value = getArguments().getString("key");
		initialize(value);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		String value = ((TextView) v.findViewById(android.R.id.text1)).getText().toString();
		
		mpManager.setFirstSong(value);
		mpManager.setSongUpdated(true);
		mpManager.setPlaylist(songList, musicManager.getPlaylistData(songList));
	}
	
	private void initialize(String value)
	{
		mpManager = MediaPlayerManager.getInstance();
		adapter = new CustomArrayAdapter();
		songList = new ArrayList<String>();
		musicManager = new MusicManager(getActivity());
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		
		songList = (ArrayList<String>) musicManager.getAlbumSongs(value);
		arrayAdapter = adapter.CreateArrayAdapter(songList, getActivity(), Color.WHITE);
		musicList.setAdapter(arrayAdapter);
		musicList.setOnItemClickListener(this);
	}
}