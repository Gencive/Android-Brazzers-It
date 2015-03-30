package com.pkesslas.brazzersit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Pierre-Elie on 01/01/15.
 */
public class MyApplication extends Application {

	@Override
	public void onCreate()
	{
		super.onCreate();

		initParse();
	}

	private void initParse() {
		//Parse.enableLocalDatastore(this);
		//Parse.initialize(this, "TJOWTuFpoQToMQ6EyPXhYEA7GP0ctSIL8yBZVp1u", "IXjIMuOE8qCpaxmgs0CnLyrjks57JT3zictcfS3S");
	}
}
