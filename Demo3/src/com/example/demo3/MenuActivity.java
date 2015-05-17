package com.example.demo3;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;



public class MenuActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		if(isConn()==false) 
		{ 
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Internet Servisler Kapalý. Açmak Ýstiyormusunuz?")
        .setCancelable(false)
        .setPositiveButton("Ýnternet Aç",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                		android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
            	Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();} 
		
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            
        }else{
            showGPSDisabledAlertToUser();
        }
		
		ImageButton harita=(ImageButton) findViewById(R.id.haritaButton);
		harita.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MenuActivity.this,HaritaActivity.class);
				startActivity(intent);
				
			}
		});
		
		ImageButton kimler=(ImageButton) findViewById(R.id.kimlerButton);
		kimler.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MenuActivity.this,KimlerActivity.class);
				startActivity(intent);
				
			}
		});
		ImageButton profilImageButton =(ImageButton)findViewById(R.id.profilButton);
		profilImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent (MenuActivity.this,ProfilActivity.class);
				startActivity(intent);
				
			}
		});
		
		ImageButton ayarlarImageButton=(ImageButton)findViewById(R.id.ayarlarButton);
	
		ayarlarImageButton.setOnClickListener(new  View.OnClickListener() {
		
	
		public void onClick(View v) {
			
			Intent intent=new Intent (MenuActivity.this,AyarlarActivity.class);
			startActivity(intent);
		}
	});
	}
	public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
	private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS Kapalý. Açmak Ýstiyormusunuz?")
        .setCancelable(false)
        .setPositiveButton("Gps Aç",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
