package com.pkesslas.brazzersit.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.OnlineGalleryAdapter;
import com.pkesslas.brazzersit.adapter.ViewPagerAdapter;

public class OnlineGalleryActivity extends ActionBarActivity implements View.OnClickListener {
	private ViewPager mPager;
	private OnlineGalleryAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_gallery);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
		query.orderByDescending("point");

		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new OnlineGalleryAdapter(this, getSupportFragmentManager(), query);
		mPager.setOffscreenPageLimit(1);
		mPager.setAdapter(mPagerAdapter);
	}

	@Override
	public void onClick(View v) {
	}

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_online_gallery, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	*/
}
