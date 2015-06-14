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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.activities.AlbumActivity;
import com.weaver.eric.orion.adapters.ArtistTabItemAdapter;
import com.weaver.eric.orion.loaders.ArtistLoader;
import com.weaver.eric.orion.models.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistTabHostFragment extends Fragment implements OnItemClickListener, LoaderManager.LoaderCallbacks<List<Artist>>{
	private View mView;
	private ListView musicList;

	private ArrayList<Artist> contentList;
	private ArrayAdapter<Artist> artistAdapter;
	private ArtistTabItemAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_list_layout,
				container, false);
		mView = rootView;
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		artistAdapter =  new ArtistTabItemAdapter(this.getActivity(), R.layout.item_simple, new ArrayList<Artist>());
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(this);
		getLoaderManager().initLoader(0, null, this);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id)
	{
		Long value = contentList.get(position).getId();
		Intent myIntent = new Intent(getActivity(), AlbumActivity.class);
		myIntent.putExtra("key", value);
		getActivity().startActivity(myIntent);
	}

	@Override
	public Loader<List<Artist>> onCreateLoader(int id, Bundle args) {
		return new ArtistLoader(mView.getContext());
	}

	@Override
	public void onLoadFinished(Loader<List<Artist>> loader, List<Artist> data) {
		contentList = new ArrayList<>(data);
		artistAdapter =  new ArtistTabItemAdapter(this.getActivity(), R.layout.item_simple, contentList);
		musicList.setAdapter(artistAdapter);
		artistAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<List<Artist>> loader) {
		musicList.setAdapter(null);
	}
}