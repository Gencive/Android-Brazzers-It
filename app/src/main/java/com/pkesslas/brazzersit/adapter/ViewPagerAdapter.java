package com.pkesslas.brazzersit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pkesslas.brazzersit.fragment.CreatePictureFragment;
import com.pkesslas.brazzersit.fragment.HomeFragment;
import com.pkesslas.brazzersit.fragment.TakePictureFragment;

/**
 * Created by Pierre-Elie on 30/12/14.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	private static int NUM_PAGES = 3;

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return new HomeFragment();
			case 1:
				return new TakePictureFragment();
			case 2:
				return new CreatePictureFragment();
			default:
				return new HomeFragment();
		}
	}

	@Override
	public int getCount() {
		return NUM_PAGES;
	}
}
