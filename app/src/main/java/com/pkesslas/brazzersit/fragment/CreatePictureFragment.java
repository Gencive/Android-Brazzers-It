package com.pkesslas.brazzersit.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.brazzersit.Activity.MainActivity;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.helper.BitmapHelper;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CreatePictureFragment extends Fragment implements View.OnClickListener {
	private static int RESULT_LOAD_IMAGE = 1;

	private RelativeLayout rootView;
	private TextView browseButton, saveButton, deleteButton;

	private Bitmap finalBitmap;
	private Uri source, outputUri;
	private Context context;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_create_picture, container, false);


		context = getActivity();
		browseButton = (TextView) rootView.findViewById(R.id.btn_browse);
		saveButton = (TextView) rootView.findViewById(R.id.btn_save);
		deleteButton = (TextView) rootView.findViewById(R.id.btn_delete);

		browseButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_browse) {
			Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, RESULT_LOAD_IMAGE);
		} else if (v.getId() == R.id.btn_delete) {
			Toast.makeText(context, "Picture hasn't been saved", Toast.LENGTH_LONG).show();
			((MainActivity)getActivity()).setPagePosition(MainActivity.HOME_POSITION);
		} else if (v.getId() == R.id.btn_save) {
			FileHelper.saveBitmapToFile(finalBitmap, getFinalPngPath());
			Toast.makeText(context, "Picture has been saved", Toast.LENGTH_LONG).show();
			((MainActivity)getActivity()).setPagePosition(MainActivity.HOME_POSITION);
		}
	}

	private String getFinalPngPath() {
		return new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + FileHelper.createImageName("BRZ");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("CreatePictureFragment", "onActivityResult");

		if (resultCode == getActivity().RESULT_CANCELED) {
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
			source = data.getData();
			outputUri = Uri.fromFile(new File(FileHelper.STORAGE_DIR, "tmp_cropped.png"));

			new Crop(source).output(outputUri).withAspect(1, 1).start(context, this);
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}
	}

	private void handleCrop(int resultCode, Intent result) {
		Log.i("CreatePictureFragment", "handleCrop");
		if (resultCode == getActivity().RESULT_OK) {
			ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
			try {
				finalBitmap = createFinalBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(), Crop.getOutput(result)), 25);
				imageView.setImageBitmap(finalBitmap);

				saveButton.setVisibility(View.VISIBLE);
				deleteButton.setVisibility(View.VISIBLE);
				File file = new File(FileHelper.STORAGE_DIR, "tmp_cropped.png");
				FileHelper.deleteFile(file.getAbsolutePath());
			} catch (IOException e) {
				startActivity(getActivity().getIntent());
			}
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private Bitmap createFinalBitmap(Bitmap picture, int margin) {
		Bitmap logoInBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brazzers);
		picture = BitmapHelper.getResizedBitmap(picture, 640, 640);
		logoInBitmap = BitmapHelper.getResizedBitmap(logoInBitmap, picture.getHeight() * 10 / 100, picture.getWidth() * 50 / 100);
		int xLogo = (picture.getWidth() - logoInBitmap.getWidth()) - margin;
		int yLogo = (picture.getHeight() - logoInBitmap.getHeight()) - margin;

		return BitmapHelper.overlay(picture, logoInBitmap, xLogo,  yLogo);
	}
}
