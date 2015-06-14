package com.weaver.eric.orion.models;

public class Artist
{
	long id;
	String artistName;
	int albumCount;
	int songCount;
	
	public Artist(long id, String title, int numAlbums, int numSongs)
	{
		this.id = id;
		this.artistName = title;
		this.albumCount = numAlbums;
		this.songCount = numSongs;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle()
	{
		return artistName;
	}

	public void setTitle(String title)
	{
		this.artistName = title;
	}

	public int getNumAlbums()
	{
		return albumCount;
	}

	public void setNumAlbums(int numAlbums)
	{
		this.albumCount = numAlbums;
	}

	public int getNumSongs()
	{
		return songCount;
	}

	public void setNumSongs(int numSongs)
	{
		this.songCount = numSongs;
	}	
	
	public String getFormattedDescription()
	{
		return albumCount + " albums, " + songCount + " songs";
	}
}
