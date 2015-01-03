package com.pkesslas.brazzersit.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkesslas.brazzersit.Activity.OnlineGalleryActivity;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.MainGalleryAdapter;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.pkesslas.brazzersit.interfaces.FirstPageFragmentListener;

import java.util.ArrayList;


public class OnlineGalleryFragment extends Fragment implements View.OnClickListener {
	private RelativeLayout rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		startActivity(new Intent(getActivity(), OnlineGalleryActivity.class));
		return super.onCreateView(inflater, container, savedInstanceState);
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
