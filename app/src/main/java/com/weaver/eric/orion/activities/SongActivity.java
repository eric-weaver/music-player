package com.weaver.eric.orion.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v4.app.LoaderManager;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.adapters.SimpleSongAdapter;
import com.weaver.eric.orion.loaders.AlbumSongLoader;
import com.weaver.eric.orion.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Song>> {

    //tag for logs
    private static final String TAG = "SongActivity";

    private Long mAlbumId;

    private ArrayList<Song> contentList;
    private ArrayAdapter<Song> songAdapter;
    private ListView musicList;

    private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(musicBound) {
                musicSrv.setList(contentList);
                musicSrv.setSong(position);
                musicSrv.playSong();
            }
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

        mAlbumId = getIntent().getLongExtra("key", 0);

        songAdapter = new SimpleSongAdapter(this, new ArrayList<Song>());
        musicList = (ListView) findViewById(R.id.list_simple);
        musicList.setAdapter(songAdapter);
        musicList.setOnItemClickListener(listClickListener);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.song, menu);
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
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        return new AlbumSongLoader(this, mAlbumId);
    }

    @Override
    public void onLoadFinished(Loader<List<Song>> loader, List<Song> data) {
        contentList = new ArrayList<>(data);
        musicList.setAdapter(new SimpleSongAdapter(this, contentList));
        songAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Song>> loader) {
        musicList.setAdapter(null);
    }
}
