package com.weaver.eric.orion.models;

public class Album
{
	long albumId;
	String albumName;
	String artistName;
	int songCount;
	String year;
	String imageUri;
	
	public Album(long id, String albumName, String artistName, int numSongs, String year, String imageUri)
	{
		this.albumId = id;
		this.albumName = albumName;
		this.artistName = artistName;
		this.songCount = numSongs;
		this.year = year;
		this.imageUri = imageUri;
	}

	public long getId() {
		return albumId;
	}

	public void setId(long id) {
		this.albumId = id;
	}

	public String getTitle()
	{
		return albumName;
	}

	public void setTitle(String title)
	{
		this.albumName = title;
	}

	public String getImageUri()
	{
		return imageUri;
	}

	public void setImageUri(String imageUri)
	{
		this.imageUri = imageUri;
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
		return songCount + " songs";
	}
}
