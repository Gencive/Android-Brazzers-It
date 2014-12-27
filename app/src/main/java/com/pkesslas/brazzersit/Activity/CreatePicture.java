package com.pkesslas.brazzersit.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.helper.BitmapHelper;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

public class CreatePicture extends ActionBarActivity implements View.OnClickListener {
	private static int RESULT_LOAD_IMAGE = 1;

	private TextView browseButton, saveButton, deleteButton;

	private Bitmap finalBitmap;
	private Uri source, outputUri;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_picture);

		browseButton = (TextView) findViewById(R.id.btn_browse);
		saveButton = (TextView) findViewById(R.id.btn_save);
		deleteButton = (TextView) findViewById(R.id.btn_delete);

		browseButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_browse) {
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, RESULT_LOAD_IMAGE);
		} else if (v.getId() == R.id.btn_delete) {
			Toast.makeText(this, "Picture hasn't been saved", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, MainActivity.class));
			finish();
		} else if (v.getId() == R.id.btn_save) {
			FileHelper.saveBitmapToFile(finalBitmap, getFinalPngPath());
			Toast.makeText(this, "Picture has been saved", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}

	private String getFinalPngPath() {
		return new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + FileHelper.createImageName("BRZ");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_CANCELED) {
			finish();
			startActivity(getIntent());
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = {
					MediaStore.Images.Media.DATA
			};

			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			source = Uri.fromFile(new File(picturePath));
			outputUri = Uri.fromFile(new File(FileHelper.STORAGE_DIR, "tmp_cropped.png"));

			new Crop(source).output(outputUri).withMaxSize(1280, 1280).start(this);
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			ImageView imageView = (ImageView) findViewById(R.id.image);
			try {
				finalBitmap = createFinalBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result)), 25);
				imageView.setImageBitmap(finalBitmap);

				saveButton.setVisibility(View.VISIBLE);
				deleteButton.setVisibility(View.VISIBLE);
			} catch (IOException e) {
				finish();
				startActivity(getIntent());
			}
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
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
