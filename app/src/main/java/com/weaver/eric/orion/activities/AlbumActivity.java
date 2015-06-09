package com.weaver.eric.orion.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.adapters.AlbumTabItemAdapter;
import com.weaver.eric.orion.objects.AlbumTabItem;

import java.util.ArrayList;

public class AlbumActivity extends BaseActivity
{
	//tag for logs
	private static final String TAG = "AlbumActivity";

	private ArrayList<AlbumTabItem> contentList;

	private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String value = contentList.get(position).getTitle();
			Intent myIntent = new Intent(view.getContext(), SongActivity.class);
			myIntent.putExtra("key", value); //Optional parameters
			view.getContext().startActivity(myIntent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_sliding_drawer);
		LinearLayout container = (LinearLayout)findViewById(R.id.container_drawer);
		LayoutInflater layoutInflater =
				(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View addView = layoutInflater.inflate(R.layout.fragment_list_layout, null);
		container.addView(addView);

		String value = getIntent().getStringExtra("key");

		initialize(value);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initialize(String value)
	{
		contentList = new ArrayList<>();
		ListView musicList = (ListView) findViewById(R.id.list_simple);
		contentList = getAlbumItems(value);
		ArrayAdapter<AlbumTabItem> artistAdapter =  new AlbumTabItemAdapter(this, R.layout.item_simple_image, contentList);
		musicList.setAdapter(artistAdapter);
		musicList.setOnItemClickListener(listClickListener);

	}

	private ArrayList<AlbumTabItem> getAlbumItems(String artist)
	{
		ContentResolver cr = this.getContentResolver();

		Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		String where = MediaStore.Audio.Albums.ARTIST + "=?";
		String[] selection = { artist };
		String[] columns =
				{ MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ALBUM_ART };
		String sort = MediaStore.Audio.AudioColumns.ALBUM + " ASC";
		ArrayList<AlbumTabItem> listItems = new ArrayList<>();
		try
		{
			Cursor cursor = cr.query(uri, columns, where, selection, sort);

			String title;
			String albumCover;
			String numSongs;

			AlbumTabItem item;
			while (cursor.moveToNext())
			{
				title = cursor
						.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
				albumCover = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
				numSongs = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));

				if (title.length() > 0)
				{
					item = new AlbumTabItem(title, albumCover, numSongs);
					listItems.add(item);
				}
			}
			cursor.close();
		} catch (Exception e)
		{
			Log.e(TAG, e.getLocalizedMessage(), e);
		}
		return listItems;
	}
}
