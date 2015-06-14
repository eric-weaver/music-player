package com.weaver.eric.orion.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.ArtistColumns;
import android.provider.BaseColumns;

import com.weaver.eric.orion.models.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 6/13/2015.
 */
public class ArtistLoader extends WrapperAsyncTaskLoader<List<Artist>> {

    public ArtistLoader(final Context context) {
        super(context);
    }

    @Override
    public List<Artist> loadInBackground() {
        final ArrayList<Artist> mArtistsList = new ArrayList<>();
        Cursor mCursor = makeArtistCursor(getContext());

        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                final long id = mCursor.getLong(0);

                final String artistName = mCursor.getString(1);

                final int albumCount = mCursor.getInt(2);

                final int songCount = mCursor.getInt(3);

                final Artist artist = new Artist(id, artistName, songCount, albumCount);

                mArtistsList.add(artist);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mArtistsList;
    }

    public static final Cursor makeArtistCursor(final Context context) {
        return context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[] {
                        /* 0 */
                        BaseColumns._ID,
                        /* 1 */
                        ArtistColumns.ARTIST,
                        /* 2 */
                        ArtistColumns.NUMBER_OF_ALBUMS,
                        /* 3 */
                        ArtistColumns.NUMBER_OF_TRACKS
                }, null, null, ArtistColumns.ARTIST + " ASC");
    }
}