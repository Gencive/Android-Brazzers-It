package com.pkesslas.brazzersit.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.GalleryAdapter;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.pkesslas.brazzersit.interfaces.FirstPageFragmentListener;

import java.io.File;
import java.util.ArrayList;

public class LocalGalleryFragment extends Fragment implements View.OnClickListener {
	private static final int RELOAD = 1;

	private FirstPageFragmentListener listener;
	private ImageView selectedImage;
	private TextView leftButton, rightButton, shareButton, deleteButton;
	private Gallery gallery;
	private GalleryAdapter galleryAdapter;
	private RelativeLayout rootView;

	private ArrayList<String> picturePath;
	public int picturePosition = -1;

	public LocalGalleryFragment() {}

	public LocalGalleryFragment(FirstPageFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("postion", picturePosition);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("LocalGalleryFragment", "onCreateView");
		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_gallerie, container, false);

		picturePath = FileHelper.getAllFinalPicturePath();
		if (picturePath.size() == 0) {
			Log.d("LocalGalleryFragment", "picturePath.size() == 0");
			// TODO
			return rootView;
		}

		Log.d("LocalGalleryFragment", "buildGallery");
		gallery = (Gallery) rootView.findViewById(R.id.gallery1);
		selectedImage = (ImageView) rootView.findViewById(R.id.picture);
		rightButton = (TextView) rootView.findViewById(R.id.btn_right);
		leftButton = (TextView) rootView.findViewById(R.id.btn_left);
		shareButton = (TextView) rootView.findViewById(R.id.btn_share);
		deleteButton = (TextView) rootView.findViewById(R.id.btn_delete);

		rightButton.setOnClickListener(this);
		leftButton.setOnClickListener(this);
		shareButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);

		//this.picturePosition = getActivity().getIntent().getExtras().getInt("position");

		if (picturePosition == -1) {
			this.picturePosition = getArguments().getInt("position");
		}

		Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(picturePosition));
		selectedImage.setImageBitmap(bmp);

		buildGallery();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null && picturePosition == -1) {
			this.picturePosition = savedInstanceState.getInt("position");
		}
	}

	private void buildGallery() {
		picturePath = FileHelper.getAllFinalPicturePath();
		gallery.setSpacing(1);
		galleryAdapter = new GalleryAdapter(getActivity(), picturePath);
		gallery.setAdapter(galleryAdapter);
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				picturePosition = position;
				Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(position));
				selectedImage.setImageBitmap(bmp);
			}
		});
		gallery.setSelection(picturePosition);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_left) {
			if (picturePosition - 1 >= 0) {
				picturePosition--;
				Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(picturePosition));
				selectedImage.setImageBitmap(bmp);
				galleryAdapter.getItem(picturePosition);
				gallery.setSelection(picturePosition);
			}
		} else if (v.getId() == R.id.btn_right) {
			if (picturePosition + 1 < picturePath.size()) {
				picturePosition++;
				Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(picturePosition));
				selectedImage.setImageBitmap(bmp);
				galleryAdapter.getItemId(picturePosition);
				gallery.setSelection(picturePosition);
			}
		} else if (v.getId() == R.id.btn_share) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			shareIntent.setType("image/*");

			Uri uri = Uri.fromFile(new File(picturePath.get(picturePosition)));
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(shareIntent, "Share via"));
		} else if (v.getId() == R.id.btn_delete) {
			FileHelper.deleteFile(picturePath.get(picturePosition));

			if (picturePosition >= picturePath.size() - 1) {
				picturePosition--;
			}
			buildGallery();
			Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(picturePosition));
			selectedImage.setImageBitmap(bmp);
		}
	}

	public void backPressed() {
		listener.onSwitchToNextHomeFragment();
	}
}