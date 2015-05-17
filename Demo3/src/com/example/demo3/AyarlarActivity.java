package com.example.demo3;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;
import android.view.MenuItem;


public class AyarlarActivity  extends PreferenceActivity{

	public void onBuildHeaders (List<Header>target){
		loadHeadersFromResource(R.xml.preferences, target);
		
		
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	}
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()){
		case android.R.id.home:
			Intent intent=new Intent (this,MenuActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	
		
		
		
	}
	
}
