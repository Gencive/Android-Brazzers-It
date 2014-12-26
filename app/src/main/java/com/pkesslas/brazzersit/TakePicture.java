package com.pkesslas.brazzersit;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TakePicture extends ActionBarActivity implements View.OnClickListener {
	private Camera camera;
	private Preview preview;
	private FrameLayout previewLayout;
	private TextView takePictureButton;
	private Context context;
	private boolean cameraRelease = false;

	private String getImagePath() {
		String fileName;

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		fileName = "JPEG_" + timeStamp + ".jpg";
		return fileName;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture);

		camera = Camera.open();

		Camera.Parameters parameters = camera.getParameters();
		Log.e("size", parameters.getPictureSize().height + " " + parameters.getPictureSize().width);
		List<Camera.Size> list = parameters.getSupportedPictureSizes();
		for (Camera.Size l : list) {
			Log.e("elnlsgsgs", l.height + " " + l.width);
			if (l.height == 720) {
				parameters.setPictureSize(l.width, l.height);
			//	break ;
			}
		}

		camera.setParameters(parameters);

		previewLayout = (FrameLayout) findViewById(R.id.preview);
		preview = new Preview(this, camera);
		context = this;
		takePictureButton = (TextView) findViewById(R.id.btn_take_picture);
		takePictureButton.setOnClickListener(this);
		previewLayout.addView(preview);
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
		Log.d("CAMERA","Destroy");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_take_picture) {
			camera.takePicture(null, null, photoCallback);
		}
	}

	private static File getOutputMediaFile() {
		File mediaStorageDir = new File("/sdcard/", "test/");

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + "test.png");

		return mediaFile;
	}

	Camera.PictureCallback photoCallback=new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//make a new picture file
			File pictureFile = getOutputMediaFile();

			if (pictureFile == null) {
				return;
			}
			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();

				displayPicture(pictureFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			preview.refreshCamera(camera);
		}
	};

	protected void displayPicture(File photo) {
		cameraRelease = true;
		camera.release();
		Intent intent = new Intent(context, DisplayTakenPhoto.class);
		Log.i("Photo path", photo.getAbsolutePath());
		intent.putExtra("path", photo.getAbsolutePath());
		startActivity(intent);
	}
}
