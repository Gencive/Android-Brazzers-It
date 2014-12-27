package com.pkesslas.brazzersit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.pkesslas.brazzersit.R;

import java.util.ArrayList;

/**
 * Created by Pierre-Elie on 27/12/14.
 */
public class MainGalleryAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;

	public MainGalleryAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.main_gallery_layout, values);

		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.main_gallery_layout, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.thumbnails);

		Bitmap bmp = BitmapFactory.decodeFile(values.get(position));

		imageView.setImageBitmap(bmp);

		return rowView;
	}
}
