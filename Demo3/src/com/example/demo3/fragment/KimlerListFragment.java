package com.example.demo3.fragment;

import com.example.demo3.background.ArkadasSorgulaAsyncTask;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ListView;


public class KimlerListFragment extends ListFragment {

public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		ListView arkadasListView = getListView();
		
		setListShown(true);
		ArkadasSorgulaAsyncTask task=new ArkadasSorgulaAsyncTask(getActivity(), this);
		task.execute();
}
	
}
