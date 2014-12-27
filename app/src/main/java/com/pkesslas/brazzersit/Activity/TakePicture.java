package com.pkesslas.brazzersit.Activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pkesslas.brazzersit.helper.FileHelper;
import com.pkesslas.brazzersit.view.Preview;
import com.pkesslas.brazzersit.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class TakePicture extends ActionBarActivity implements View.OnClickListener {
	private Camera camera;
	private Preview preview;
	private FrameLayout previewLayout;
	private TextView takePictureButton;
	private Context context;
	private boolean cameraRelease = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture);

		camera = Camera.open();

		setCameraParameters();

		previewLayout = (FrameLayout) findViewById(R.id.preview);
		preview = new Preview(this, camera);
		context = this;
		takePictureButton = (TextView) findViewById(R.id.btn_take_picture);
		takePictureButton.setOnClickListener(this);
		previewLayout.addView(preview);
	}

	private void setCameraParameters() {
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> list = parameters.getSupportedPictureSizes();
		setPictureSize(parameters, list);

		camera.setParameters(parameters);
	}

	private void setPictureSize(Camera.Parameters parameters, List<Camera.Size> list) {
		int height = 0, width = 0;
		for (int i = list.size() - 1; i >= 0; i--) {
			if (list.get(i).height > 640 && list.get(i).width > 640) {
				height = list.get(i).height;
				width = list.get(i).width;
				break;
			}
		}
		if (height == 0 && width == 0) {
			height = list.get(list.size() - 1).height;
			width = list.get(list.size() - 1).width;
		}
		parameters.setPictureSize(width, height);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (cameraRelease == false) {
			camera.stopPreview();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		camera.release();
		Log.d("CAMERA", "Destroy");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_take_picture) {
			camera.takePicture(null, null, photoCallback);
		}
	}

	private static File getOutputMediaFile() {
		File mediaFile;
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		mediaFile = new File(mediaStorageDir.getPath() + File.separator + FileHelper.createImageName("IMG"));

		return mediaFile;
	}

	Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();

			if (pictureFile == null) {
				return;
			}
			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();

				goToDisplayPicture(pictureFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			preview.refreshCamera(camera);
		}
	};

	protected void goToDisplayPicture(File photo) {
		cameraRelease = true;
		camera.release();
		Intent intent = new Intent(context, DisplayTakenPhoto.class);
		Log.i("Photo path", photo.getAbsolutePath());
		intent.putExtra("path", photo.getAbsolutePath());
		startActivity(intent);
		finish();
	}
}
