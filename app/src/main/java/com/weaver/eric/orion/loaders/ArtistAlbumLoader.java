package com.weaver.eric.orion.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AlbumColumns;

import com.weaver.eric.orion.models.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 6/13/2015.
 */
public class ArtistAlbumLoader extends WrapperAsyncTaskLoader<List<Album>> {

    private final Long mArtistID;

    public ArtistAlbumLoader(final Context context, final Long artistId) {
        super(context);
        mArtistID = artistId;
    }

    @Override
    public List<Album> loadInBackground() {
        final ArrayList<Album> mAlbumsList = new ArrayList<>();
        Cursor mCursor = makeArtistAlbumCursor(getContext(), mArtistID);

        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                final long id = mCursor.getLong(0);

                final String albumName = mCursor.getString(1);

                final String artist = mCursor.getString(2);

                final int songCount = mCursor.getInt(3);

                final String year = mCursor.getString(4);

                final Album album = new Album(id, albumName, artist, songCount, year, null);

                mAlbumsList.add(album);
            } while (mCursor.moveToNext());
        }
        // Close the cursor
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mAlbumsList;
    }

    public static final Cursor makeArtistAlbumCursor(final Context context, final Long artistId) {
        return context.getContentResolver().query(
                Audio.Artists.Albums.getContentUri("external", artistId), new String[] {
                        /* 0 */
                        BaseColumns._ID,
                        /* 1 */
                        AlbumColumns.ALBUM,
                        /* 2 */
                        AlbumColumns.ARTIST,
                        /* 3 */
                        AlbumColumns.NUMBER_OF_SONGS,
                        /* 4 */
                        AlbumColumns.FIRST_YEAR
                }, null, null, AlbumColumns.ALBUM + " ASC");
    }
}