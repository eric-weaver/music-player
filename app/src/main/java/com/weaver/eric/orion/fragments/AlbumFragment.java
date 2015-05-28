package com.weaver.eric.orion.fragments;


import java.util.ArrayList;

import android.content.Intent;
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
import com.weaver.eric.orion.activities.SongActivity;
import com.weaver.eric.orion.adapters.CustomArrayAdapter;
import com.weaver.eric.orion.managers.MusicManager;

public class AlbumFragment extends Fragment implements OnItemClickListener
{
	private ListView musicList;
	
	private View mView;

	private MusicManager musicManager;
	
	private ArrayList<String> contentList;
	private ArrayAdapter<String> artistAdapter;
	private CustomArrayAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
		mView = rootView;
		String value = getArguments().getString("key");
		initialize(value);
		return rootView;
	}
	
	private void initialize(String value)
	{
		adapter = new CustomArrayAdapter();
		contentList = new ArrayList<String>();
		musicManager = new MusicManager(getActivity());
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		
		contentList = (ArrayList<String>) musicManager.getArtistAlbums(value);
		artistAdapter = adapter.CreateArrayAdapter(contentList, getActivity(), Color.WHITE);
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		String value = ((TextView) v.findViewById(android.R.id.text1)).getText().toString();
		
		Intent myIntent = new Intent(getActivity(), SongActivity.class);
		myIntent.putExtra("key", value); //Optional parameters
		this.startActivity(myIntent);
		
	}
}