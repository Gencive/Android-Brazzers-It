package com.pkesslas.brazzersit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.MainGalleryAdapter;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.pkesslas.brazzersit.interfaces.FirstPageFragmentListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {
	static private FirstPageFragmentListener listener;

	private final static int RELOAD = 1;
	private TextView cameraButton, uploadButton, galleryButton;
	private ListView photoList;
	private Context context;
	private RelativeLayout rootView;

	public HomeFragment() {
	}

	public HomeFragment(FirstPageFragmentListener listener) {
		this.listener = listener;
	}

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
				Bundle args = new Bundle();
				args.putInt("position", position);
				listener.onSwitchToNextHomeFragment(args);
			}
		});
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
