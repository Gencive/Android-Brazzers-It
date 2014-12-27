package com.pkesslas.brazzersit.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.brazzersit.Activity.MainActivity;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.helper.BitmapHelper;
import com.pkesslas.brazzersit.helper.FileHelper;

import java.io.File;


public class DisplayTakenPhoto extends ActionBarActivity implements View.OnClickListener {
	private String picturePath;
	private File pictureFile;
	private Bitmap finalBitmap;

	private TextView save, delete;
	private ImageView pictureView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_taken_photo);

		picturePath = getIntent().getStringExtra("path");
		pictureView = (ImageView) findViewById(R.id.taken_photo);

		pictureFile = new File(picturePath);

		save = (TextView) findViewById(R.id.btn_save);
		delete = (TextView) findViewById(R.id.btn_delete);

		save.setOnClickListener(this);
		delete.setOnClickListener(this);

		displayFinalBitmap();
	}

	private void displayFinalBitmap() {
		Bitmap picture = BitmapHelper.RotateBitmap(BitmapFactory.decodeFile(picturePath), 90);
		try {
			finalBitmap = createFinalBitmap(picture, 25);
			pictureView.setImageBitmap(finalBitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
			pictureView.setImageBitmap(picture);
		}
	}

	private Bitmap createFinalBitmap(Bitmap picture, int margin) {
		Bitmap logoInBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brazzers);
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
		} else if (v.getId() == R.id.btn_save) {
			FileHelper.saveBitmapToFile(finalBitmap, getFinalPngPath());
			Toast.makeText(this, "Picture has been saved", Toast.LENGTH_LONG).show();
		}
		startActivity(new Intent(this, MainActivity.class));
	}

	private String getFinalPngPath() {
		return new File(Environment.getExternalStorageDirectory(),
				Environment.DIRECTORY_PICTURES).getAbsolutePath() + FileHelper.createImageName("BRZ");
				Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + FileHelper.createImageName("BRZ");
	}
}
