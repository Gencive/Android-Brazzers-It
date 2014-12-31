package com.pkesslas.brazzersit.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pkesslas.brazzersit.fragment.CreatePictureFragment;
import com.pkesslas.brazzersit.fragment.DisplayTakenPhotoFragment;
import com.pkesslas.brazzersit.fragment.HomeFragment;
import com.pkesslas.brazzersit.fragment.LocalGalleryFragment;
import com.pkesslas.brazzersit.fragment.TakePictureFragment;
import com.pkesslas.brazzersit.interfaces.FirstPageFragmentListener;

/**
 * Created by Pierre-Elie on 30/12/14.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	private static int NUM_PAGES = 3;

	private FragmentManager fragmentManager;
	private Fragment homeFragment;
	private Fragment takePictureFragment;
	FirstPageListener listener = new FirstPageListener();

	private final class FirstPageListener implements FirstPageFragmentListener {
		public void onSwitchToNextHomeFragment() {
			onSwitchToNextHomeFragment(null);
		}

		public void onSwitchToNextHomeFragment(Bundle bundle) {
			fragmentManager.beginTransaction().remove(homeFragment).commit();
			if (homeFragment instanceof HomeFragment){
				homeFragment = new LocalGalleryFragment(listener);
			} else {
				homeFragment = new HomeFragment(listener);
			}
			homeFragment.setArguments(bundle);
			notifyDataSetChanged();
		}

		public void onSwitchToNextTakePictureFragment() {
			onSwitchToNextTakePictureFragment(null);
		}

		public void onSwitchToNextTakePictureFragment(Bundle bundle) {
			fragmentManager.beginTransaction().remove(takePictureFragment).commit();
			if (takePictureFragment instanceof TakePictureFragment){
				takePictureFragment = new DisplayTakenPhotoFragment(listener);
			} else {
				takePictureFragment = new TakePictureFragment(listener);
			}
			takePictureFragment.setArguments(bundle);
			notifyDataSetChanged();
		}
	}

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragmentManager = fm;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				if (homeFragment == null)
				{
					homeFragment = new HomeFragment(listener);
				}
				return homeFragment;
			case 1:
				if (takePictureFragment == null)
				{
					takePictureFragment = new TakePictureFragment(listener);
				}
				return takePictureFragment;
			case 2:
				return new CreatePictureFragment();
			default:
				return homeFragment;
		}
	}

	@Override
	public int getCount() {
		return NUM_PAGES;
	}

	@Override
	public int getItemPosition(Object object) {
		if (object instanceof HomeFragment && homeFragment instanceof LocalGalleryFragment) {
			return POSITION_NONE;
		} else if (object instanceof LocalGalleryFragment && homeFragment instanceof HomeFragment) {
			return POSITION_NONE;
		} else if (object instanceof TakePictureFragment && takePictureFragment instanceof DisplayTakenPhotoFragment) {
				return POSITION_NONE;
		} else if (object instanceof DisplayTakenPhotoFragment && takePictureFragment instanceof TakePictureFragment) {
			return POSITION_NONE;
		}
		return POSITION_UNCHANGED;
	}

	public void setHomeFragment(Fragment fragment) {
		this.homeFragment = fragment;
	}
}
