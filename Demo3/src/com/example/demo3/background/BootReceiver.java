package com.example.demo3.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	
	private static final String TAG = "BootReceiver";
	private static final long ZAMAN_ARALIGI = 60 * 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i(TAG, "Started");
		
		SharedPreferences tercihler = PreferenceManager.getDefaultSharedPreferences(context);
		boolean otomatikBaslat = tercihler.getBoolean("PREF_OTOMATIK_BASLAT", true);
		
		Log.i(TAG, "Otomatik Baslat : " + otomatikBaslat);
		
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent serviceIntent = new Intent(context, KonumGuncelleServis.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 1, serviceIntent, 0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, ZAMAN_ARALIGI, ZAMAN_ARALIGI, pendingIntent);
		
		if(otomatikBaslat) {
			manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, ZAMAN_ARALIGI, ZAMAN_ARALIGI, pendingIntent);
		} else {
			manager.cancel(pendingIntent);
		}
		
		Log.i(TAG, "Ended");

	}

}
