package com.weaver.eric.orion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.activities.SongActivity;
import com.weaver.eric.orion.adapters.AlbumTabItemAdapter;
import com.weaver.eric.orion.loaders.AlbumLoader;
import com.weaver.eric.orion.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumTabHostFragment extends Fragment implements OnItemClickListener, LoaderManager.LoaderCallbacks<List<Album>>{
	private View mView;
	private ListView musicList;

	private ArrayList<Album> contentList;
	private AlbumTabItemAdapter albumAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
		mView = rootView;
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		albumAdapter =  new AlbumTabItemAdapter(this.getActivity(), R.layout.item_simple_image, new ArrayList<Album>());
		musicList.setAdapter(albumAdapter);
		musicList.setOnItemClickListener(this);
		getLoaderManager().initLoader(0, null, this);
		return rootView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		Long value = contentList.get(position).getId();
		Intent myIntent = new Intent(getActivity(),SongActivity.class);
		myIntent.putExtra("key", value);
		this.startActivity(myIntent);
	}

	@Override
	public Loader<List<Album>> onCreateLoader(int id, Bundle args) {
		return new AlbumLoader(mView.getContext());
	}

	@Override
	public void onLoadFinished(Loader<List<Album>> loader, List<Album> data) {
		contentList = new ArrayList<>(data);
		albumAdapter =  new AlbumTabItemAdapter(this.getActivity(), R.layout.item_simple_image, contentList);
		musicList.setAdapter(albumAdapter);
		albumAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<List<Album>> loader) {
		musicList.setAdapter(null);
	}
}
