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
import com.weaver.eric.orion.objects.AlbumTabItem;

public class AlbumTabHostFragment extends Fragment implements OnItemClickListener
{
	private View mView;
	private ListView musicList;

	private ArrayList<AlbumTabItem> contentList;
	private ArrayAdapter<AlbumTabItem> artistAdapter;
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
		contentList = new ArrayList<AlbumTabItem>();
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		contentList = getAlbumItems();
		artistAdapter =  new AlbumTabItemAdapter(this.getActivity(), R.layout.item_simple_image, contentList);
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(this);
	}
	
	private ArrayList<AlbumTabItem> getAlbumItems()
	{
		ContentResolver cr = mView.getContext().getContentResolver();
		String where = null;
		Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		String album = MediaStore.Audio.Albums.ALBUM;
		String song = MediaStore.Audio.Albums.NUMBER_OF_SONGS;
		String albumArt = MediaStore.Audio.Albums.ALBUM_ART;
		String[] columns =
		{ album, song, albumArt };
		String sort = MediaStore.Audio.AudioColumns.ALBUM + " ASC";
		ArrayList<AlbumTabItem> listItems = new ArrayList<AlbumTabItem>();
		try
		{
			Cursor cursor = cr.query(uri, columns, where, null, sort);
			String title = null;
			String albumCover = null;
			String numSongs = null;
			
			AlbumTabItem item;
			while (cursor.moveToNext())
			{
				title = cursor
						.getString(cursor.getColumnIndexOrThrow(album));
				albumCover = cursor.getString(cursor
						.getColumnIndexOrThrow(albumArt));
				numSongs = cursor.getString(cursor
						.getColumnIndexOrThrow(song));
				
				if (title.length() > 0)
				{
					item = new AlbumTabItem(title, albumCover, numSongs);
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
