package com.pkesslas.brazzersit.helper;

import android.graphics.Bitmap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
}
