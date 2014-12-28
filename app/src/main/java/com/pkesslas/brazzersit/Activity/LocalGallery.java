package com.pkesslas.brazzersit.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.GalleryAdapter;
import com.pkesslas.brazzersit.helper.FileHelper;

import java.io.File;
import java.util.ArrayList;

public class LocalGallery extends ActionBarActivity implements View.OnClickListener {
	private static final int RELOAD = 1;

	private ImageView selectedImage;
	private TextView leftButton, rightButton, shareButton, deleteButton, cameraButton, createButton;
	private Gallery gallery;

	private ArrayList<String> picturePath;
	public int picturePosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallerie);

		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);

		picturePath = FileHelper.getAllFinalPicturePath();
		if (picturePath.size() == 0) {
			finishActivity(RELOAD);
			finish();
			return;
		}

		gallery = (android.widget.Gallery) findViewById(R.id.gallery1);
		selectedImage = (ImageView)findViewById(R.id.picture);
		rightButton = (TextView) findViewById(R.id.btn_right);
		leftButton = (TextView) findViewById(R.id.btn_left);
		shareButton = (TextView) findViewById(R.id.btn_share);
		deleteButton = (TextView) findViewById(R.id.btn_delete);
		cameraButton = (TextView) findViewById(R.id.btn_camera);
		createButton = (TextView) findViewById(R.id.btn_create);

		rightButton.setOnClickListener(this);
		leftButton.setOnClickListener(this);
		shareButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		createButton.setOnClickListener(this);
		cameraButton.setOnClickListener(this);

		this.picturePosition = getIntent().getExtras().getInt("position");

		Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(picturePosition));
		selectedImage.setImageBitmap(bmp);

		gallery.setSpacing(1);
		gallery.setAdapter(new GalleryAdapter(this, picturePath));
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				picturePosition = position;
				Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(position));
				selectedImage.setImageBitmap(bmp);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_left) {
			if (picturePosition - 1 >= 0) {
				picturePosition--;
				Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(picturePosition));
				selectedImage.setImageBitmap(bmp);
			}
		} else if (v.getId() == R.id.btn_right) {
			if (picturePosition + 1 < picturePath.size()) {
				picturePosition++;
				Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(picturePosition));
				selectedImage.setImageBitmap(bmp);
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
			} else {
				picturePosition++;
			}

			getIntent().putExtra("position", picturePosition);
			finish();
			startActivity(getIntent());
		} else if (v.getId() == R.id.btn_camera) {
			finish();
			startActivity(new Intent(this, TakePicture.class));
		} else if (v.getId() == R.id.btn_create) {
			finish();
			startActivity(new Intent(this, CreatePicture.class));
		}
	}
}