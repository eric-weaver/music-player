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
import com.weaver.eric.orion.activities.SongActivity;
import com.weaver.eric.orion.adapters.AlbumTabItemAdapter;
import com.weaver.eric.orion.models.Album;

public class AlbumTabHostFragment extends Fragment implements OnItemClickListener
{
	private View mView;
	private ListView musicList;

	private ArrayList<Album> contentList;
	private ArrayAdapter<Album> artistAdapter;
	private AlbumTabItemAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
		mView = rootView;
		initialize();
		return rootView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		String value = contentList.get(position).getTitle();
		Intent myIntent = new Intent(getActivity(),SongActivity.class);
		myIntent.putExtra("key", value);
		this.startActivity(myIntent);
	}
	
	private void initialize()
	{
		contentList = new ArrayList<Album>();
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		contentList = getAlbumItems();
		artistAdapter =  new AlbumTabItemAdapter(this.getActivity(), R.layout.item_simple_image, contentList);
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(this);
	}
	
	private ArrayList<Album> getAlbumItems()
	{
		ContentResolver cr = mView.getContext().getContentResolver();
		Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		String[] columns =
		{ MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ALBUM_ART };
		String sort = MediaStore.Audio.AudioColumns.ALBUM + " ASC";
		ArrayList<Album> listItems = new ArrayList<Album>();
		try
		{
			Cursor cursor = cr.query(uri, columns, null, null, sort);
			long id;
			String title;
			String albumCover;
			String numSongs;
			
			Album item;
			while (cursor.moveToNext())
			{
				id = cursor
						.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
				title = cursor
						.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
				albumCover = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
				numSongs = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
				
				if (title.length() > 0)
				{
					item = new Album(id, title, albumCover, numSongs);
					listItems.add(item);
				}
			}
			cursor.close();
		} catch (Exception e)
		{
			System.out.println("Failed to query Albums");
			System.out.println(e);
		}
		return listItems;
	}
}
