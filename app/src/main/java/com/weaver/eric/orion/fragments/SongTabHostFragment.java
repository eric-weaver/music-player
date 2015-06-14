package com.weaver.eric.orion.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.adapters.SongTabItemAdapter;
import com.weaver.eric.orion.loaders.SongLoader;
import com.weaver.eric.orion.managers.MediaPlayerManager;
import com.weaver.eric.orion.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongTabHostFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Song>> {
	private MediaPlayerManager mpManager;

	private View mView;
	private ListView musicList;

	private ArrayList<Song> contentList;
	private ArrayAdapter<Song> songAdapter;
	private OnPlaylistChangeListener mPlaylistListener;

	private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mPlaylistListener.setPlaylist(contentList, position);
		}
	};

	public interface OnPlaylistChangeListener{
		void setPlaylist(ArrayList<Song> list, int position);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
		mView = rootView;
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		songAdapter = new SongTabItemAdapter(this.getActivity(), R.layout.item_simple, new ArrayList<Song>());
		musicList.setAdapter(songAdapter);
		musicList.setOnItemClickListener(listClickListener);
		getLoaderManager().initLoader(0, null, this);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mPlaylistListener = (OnPlaylistChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ e.getLocalizedMessage());
		}
	}

	@Override
	public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
		return new SongLoader(mView.getContext());
	}

	@Override
	public void onLoadFinished(Loader<List<Song>> loader, List<Song> data) {
		contentList = new ArrayList<>(data);
		songAdapter =  new SongTabItemAdapter(this.getActivity(), R.layout.item_simple, contentList);
		musicList.setAdapter(songAdapter);
		songAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<List<Song>> loader) {
		musicList.setAdapter(null);
	}
}
