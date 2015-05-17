package com.example.demo3.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.demo3.R;

public class TonlarPreferencesFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.tonlar_preferences);
	}

}
