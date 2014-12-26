package com.pkesslas.brazzersit;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by sidereo on 26/12/14.
 */

class Preview  {

	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;

	Preview(Context context) {
	//	super(context);

		mSurfaceView = new SurfaceView(context);
	//	addView(mSurfaceView);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = mSurfaceView.getHolder();
	//	mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
}