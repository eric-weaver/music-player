package com.weaver.eric.orion.models;

public class Album
{
	long id;
	String title;
	String imageUri;
	String numSongs;
	
	public Album(long id, String title, String imageUri, String numSongs)
	{
		this.id = id;
		this.title = title;
		this.imageUri = imageUri;
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
