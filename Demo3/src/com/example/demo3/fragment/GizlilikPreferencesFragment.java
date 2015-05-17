package com.example.demo3.fragment;

import com.example.demo3.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class GizlilikPreferencesFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.gizlilik_preferences);
	}
	
}
