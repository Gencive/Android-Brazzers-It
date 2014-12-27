package com.pkesslas.brazzersit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.helper.FileHelper;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
	private TextView cameraButton, uploadButton, galleryButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);

		cameraButton = (TextView) findViewById(R.id.btn_camera);
		uploadButton = (TextView) findViewById(R.id.btn_upload);
		galleryButton = (TextView) findViewById(R.id.btn_gallery);

		cameraButton.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		galleryButton.setOnClickListener(this);

		for (String path : FileHelper.getAllFinalPicturePath()) {
			Log.i("Selected path", path);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
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
		if (v.getId() == R.id.btn_camera) {
			startActivity(new Intent(this, TakePicture.class));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("onActivtyResult", "" + requestCode);
		if (requestCode == RELOAD) {
			finish();
			startActivity(getIntent());
		}
	}
}
