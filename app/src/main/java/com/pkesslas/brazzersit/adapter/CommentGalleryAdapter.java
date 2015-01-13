package com.pkesslas.brazzersit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pkesslas.brazzersit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pierre-Elie on 27/12/14.
 */
public class CommentGalleryAdapter extends ArrayAdapter<ParseObject> {
	private final Context context;
	private final List<ParseObject> values;

	public CommentGalleryAdapter(Context context, List<ParseObject> query) {
		super(context, R.layout.comment_layout, query);

		this.context = context;
		this.values = query;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.comment_layout, parent, false);

		TextView author = (TextView) rowView.findViewById(R.id.author);
		TextView comment = (TextView) rowView.findViewById(R.id.comment);

		author.setText(((ParseUser) values.get(position).get("author")).getUsername());
		comment.setText(values.get(position).get("text").toString());
		return rowView;
	}
}
