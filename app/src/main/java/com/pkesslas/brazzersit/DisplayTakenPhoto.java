package com.pkesslas.brazzersit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DisplayTakenPhoto extends ActionBarActivity implements View.OnClickListener {
	private TextView save, delete;
	private String picturePath;
	private File pictureFile;
	private ImageView pictureView;
	private Bitmap finalizeBitmap;

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

		Bitmap picture = RotateBitmap(BitmapFactory.decodeFile(picturePath), 90);
		try {
			pictureView.setImageBitmap(overlay(picture, BitmapFactory.decodeResource(getResources(), R.drawable.brazzers)));
		} catch (NullPointerException e) {
			e.printStackTrace();
			pictureView.setImageBitmap(picture);
		}
	}

	private String getImagePath() {
		String fileName;

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		fileName = "BRZ_" + timeStamp + ".png";
		return fileName;
	}

	public static void saveBitmapToFile(Bitmap bitmap, String path) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}

	private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
		bmp2 = getResizedBitmap(bmp2, bmp1.getHeight() * 10 / 100, bmp1.getWidth() * 50 / 100);
		finalizeBitmap = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
		Canvas canvas = new Canvas(finalizeBitmap);

		canvas.drawBitmap(bmp1, new Matrix(), null);
		canvas.drawBitmap(bmp2, (bmp1.getWidth() - bmp2.getWidth()) - 25, (bmp1.getHeight() - bmp2.getHeight()) - 25, null);

		return finalizeBitmap;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_delete) {
			pictureFile.delete();
			Toast.makeText(this, "Picture hasn't been saved", Toast.LENGTH_LONG).show();
		} else if (v.getId() == R.id.btn_save) {
			saveBitmapToFile(finalizeBitmap, getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
			Toast.makeText(this, "Picture has been saved", Toast.LENGTH_LONG).show();
		}
		startActivity(new Intent(this, MainActivity.class));
	}
}
