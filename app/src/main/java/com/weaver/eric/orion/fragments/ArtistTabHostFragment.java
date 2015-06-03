package com.weaver.eric.orion.fragments;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.weaver.eric.orion.activities.AlbumActivity;
import com.weaver.eric.orion.adapters.ArtistTabItemAdapter;
import com.weaver.eric.orion.objects.ArtistTabItem;

public class ArtistTabHostFragment extends Fragment implements
		OnItemClickListener
{
	private View mView;
	private ListView musicList;

	private ArrayList<ArtistTabItem> contentList;
	private ArrayAdapter<ArtistTabItem> artistAdapter;
	private ArtistTabItemAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_list_layout,
				container, false);
		mView = rootView;
		if (savedInstanceState != null)
		{
			// Restore last state for checked position.
		}
		initialize();
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id)
	{
		String value = contentList.get(position).getTitle();
		Intent myIntent = new Intent(getActivity(), AlbumActivity.class);
		myIntent.putExtra("key", value);
		getActivity().startActivity(myIntent);
	}

	private void initialize()
	{
		contentList = new ArrayList<ArtistTabItem>();
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		contentList = getArtistItems();
		artistAdapter =  new ArtistTabItemAdapter(this.getActivity(), R.layout.item_simple, contentList);
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(this);
	}

	private ArrayList<ArtistTabItem> getArtistItems()
	{
		ContentResolver cr = mView.getContext().getContentResolver();
		String where = null;
		Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
		String artist = MediaStore.Audio.Artists.ARTIST;
		String album = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;
		String song = MediaStore.Audio.Artists.NUMBER_OF_TRACKS;
		String[] columns =
		{ artist, album, song };
		String sort = MediaStore.Audio.AudioColumns.ARTIST + " ASC";
		ArrayList<ArtistTabItem> listItems = new ArrayList<ArtistTabItem>();
		try
		{
			Cursor cursor = cr.query(uri, columns, where, null, sort);
			String title = null;
			String numAlbums = null;
			String numSongs = null;
			
			ArtistTabItem item;
			while (cursor.moveToNext())
			{
				title = cursor
						.getString(cursor.getColumnIndexOrThrow(artist));
				numAlbums = cursor.getString(cursor
						.getColumnIndexOrThrow(album));
				numSongs = cursor.getString(cursor
						.getColumnIndexOrThrow(song));
				if (title.length() > 0)
				{
					item = new ArtistTabItem(title, numAlbums, numSongs);
					listItems.add(item);
				}
			}
			cursor.close();
		} catch (Exception e)
		{
			System.out.println("Failed to query Artists");
			System.out.println(e);
		}
		return listItems;
	}
}
