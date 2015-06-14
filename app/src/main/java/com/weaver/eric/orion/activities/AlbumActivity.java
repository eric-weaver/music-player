package com.weaver.eric.orion.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.weaver.eric.orion.loaders.ArtistAlbumLoader;
import com.weaver.eric.orion.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Album>> {
	//tag for logs
	private static final String TAG = "AlbumActivity";

	private Long mArtistId;

	private ArrayList<Album> contentList;
	private ArrayAdapter<Album> albumAdapter;
	private ListView musicList;

	private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Long albumId = contentList.get(position).getId();
			Intent myIntent = new Intent(view.getContext(), SongActivity.class);
			myIntent.putExtra("key", albumId); //Optional parameters
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

		mArtistId = getIntent().getLongExtra("key", 0);

		initialize();
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

	@Override
	public Loader<List<Album>> onCreateLoader(int id, Bundle args) {
		return new ArtistAlbumLoader(this, mArtistId);
	}

	@Override
	public void onLoadFinished(Loader<List<Album>> loader, List<Album> data) {
		contentList = new ArrayList<>(data);
		musicList.setAdapter(new AlbumTabItemAdapter(this, R.layout.item_simple_image, contentList));
		albumAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<List<Album>> loader) {
		musicList.setAdapter(null);
	}

	private void initialize()
	{
		musicList = (ListView) findViewById(R.id.list_simple);
		albumAdapter =  new AlbumTabItemAdapter(this, R.layout.item_simple_image, new ArrayList<Album>());
		musicList.setAdapter(albumAdapter);
		musicList.setOnItemClickListener(listClickListener);
		getSupportLoaderManager().initLoader(0, null, this);
	}
}
