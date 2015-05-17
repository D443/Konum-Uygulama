package com.example.demo3;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.example.demo3.background.KonumSorgulaAsyncTask;
import com.example.demo3.R;

public class HaritaActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	public static final String ARKADAS_INTENT_EXTRA = "arkadas";
	
	private LocationClient locationClient;
	private LocationRequest locationRequest;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.harita);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        locationClient = new LocationClient(this, this, this);
		
		locationRequest = LocationRequest.create()
			      .setInterval(5000)
			      .setFastestInterval(500)
			      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		haritaGuncelle(location);
	}
	
	private void haritaGuncelle(Location location) {
		KonumSorgulaAsyncTask task = new KonumSorgulaAsyncTask(this, location);
		task.execute();
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		locationClient.requestLocationUpdates(locationRequest, this);
	}

	@Override
	public void onDisconnected() {}

	@Override
	public void onConnectionFailed(ConnectionResult result) {}

	@Override
	protected void onResume() {
		super.onResume();
		locationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		
		if(locationClient.isConnected())
			locationClient.removeLocationUpdates(this);
		
		locationClient.disconnect();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_harita, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MenuActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.haritaActionYenileMenuItem:
	        	haritaYenile();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		
	}
	
	private void haritaYenile() {
		
		Location location = locationClient.getLastLocation();
		
		if(location != null) {
			KonumSorgulaAsyncTask task = new KonumSorgulaAsyncTask(this, location);
			task.execute();
		}
		
	}
	
}
