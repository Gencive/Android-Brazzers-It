package com.pkesslas.brazzersit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.fragment.CreatePictureFragment;
import com.pkesslas.brazzersit.fragment.DisplayPictureOnlineGalleryFragment;
import com.pkesslas.brazzersit.fragment.DisplayTakenPhotoFragment;
import com.pkesslas.brazzersit.fragment.HomeFragment;
import com.pkesslas.brazzersit.fragment.LocalGalleryFragment;
import com.pkesslas.brazzersit.fragment.TakePictureFragment;
import com.pkesslas.brazzersit.interfaces.FirstPageFragmentListener;

import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Pierre-Elie on 30/12/14.
 */
public class OnlineGalleryAdapter extends FragmentStatePagerAdapter {
	private int NUM_PAGES;
	private FragmentManager fragmentManager;
	private ParseQuery<ParseObject> query;
	private Context context;


	public OnlineGalleryAdapter(Context context, FragmentManager fm, ParseQuery<ParseObject> query) {
		super(fm);

		try {
			NUM_PAGES = query.count();
		} catch (com.parse.ParseException e) {
			NUM_PAGES = 0;
		}
		fragmentManager = fm;
		this.query = query;

	}

	@Override
	public Fragment getItem(int position) {
		try {
			return new DisplayPictureOnlineGalleryFragment(this.query.find().get(position));
		} catch (com.parse.ParseException e) {
			return new DisplayPictureOnlineGalleryFragment(null);
		}
	}

	@Override
	public int getCount() {
		return NUM_PAGES;
	}
}
