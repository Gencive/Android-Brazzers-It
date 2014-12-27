package com.pkesslas.brazzersit.helper;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pierre-Elie on 27/12/14.
 */
public class FileHelper {

	public static String createImageName(String anchor) {
		String fileName;

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		fileName = anchor + "_" + timeStamp + ".jpg";
		return fileName;
	}

	public static void saveBitmapToFile(Bitmap bitmap, String path) {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(path);
			Log.i("Final path", path);
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

	public static ArrayList<String> getAllFinalPicturePath() {
		ArrayList<String> finalPictures = new ArrayList<String>();
		File picturesDirectory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);

		for (File picture :  picturesDirectory.listFiles()) {
			Log.e("Picture Path", picture.getAbsolutePath());
			if (picture.getName().startsWith("BRZ")) {
				finalPictures.add(picture.getAbsolutePath());
			}
		}
		return finalPictures;
	}

	public static void deleteFile(String path) {
		File fileToDelete = new File(path);

		if (fileToDelete.exists()) {
			fileToDelete.delete();
		}
	}
}
