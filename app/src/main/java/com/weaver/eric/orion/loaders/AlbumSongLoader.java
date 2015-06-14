package com.weaver.eric.orion.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;

import com.weaver.eric.orion.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 6/13/2015.
 */
public class AlbumSongLoader extends WrapperAsyncTaskLoader<List<Song>> {

    private final Long mAlbumId;

    public AlbumSongLoader(Context context, long albumId) {
        super(context);
        this.mAlbumId = albumId;
    }

    @Override
    public List<Song> loadInBackground() {
        final ArrayList<Song> mSongList = new ArrayList<>();
        Cursor mCursor = makeSongCursor(getContext(), mAlbumId);

        if(mCursor != null && mCursor.moveToFirst()){
            do{
                final long id = mCursor.getLong(0);

                final String songName = mCursor.getString(1);

                final String artist = mCursor.getString(2);

                final String album = mCursor.getString(3);

                final long duration = mCursor.getLong(4);

                final int durationInSecs = (int) duration / 1000;

                final Song song = new Song(id, songName, artist, album, durationInSecs);

                // Add everything up
                mSongList.add(song);

            }while(mCursor.moveToNext());
        }

        if(mCursor != null){
            mCursor.close();
            mCursor = null;
        }
        return mSongList;
    }

    public static final Cursor makeSongCursor(final Context context, final Long albumId) {
        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + AudioColumns.TITLE + " != ''"); //$NON-NLS-2$
        mSelection.append(" AND " + AudioColumns.ALBUM_ID + "=" + albumId);
        return context.getContentResolver().query(Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        /* 0 */
                        BaseColumns._ID,
                        /* 1 */
                        AudioColumns.TITLE,
                        /* 2 */
                        AudioColumns.ARTIST,
                        /* 3 */
                        Audio.AudioColumns.ALBUM,
                        /* 4 */
                        Audio.AudioColumns.DURATION
                }, mSelection.toString(), null,
                    Audio.AudioColumns.TITLE + " ASC");
    }
}
