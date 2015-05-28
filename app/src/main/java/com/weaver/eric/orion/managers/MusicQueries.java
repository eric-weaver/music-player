package com.weaver.eric.orion.managers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicQueries {

	public Cursor getArtistCursor(Context context, Cursor cursor) {
		String where = null;
		ContentResolver cr = context.getContentResolver();
		final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		final String artist = MediaStore.Audio.Albums.ARTIST;
		final String[] columns = { artist };
		final String sort = MediaStore.Audio.AudioColumns.ARTIST + " ASC";

		try {
			cursor = cr.query(uri, columns, where, null, sort);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query Artists");
			System.out.println(e);
			return null;
		}
	}

	public Cursor getAllAlbumCursor(Context context, Cursor cursor) {
		String where = null;
		ContentResolver cr = context.getContentResolver();
		final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		final String album = MediaStore.Audio.Albums.ALBUM;
		final String[] columns = { album };
		final String sort = MediaStore.Audio.AudioColumns.ALBUM + " ASC";

		try {
			cursor = cr.query(uri, columns, where, null, sort);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query All Albums");
			System.out.println(e);
			return null;
		}
	}
	
	public Cursor getAllAlbumInfoCursor(Context context, Cursor cursor) {
		String where = null;
		ContentResolver cr = context.getContentResolver();
		final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		final String album = MediaStore.Audio.Albums.ALBUM;
		final String artist = MediaStore.Audio.Albums.ARTIST;
		final String numSongs = MediaStore.Audio.Albums.NUMBER_OF_SONGS;
		final String albumArt = MediaStore.Audio.Albums.ALBUM_ART;
		final String[] columns = { album, artist, numSongs, albumArt };
		final String sort = MediaStore.Audio.AudioColumns.ALBUM + " ASC";

		try {
			cursor = cr.query(uri, columns, where, null, sort);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query All Albums");
			System.out.println(e);
			return null;
		}
	}
	
//	public Cursor getArtistAlbumsInfoCursor(Context context, Cursor cursor, String artist)
//	{
//		
//	}
	
	public Cursor getAllSongsInfoCursor(Context context, Cursor cursor) {

		ContentResolver cr = context.getContentResolver();

		final String songNames = MediaStore.Audio.Media.TITLE;
		final String artistName = MediaStore.Audio.Media.ARTIST;
		final String duration = MediaStore.Audio.Media.DURATION;
		final String track = MediaStore.Audio.Media.TRACK;
		final String sort = MediaStore.Audio.AudioColumns.TITLE + " ASC";
		final String where = null;
		final String[] selection = null;
		final String[] columns = { artistName, songNames, duration, track };
		final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		try {
			cursor = cr.query(uri, columns, where, selection, sort);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query All songs");
			System.out.println(e);
			return null;
		}
	}

	public Cursor getArtistAlbumsCursor(Context context, Cursor cursor,
			String artist) {

		ContentResolver cr = context.getContentResolver();

		final String album_name = MediaStore.Audio.Albums.ALBUM;
		final String sort = MediaStore.Audio.AudioColumns.ALBUM + " ASC";
		final String where = MediaStore.Audio.Albums.ARTIST + "=?";
		final String[] selection = { artist };
		final String[] columns = { album_name };
		final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		try {
			cursor = cr.query(uri, columns, where, selection, sort);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query " + artist + "'s albums");
			System.out.println(e);
			return null;
		}
	}

	public Cursor getAllSongsCursor(Context context, Cursor cursor) {

		ContentResolver cr = context.getContentResolver();

		final String songNames = MediaStore.Audio.Media.TITLE;
		final String sort = MediaStore.Audio.AudioColumns.TITLE + " ASC";
		final String where = null;
		final String[] selection = null;
		final String[] columns = { songNames };
		final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		try {
			cursor = cr.query(uri, columns, where, selection, sort);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query All songs");
			System.out.println(e);
			return null;
		}
	}

	public Cursor getAlbumSongsCursor(Context context, Cursor cursor,
			String album) {

		ContentResolver cr = context.getContentResolver();

		final String songNames = MediaStore.Audio.Media.TITLE;
		final String sort = MediaStore.Audio.AudioColumns.TITLE + " ASC";
		final String where = MediaStore.Audio.Albums.ALBUM + "=?";
		final String[] selection = { album };
		final String[] columns = { songNames };
		final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		try {
			cursor = cr.query(uri, columns, where, selection, sort);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query " + album + "'s songs");
			System.out.println(e);
			return null;
		}
	}
	
	public Cursor playSongCursor(Context context, Cursor cursor, String song)
	{
		ContentResolver cr = context.getContentResolver();

		final String songNames = MediaStore.Audio.Media.TITLE;
		final String songData = MediaStore.Audio.Media.DATA;
		final String where = MediaStore.Audio.Media.TITLE + "=?";
		final String[] selection = {song};
		final String[] columns = {songNames, songData};
		final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		
		try {
			cursor = cr.query(uri, columns, where, selection, null);
			return cursor;
		} catch (Exception e) {
			System.out.println("Failed to query " + song + "'s data");
			System.out.println(e);
			return null;
		}
	}
}