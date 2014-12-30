package com.pkesslas.brazzersit.fragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkesslas.brazzersit.Activity.DisplayTakenPhoto;
import com.pkesslas.brazzersit.Activity.MainActivity;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.pkesslas.brazzersit.view.Preview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class TakePictureFragment extends Fragment implements View.OnClickListener {
	private RelativeLayout rootView;
	private Camera camera;
	private Preview preview;
	private FrameLayout previewLayout;
	private TextView takePictureButton, flashButton, buttonHome, buttonCreate;
	private Context context;
	private boolean cameraRelease = false;
	private boolean flashEnable = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = (RelativeLayout) inflater.inflate(R.layout.take_picture, container, false);

		camera = Camera.open();

		setCameraParameters();

		context = getActivity();
		previewLayout = (FrameLayout) rootView.findViewById(R.id.preview);
		preview = new Preview(context, camera);
		takePictureButton = (TextView) rootView.findViewById(R.id.btn_take_picture);
		flashButton = (TextView) rootView.findViewById(R.id.btn_flash);

		takePictureButton.setOnClickListener(this);
		flashButton.setOnClickListener(this);

		previewLayout.addView(preview);

		return rootView;
	}

	private void setCameraParameters() {
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> list = parameters.getSupportedPictureSizes();
		setPictureSize(parameters, list);
		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

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
//			camera.stopPreview();
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
		} else if (v.getId() == R.id.btn_flash) {
			enableFlash();
		}
	}

	private void enableFlash() {
		if (flashEnable == false) {
			flashEnable = true;
			flashButton.setBackground(getResources().getDrawable(R.drawable.flash_on));
			camera.stopPreview();
			Camera.Parameters p = camera.getParameters();
			p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			camera.setParameters(p);
			camera.startPreview();
		} else if (flashEnable == true) {
			flashEnable = false;
			flashButton.setBackground(getResources().getDrawable(R.drawable.flash_off));
			camera.stopPreview();
			Camera.Parameters p = camera.getParameters();
			p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(p);
			camera.startPreview();
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
	}
}
