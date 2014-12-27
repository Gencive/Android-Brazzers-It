package com.pkesslas.brazzersit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.pkesslas.brazzersit.R;

import java.util.ArrayList;

/**
 * Created by Pierre-Elie on 27/12/14.
 */
public class GalleryAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<String> picturePath;

	public GalleryAdapter(Context context, ArrayList<String> objects) {
		mContext = context;
		picturePath = objects;
	}

	public int getCount() {
		return picturePath.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}


	// Override this method according to your need
	public View getView(int index, View view, ViewGroup viewGroup)
	{
		// TODO Auto-generated method stub
		ImageView i = new ImageView(mContext);

		Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(index));

		i.setImageBitmap(bmp);
		i.setLayoutParams(new Gallery.LayoutParams(200, 200));

		i.setScaleType(ImageView.ScaleType.FIT_XY);

		return i;
	}
}
