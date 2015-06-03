package com.weaver.eric.orion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.fragments.MainFragment;
import com.weaver.eric.orion.fragments.PlayerFragment;

public class MainActivity extends BaseActivity
{
	
	public static final String PREFS_NAME = "MyPrefsFile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_sliding_drawer);
		
		Intent intent = getIntent();
		String value = intent.getStringExtra("key");
		Bundle bundle=new Bundle();
		bundle.putString("key", value);
		
		Fragment playerFragment = new PlayerFragment();
		Fragment fragment = new MainFragment();
		fragment.setArguments(bundle);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.container_drawer_player, playerFragment).add(R.id.container_drawer, fragment).commit();
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
	protected void onPause()
	{
		super.onPause();
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
}