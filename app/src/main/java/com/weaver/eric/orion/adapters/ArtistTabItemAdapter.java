package com.weaver.eric.orion.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weaver.eric.orion.R;
import com.weaver.eric.orion.objects.ArtistTabItem;

public class ArtistTabItemAdapter extends ArrayAdapter<ArtistTabItem>
{
	private Activity context;
	private ArrayList<ArtistTabItem> objects;
	private int resource;

	static class ViewHolder
	{
		public TextView title;
		public TextView description;
	}

	public ArtistTabItemAdapter(Activity context, int resource,
			ArrayList<ArtistTabItem> objects)
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
			rowView.setTag(viewHolder);
		}
		
		// fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String title = objects.get(position).getTitle();
	    String description = objects.get(position).getFormattedDescription();
	    if(title != null)
	    {
	    	holder.title.setText(title);
	    }
	    if(description != null)
	    {
	    	holder.description.setText(description);
	    }
	    return rowView;
	}
}
