package com.pkesslas.brazzersit.interfaces;

import android.os.Bundle;

public interface FirstPageFragmentListener
{
	void onSwitchToNextHomeFragment();

	void onSwitchToNextHomeFragment(Bundle bundle);

	void onSwitchToNextTakePictureFragment(Bundle bundle);

	void onSwitchToNextTakePictureFragment();
}