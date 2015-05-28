package com.weaver.eric.orion.objects;

public class AlbumTabItem
{
	String title;
	String imageUri;
	String numSongs;
	
	public AlbumTabItem(String title, String imageUri, String numSongs)
	{
		this.title = title;
		this.imageUri = imageUri;
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

	public String getImageUri()
	{
		return imageUri;
	}

	public void setImageUri(String imageUri)
	{
		this.imageUri = imageUri;
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
		return numSongs + " songs";
	}
}
