package com.weaver.eric.orion.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weaver.eric.orion.objects.Song;

import java.util.ArrayList;

public class SimpleSongAdapter extends ArrayAdapter<Song> {
    private Activity context;
    private ArrayList<Song> objects;

    static class ViewHolder {
        public TextView title;
    }

    public SimpleSongAdapter(Activity context,
                             ArrayList<Song> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(android.R.id.text1);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String title = objects.get(position).getSongName();
        if (title != null) {
            holder.title.setText(title);
        }

        return rowView;
    }
}
