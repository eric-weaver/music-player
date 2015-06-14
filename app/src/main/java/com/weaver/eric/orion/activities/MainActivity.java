package com.weaver.eric.orion.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.adapters.TabPagerItem;
import com.weaver.eric.orion.adapters.TabsPagerAdapter;
import com.weaver.eric.orion.fragments.AlbumTabHostFragment;
import com.weaver.eric.orion.fragments.ArtistTabHostFragment;
import com.weaver.eric.orion.fragments.SongTabHostFragment;
import com.weaver.eric.orion.models.Song;
import com.weaver.eric.orion.view.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements SongTabHostFragment.OnPlaylistChangeListener {
	
	public static final String PREFS_NAME = "MyPrefsFile";

	private ViewPager viewPager;
	private SlidingTabLayout mSlidingTabLayout;
	private TabsPagerAdapter mAdapter;

	private boolean loaded = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_sliding_drawer);
		LinearLayout container = (LinearLayout)findViewById(R.id.container_drawer);
		LayoutInflater layoutInflater =
				(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View addView = layoutInflater.inflate(R.layout.fragment_main, null);
		container.addView(addView);

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		mAdapter.addTab(new TabPagerItem(new ArtistTabHostFragment(),getString(R.string.tab_artist_name), Color.BLUE, Color.GRAY));
		mAdapter.addTab(new TabPagerItem(new AlbumTabHostFragment(),getString(R.string.tab_album_name), Color.BLUE, Color.GRAY));
		mAdapter.addTab(new TabPagerItem(new SongTabHostFragment(), getString(R.string.tab_song_name), Color.BLUE, Color.GRAY));
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(mAdapter);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(viewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) 
		{
		case R.id.action_settings:

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		super.onSaveInstanceState(savedInstanceState);
		
	}

	@Override
	public void setPlaylist(ArrayList<Song> list, int position) {
		if(musicBound){
			musicSrv.setList(list);
			musicSrv.setSong(position);
			musicSrv.playSong();
		}
	}
}