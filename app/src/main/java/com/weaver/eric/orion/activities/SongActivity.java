package com.weaver.eric.orion.activities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.weaver.eric.orion.adapters.SimpleSongAdapter;
import com.weaver.eric.orion.objects.Song;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongActivity extends BaseActivity {

    //tag for logs
    private static final String TAG = "SongActivity";

    private ArrayList<Song> contentList;

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

        String value = getIntent().getStringExtra("key");

        initialize(value);
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
