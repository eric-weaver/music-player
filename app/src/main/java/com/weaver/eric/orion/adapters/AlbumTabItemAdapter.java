package com.weaver.eric.orion.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.objects.AlbumTabItem;

public class AlbumTabItemAdapter extends ArrayAdapter<AlbumTabItem>
{
	private Activity context;
	private ArrayList<AlbumTabItem> objects;
	private int resource;

	static class ViewHolder
	{
		public TextView title;
		public TextView description;
		public ImageView image;
	}

	public AlbumTabItemAdapter(Activity context, int resource,
			ArrayList<AlbumTabItem> objects)
	{
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		// reuse views
		if (rowView == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(resource, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) rowView.findViewById(R.id.item_title);
			viewHolder.description = (TextView) rowView.findViewById(R.id.item_description);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.item_image);
			rowView.setTag(viewHolder);
		}
		
		// fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String title = objects.get(position).getTitle();
	    String description = objects.get(position).getFormattedDescription();
	    String image = objects.get(position).getImageUri();
	    if(title != null)
	    {
	    	holder.title.setText(title);
	    }
	    if(description != null)
	    {
	    	holder.description.setText(description);
	    }
	    if(image != null)
	    {
	    	holder.image.setImageBitmap(BitmapFactory.decodeFile(image));
	    }
	    
	    

	    return rowView;
	}
}
