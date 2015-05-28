package com.weaver.eric.orion.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.adapters.TabPagerItem;
import com.weaver.eric.orion.adapters.TabsPagerAdapter;
import com.weaver.eric.orion.view.SlidingTabLayout;

public class MainFragment extends Fragment
{
	private ViewPager viewPager;
	private SlidingTabLayout mSlidingTabLayout;
    private TabsPagerAdapter mAdapter;
    private View mView;
    
    private boolean loaded = false;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {

	    mView = inflater.inflate(R.layout.fragment_main, container, false);
	    if(savedInstanceState != null)
	    {
	    	loaded = savedInstanceState.getBoolean("loaded", false);
	    }
	    
		// Initilization
		viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
		mAdapter = new TabsPagerAdapter(getChildFragmentManager());
		mAdapter.addTab(new TabPagerItem(new ArtistTabHostFragment(),getString(R.string.tab_artist_name), Color.BLUE, Color.GRAY));
		mAdapter.addTab(new TabPagerItem(new AlbumTabHostFragment(),getString(R.string.tab_album_name), Color.BLUE, Color.GRAY));
		mAdapter.addTab(new TabPagerItem(new SongTabHostFragment(),getString(R.string.tab_song_name), Color.BLUE, Color.GRAY));
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(mAdapter);
		mSlidingTabLayout = (SlidingTabLayout) mView.findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(viewPager);
		
		return mView;
	}
    
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		outState.putBoolean("loaded", loaded);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() 
	{		
		super.onResume();
	}
}
