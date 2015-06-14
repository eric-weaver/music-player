package com.weaver.eric.orion.adapters;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weaver.eric.orion.R;
import com.weaver.eric.orion.models.Album;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AlbumTabItemAdapter extends ArrayAdapter<Album>
{
	private static final String TAG = "AlbumItemAdapter";
	private static final String BUNDLE_BM = "BM";
	private static final String BUNDLE_POS = "POS";
	private static final String BUNDLE_ID = "ID";

	private final Activity mContext;
	private final ArrayList<Album> mObjects;
	private final int mResource;
	private final DisplayImageOptions mOptions;

	static class ViewHolder
	{
		public TextView title;
		public TextView description;
		public ImageView image;
	}

	public AlbumTabItemAdapter(Activity context, int resource,
			ArrayList<Album> objects)
	{
		super(context, resource, objects);
		this.mContext = context;
		this.mObjects = objects;
		this.mResource = resource;
		mOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.music_notes)
				.showImageForEmptyUri(R.drawable.music_notes)
				.showImageOnFail(R.drawable.music_notes)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{
		View rowView = convertView;
		// reuse views
		if (rowView == null)
		{
			LayoutInflater inflater = mContext.getLayoutInflater();
			rowView = inflater.inflate(mResource, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) rowView.findViewById(R.id.item_title);
			viewHolder.description = (TextView) rowView.findViewById(R.id.item_description);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.item_image);
			rowView.setTag(viewHolder);
		}

		// fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    final String title = mObjects.get(position).getTitle();
	    final String description = mObjects.get(position).getFormattedDescription();
		//Add a default image
		holder.image.setImageResource(R.drawable.music_notes);
	    if(title != null) {
	    	holder.title.setText(title);
	    }
	    if(description != null) {
			holder.description.setText(description);
		}
		final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
		final Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, mObjects.get(position).getId());
		ImageLoader.getInstance().displayImage(albumArtUri.toString(), holder.image, mOptions);

	    return rowView;
	}
}
