package com.pkesslas.brazzersit.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.MainGalleryAdapter;
import com.pkesslas.brazzersit.helper.FileHelper;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
	private final static int RELOAD = 1;
	private TextView cameraButton, uploadButton, galleryButton;
	private ListView photoList;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);

		context = this;
		cameraButton = (TextView) findViewById(R.id.btn_camera);
		uploadButton = (TextView) findViewById(R.id.btn_upload);
		galleryButton = (TextView) findViewById(R.id.btn_gallery);

		cameraButton.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		galleryButton.setOnClickListener(this);

		buildListView();
	}

	private void buildListView() {
		ArrayList<String> objects = FileHelper.getAllFinalPicturePath();
		for (String path : objects) {
			Log.i("Selected path", path);
		}

		photoList = (ListView) findViewById(R.id.photo_list);
		final MainGalleryAdapter adapter = new MainGalleryAdapter(this, objects);
		photoList.setAdapter(adapter);

		photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context, LocalGallery.class);

				intent.putExtra("position", position);
				startActivityForResult(intent, RELOAD);
			}
		});
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
		} else if (v.getId() == R.id.btn_upload) {
			startActivity(new Intent(this, CreatePicture.class));
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
