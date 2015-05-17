package com.example.demo3.background;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class KonumGuncelleServis extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	private LocationClient locationClient;
	private LocationRequest locationRequest;
	
	@Override
	public void onCreate() {
		Log.d("KonumGuncelleService", "olusturuldu");
		super.onCreate();
		
		locationClient = new LocationClient(this, this, this);
		
		locationRequest = LocationRequest.create()
			      .setNumUpdates(1)
			      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("KonumGuncelleService", "baslatildi");
		
		if(!locationClient.isConnected())
			locationClient.connect();
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		locationClient.requestLocationUpdates(locationRequest, this);
	}
	
	public void onLocationChanged(Location location) {
		UzaklikHesaplaAsyncTask task = new UzaklikHesaplaAsyncTask(this);
		task.execute(new Location[] {location});
		
		stopSelf();
	}

	@Override
	public void onDisconnected() {}

	@Override
	public void onConnectionFailed(ConnectionResult result) {}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		locationClient.disconnect();
		super.onDestroy();
	}

}
