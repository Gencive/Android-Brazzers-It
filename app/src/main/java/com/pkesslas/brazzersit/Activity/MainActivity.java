package com.pkesslas.brazzersit.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.ViewPagerAdapter;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
	private final static int RELOAD = 1;

	public final static int HOME_POSITION = 0;
	public final static int CAMERA_POSITION = 1;
	public final static int CREATE_POSITION = 2;

	private TextView cameraButton, createButton, galleryButton, homeButton;
	private ListView photoList;
	private Context context;

	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);

		context = this;
		homeButton = (TextView) findViewById(R.id.btn_home);
		cameraButton = (TextView) findViewById(R.id.btn_camera);
		createButton = (TextView) findViewById(R.id.btn_create);
//		galleryButton = (TextView) findViewById(R.id.btn_gallery);

		cameraButton.setOnClickListener(this);
		createButton.setOnClickListener(this);
		homeButton.setOnClickListener(this);
//		galleryButton.setOnClickListener(this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_home) {
			mPager.setCurrentItem(HOME_POSITION);
		} else if (v.getId() == R.id.btn_camera) {
			mPager.setCurrentItem(CAMERA_POSITION);
		} else if (v.getId() == R.id.btn_create) {
			mPager.setCurrentItem(CREATE_POSITION);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("onActivtyResult", "" + requestCode);
		if (requestCode == RELOAD) {
			finish();
			startActivity(getIntent());
		}
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the system to handle the
			// Back button. This calls finish() on this activity and pops the back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}
	public void onPageScrollStateChanged(int arg0) {
		int pos = getCurrentSelectedFragmentPosition();
		System.out.println("position = " + pos);
		if (pos == 0) {
			homeButton.setBackground(getResources().getDrawable(R.drawable.button_home_activate));
			cameraButton.setBackground(getResources().getDrawable(R.drawable.button_camera));
			createButton.setBackground(getResources().getDrawable(R.drawable.button_create));
		} else if (pos == 1) {
			cameraButton.setBackground(getResources().getDrawable(R.drawable.button_camera_activate));
			homeButton.setBackground(getResources().getDrawable(R.drawable.button_home));
			createButton.setBackground(getResources().getDrawable(R.drawable.button_create));
		} else if (pos == 2) {
			createButton.setBackground(getResources().getDrawable(R.drawable.button_create_activate));
			cameraButton.setBackground(getResources().getDrawable(R.drawable.button_camera));
			homeButton.setBackground(getResources().getDrawable(R.drawable.button_home));
		}
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void onPageSelected(int arg0) {
	}

	public int getCurrentSelectedFragmentPosition() {
		return mPager.getCurrentItem();
	}

	public void setPagePosition(int position) {
		mPager.setCurrentItem(position);
	}

	public void reloadFragment(int position) {

	}
}
