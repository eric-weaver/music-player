package com.weaver.eric.orion.objects;

public class SongTabItem
{
	String title;
	String artist;
	
	public SongTabItem(String title, String artist)
	{
		this.title = title;
		this.artist = artist;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getArtist()
	{
		return artist;
	}

	public void setArtist(String artist)
	{
		this.artist = artist;
	}
}
