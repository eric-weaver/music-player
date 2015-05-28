package com.weaver.eric.orion.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter 
{
	public ArrayAdapter<String> CreateArrayAdapter(ArrayList<String> list, Context mContext, final int textColor)
	{
		// This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter
        ArrayAdapter<String>arrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1, list)
        {
        	 @Override
             public View getView(int position, View convertView, ViewGroup parent) 
        	 {
                 View view = super.getView(position, convertView, parent);
                 TextView text = (TextView) view.findViewById(android.R.id.text1);
                 text.setTextColor(textColor);
                 return view;
        	 }
        };
        return arrayAdapter;
	}
}
