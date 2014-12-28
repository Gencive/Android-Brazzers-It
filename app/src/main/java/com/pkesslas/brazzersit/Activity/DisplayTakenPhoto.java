package com.pkesslas.brazzersit.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.brazzersit.Activity.MainActivity;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.helper.BitmapHelper;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class DisplayTakenPhoto extends ActionBarActivity implements View.OnClickListener {
	private String picturePath;
	private File pictureFile;
	private Uri source;
	private Uri outputUri;
	private Bitmap finalBitmap;

	private TextView save, delete, buttonHome, buttonCreate;
	private ImageView pictureView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_taken_photo);

		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);

		picturePath = getIntent().getStringExtra("path");
		getCroppedImage();
		pictureView = (ImageView) findViewById(R.id.taken_photo);
		pictureFile = new File(picturePath);

		save = (TextView) findViewById(R.id.btn_save);
		delete = (TextView) findViewById(R.id.btn_delete);
		buttonHome = (TextView) findViewById(R.id.btn_home);
		buttonCreate = (TextView) findViewById(R.id.btn_create);

		save.setOnClickListener(this);
		delete.setOnClickListener(this);
		buttonHome.setOnClickListener(this);
		buttonCreate.setOnClickListener(this);
	}

	private void getCroppedImage() {
		Bitmap image = BitmapHelper.RotateBitmap(BitmapFactory.decodeFile(picturePath), 90);
		String tmpPath = FileHelper.STORAGE_DIR.getAbsolutePath() + File.separator + FileHelper.createImageName("TMP");
		FileHelper.saveBitmapToFile(image, tmpPath);

		source = Uri.fromFile(new File(tmpPath));
		outputUri = Uri.fromFile(new File(FileHelper.STORAGE_DIR, "tmp_cropped.png"));

		new Crop(source).output(outputUri).withAspect(1, 1).start(this);
	}

	private void displayFinalBitmap(Uri source) {
		try {
			Bitmap picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), source);
			deletePictureWithoutLogo(picturePath);
			finalBitmap = createFinalBitmap(picture, 25);
			pictureView.setImageBitmap(finalBitmap);

			FileHelper.deleteFile(outputUri.getPath());
			FileHelper.deleteFile(source.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deletePictureWithoutLogo(String path) {
		FileHelper.deleteFile(path);
	}

	private Bitmap createFinalBitmap(Bitmap picture, int margin) {
		Bitmap logoInBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brazzers);
		picture = BitmapHelper.getResizedBitmap(picture, 640, 640);
		logoInBitmap = BitmapHelper.getResizedBitmap(logoInBitmap, picture.getHeight() * 10 / 100, picture.getWidth() * 50 / 100);
		int xLogo = (picture.getWidth() - logoInBitmap.getWidth()) - margin;
		int yLogo = (picture.getHeight() - logoInBitmap.getHeight()) - margin;

		return BitmapHelper.overlay(picture, logoInBitmap, xLogo,  yLogo);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_delete) {
			pictureFile.delete();
			Toast.makeText(this, "Picture hasn't been saved", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, TakePicture.class));
		} else if (v.getId() == R.id.btn_save) {
			FileHelper.saveBitmapToFile(finalBitmap, getFinalPngPath());
			Toast.makeText(this, "Picture has been saved", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, MainActivity.class));
		} else if (v.getId() == R.id.btn_home) {
			finish();
			startActivity(new Intent(this, MainActivity.class));
		} else if (v.getId() == R.id.btn_create) {
			finish();
			startActivity(new Intent(this, CreatePicture.class));
		}
		finish();
	}

	private String getFinalPngPath() {
		return new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + FileHelper.createImageName("BRZ");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent result) {
		if (resultCode == RESULT_CANCELED) {
			finish();
			startActivity(new Intent(this, TakePicture.class));
		}
		if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, result);
		}
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			displayFinalBitmap(Crop.getOutput(result));
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
