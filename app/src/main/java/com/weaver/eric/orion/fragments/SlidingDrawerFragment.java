package com.weaver.eric.orion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weaver.eric.orion.R;

public class SlidingDrawerFragment extends Fragment
{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_sliding_drawer, container, false);
		Fragment playerFragment = new PlayerFragment();
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.add(R.id.container_drawer_player, playerFragment).commit();

		return rootView;
	}
}
