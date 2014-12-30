package com.pkesslas.brazzersit.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkesslas.brazzersit.Activity.LocalGallery;
import com.pkesslas.brazzersit.Activity.TakePicture;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.MainGalleryAdapter;
import com.pkesslas.brazzersit.helper.FileHelper;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {
	private final static int RELOAD = 1;
	private TextView cameraButton, uploadButton, galleryButton;
	private ListView photoList;
	private Context context;
	private RelativeLayout rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);

		context = getActivity();
		buildListView();

		return rootView;
	}

	private void buildListView() {
		ArrayList<String> objects = FileHelper.getAllFinalPicturePath();
		for (String path : objects) {
			Log.i("Selected path", path);
		}

		photoList = (ListView) rootView.findViewById(R.id.photo_list);
		final MainGalleryAdapter adapter = new MainGalleryAdapter(context, objects);
		photoList.setAdapter(adapter);

		photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context, LocalGallery.class);

				intent.putExtra("position", position);
				//startActivityForResult(intent, RELOAD);
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();

				LocalGalleryFragment myFragment = new LocalGalleryFragment();

				Bundle args = new Bundle();
				args.putInt("position", position);
				myFragment.setArguments(args);
				transaction.replace(R.layout.activity_gallerie, myFragment);
				transaction.commit();
			}
		});
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
	}
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("onActivtyResult", "" + requestCode);
		if (requestCode == RELOAD) {
			startActivity(context.getIntent());
		}
	}
*/
}
