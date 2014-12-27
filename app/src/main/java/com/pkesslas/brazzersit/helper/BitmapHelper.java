package com.pkesslas.brazzersit.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Created by Pierre-Elie on 27/12/14.
 */
public class BitmapHelper {

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	}

	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2, int x, int y) {
		Bitmap finalizeBitmap;

		finalizeBitmap = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
		Canvas canvas = new Canvas(finalizeBitmap);

		canvas.drawBitmap(bmp1, new Matrix(), null);
		canvas.drawBitmap(bmp2, x, y, null);

		return finalizeBitmap;
	}
}
