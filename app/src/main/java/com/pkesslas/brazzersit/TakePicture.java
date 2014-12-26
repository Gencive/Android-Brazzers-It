package com.pkesslas.brazzersit;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TakePicture extends ActionBarActivity implements View.OnClickListener, SurfaceHolder.Callback {
	private Camera camera;
	private SurfaceView preview;
	private String currentPhotoPath;
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

		preview = (SurfaceView) findViewById(R.id.preview);
		preview.getHolder().addCallback(this);
		preview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		context = this;
		takePictureButton = (TextView) findViewById(R.id.btn_take_picture);
		takePictureButton.setOnClickListener(this);
		camera = Camera.open();
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

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Camera.Parameters params = camera.getParameters();
		List<Camera.Size> sizes = params.getSupportedPreviewSizes();
		Camera.Size selected = sizes.get(0);
		params.setPreviewSize(selected.width,selected.height);
		camera.setParameters(params);

		camera.setDisplayOrientation(90);
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setDisplayOrientation(90);
			camera.setPreviewDisplay(preview.getHolder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i("PREVIEW", "surfaceDestroyed");
	}


	Camera.PictureCallback photoCallback=new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			new SavePhotoTask().execute(data);
			camera.startPreview();
		}
	};

	class SavePhotoTask extends AsyncTask<byte[], String, String> {
		private boolean result = false;
		File photo;

		@Override
		protected String doInBackground(byte[]... jpeg) {
			currentPhotoPath = getImagePath();
			photo = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), currentPhotoPath);

			if (photo.exists()) {
				photo.delete();
			}

			try {
				FileOutputStream fos = new FileOutputStream(photo.getPath());

				fos.write(jpeg[0]);
				fos.close();
				Log.e("Photo path", photo.getAbsolutePath());

				result = true;
			} catch (java.io.IOException e) {
				Log.e("PictureDemo", "Exception in photoCallback", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String string) {
			if (result == true) {
				cameraRelease = true;
				camera.release();
				Intent intent = new Intent(context, DisplayTakenPhoto.class);
				intent.putExtra("path", photo.getAbsolutePath());
				startActivity(intent);
			}
		}
	}
}
