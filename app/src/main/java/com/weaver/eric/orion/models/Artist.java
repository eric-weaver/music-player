package com.weaver.eric.orion.models;

public class Artist
{
	long id;
	String title;
	String numAlbums;
	String numSongs;
	
	public Artist(long id, String title, String numAlbums, String numSongs)
	{
		this.id = id;
		this.title = title;
		this.numAlbums = numAlbums;
		this.numSongs = numSongs;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getNumAlbums()
	{
		return numAlbums;
	}

	public void setNumAlbums(String numAlbums)
	{
		this.numAlbums = numAlbums;
	}

	public String getNumSongs()
	{
		return numSongs;
	}

	public void setNumSongs(String numSongs)
	{
		this.numSongs = numSongs;
	}	
	
	public String getFormattedDescription()
	{
		return numAlbums + " albums, " + numSongs + " songs";
	}
}
