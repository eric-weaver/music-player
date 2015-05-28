package com.weaver.eric.orion.objects;

public class ArtistTabItem
{
	String title;
	String numAlbums;
	String numSongs;
	
	public ArtistTabItem(String title, String numAlbums, String numSongs)
	{
		this.title = title;
		this.numAlbums = numAlbums;
		this.numSongs = numSongs;
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
