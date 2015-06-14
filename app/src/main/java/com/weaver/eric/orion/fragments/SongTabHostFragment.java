package com.weaver.eric.orion.fragments;

import java.util.ArrayList;

import android.content.ContentResolver;
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
import com.weaver.eric.orion.adapters.SongTabItemAdapter;
import com.weaver.eric.orion.managers.MediaPlayerManager;
import com.weaver.eric.orion.models.SongTabItem;

public class SongTabHostFragment extends Fragment implements OnItemClickListener
{
	private MediaPlayerManager mpManager;
	
	private View mView;
	private ListView musicList;

	private ArrayList<SongTabItem> contentList;
	private ArrayAdapter<SongTabItem> artistAdapter;
	private SongTabItemAdapter adapter;
	
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
//		String value = contentList.get(position).getTitle();
//		mpManager.setPlaylist(contentList, musicManager.getPlaylistData(songList));
//		mpManager.setFirstSong(value);
//		mpManager.setSongUpdated(true);
//		Intent myIntent = new Intent(getActivity(), PlayerActivity.class);
//		myIntent.putExtra("key", value);
//		this.startActivity(myIntent);
		
	}
	
	private void initialize()
	{
		mpManager = MediaPlayerManager.getInstance();
		contentList = new ArrayList<SongTabItem>();
		musicList = (ListView) mView.findViewById(R.id.list_simple);
		contentList = getSongItems();
		artistAdapter =  new SongTabItemAdapter(this.getActivity(), R.layout.item_simple, contentList);
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(this);
	}
	
	private ArrayList<SongTabItem> getSongItems()
	{
		ContentResolver cr = mView.getContext().getContentResolver();
		String where = null;
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String artist = MediaStore.Audio.Media.ARTIST;
		String song = MediaStore.Audio.Media.TITLE;
		String[] columns =
		{ artist, song };
		String sort = MediaStore.Audio.AudioColumns.TITLE + " ASC";
		ArrayList<SongTabItem> listItems = new ArrayList<SongTabItem>();
		try
		{
			Cursor cursor = cr.query(uri, columns, where, null, sort);
			String title = null;
			String artistName = null;
			
			SongTabItem item;
			while (cursor.moveToNext())
			{
				title = cursor
						.getString(cursor.getColumnIndexOrThrow(song));
				artistName = cursor.getString(cursor
						.getColumnIndexOrThrow(artist));
				
				if (title.length() > 0)
				{
					item = new SongTabItem(title, artistName);
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
