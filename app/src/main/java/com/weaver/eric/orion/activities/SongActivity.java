package com.weaver.eric.orion.activities;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.adapters.SimpleSongAdapter;
import com.weaver.eric.orion.fragments.PlayerFragment;
import com.weaver.eric.orion.objects.Song;
import com.weaver.eric.orion.services.MusicService;
import com.weaver.eric.orion.services.MusicService.MusicBinder;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongActivity extends ActionBarActivity {

    //tag for logs
    private static final String TAG = "SongActivity";

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    private ArrayList<Song> contentList;

    private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(musicBound) {
                musicSrv.setSong(position);
                musicSrv.playSong();
            }
        }
    };
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            musicSrv.setList(contentList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sliding_drawer);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");

        Fragment playerFragment = new PlayerFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_drawer_player, playerFragment).commit();

        initialize(value);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
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
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    private void initialize(String value) {
        contentList = new ArrayList<>();
        contentList = getAlbumSongs(value);
        ArrayAdapter<Song> songAdapter = new SimpleSongAdapter(this, contentList);
        ListView musicList = (ListView) findViewById(R.id.list_simple);
        musicList.setAdapter(songAdapter);
        musicList.setOnItemClickListener(listClickListener);
    }

    private ArrayList<Song> getAlbumSongs(String album) {
        ContentResolver cr = getContentResolver();

        String sort = MediaStore.Audio.AudioColumns.TITLE + " ASC";
        String where = MediaStore.Audio.Albums.ALBUM + "=?";
        String[] selection = {album};
        String[] columns = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID};
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ArrayList<Song> listItems = new ArrayList<>();
        Map<Long, Bitmap> albumCache = new HashMap<>();
        try {
            String name;
            Bitmap albumCover;
            long id;
            long albumId;
            Song song;

            Cursor cursor = cr.query(uri, columns, where, selection, sort);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                if (name.length() > 0) {
                    if(!albumCache.containsKey(albumId)){
                        albumCover = getAlbumArt(albumId);
                        albumCache.put(albumId, albumCover);
                    }
                    else{
                        albumCover = albumCache.get(albumId);
                    }
                    song = new Song(id,name, albumCover);
                    listItems.add(song);
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return listItems;
    }

    private Bitmap getAlbumArt(Long album_id)
    {
        Bitmap bm = null;
        try
        {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

            ParcelFileDescriptor pfd = getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null)
            {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return bm;
    }
}
