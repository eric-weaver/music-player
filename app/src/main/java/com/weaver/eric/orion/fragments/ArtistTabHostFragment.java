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

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.activities.AlbumActivity;
import com.weaver.eric.orion.adapters.ArtistTabItemAdapter;
import com.weaver.eric.orion.models.Artist;

public class ArtistTabHostFragment extends Fragment implements
		OnItemClickListener
{
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
		contentList = new ArrayList<Artist>();
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		contentList = getArtistItems();
		artistAdapter =  new ArtistTabItemAdapter(this.getActivity(), R.layout.item_simple, contentList);
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(this);
	}

	private ArrayList<Artist> getArtistItems()
	{
		ContentResolver cr = mView.getContext().getContentResolver();
		Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
		String[] columns =
		{ MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS };
		String sort = MediaStore.Audio.AudioColumns.ARTIST + " ASC";
		ArrayList<Artist> listItems = new ArrayList<Artist>();
		try
		{
			Cursor cursor = cr.query(uri, columns, null, null, sort);
			long id;
			String title;
			String numAlbums;
			String numSongs;
			
			Artist item;
			while (cursor.moveToNext())
			{
				id = cursor
						.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
				title = cursor
						.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
				numAlbums = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
				numSongs = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
				if (title.length() > 0)
				{
					item = new Artist(id, title, numAlbums, numSongs);
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
