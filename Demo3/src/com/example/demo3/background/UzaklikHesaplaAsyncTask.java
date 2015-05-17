package com.example.demo3.background;

import java.util.Iterator;
import java.util.List;

import com.example.demo3.HaritaActivity;
import com.example.demo3.R;
import com.example.demo3.db.DatabaseManager;
import com.example.demo3.db.Profil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class UzaklikHesaplaAsyncTask extends AsyncTask<Location, Void, Konum> {

	private Context context;
	private SharedPreferences tercihler;
	private Notification.Builder notificationBuilder;
	
	public UzaklikHesaplaAsyncTask(Context context) {
		super();
		this.context = context;
		tercihler = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	@Override
	protected Konum doInBackground(Location... params) {
		return getYakinArkadasKonum(params[0]);
	}
	
	private Konum getYakinArkadasKonum(Location location) {
		
		boolean konumGonder = tercihler.getBoolean("PREF_KONUM_GONDER", true);
		boolean yakinlikUyarisi = tercihler.getBoolean("PREF_ARKADAS_UYARI", true);
		String yakinlikOlcusu = tercihler.getString("PREF_YAKINLIK_OLCUSU", "1");
		
		String kullaniciAdi = getKullaniciAdi();
		if(TextUtils.isEmpty(kullaniciAdi))
			return null;
		
		if(konumGonder) {
			Konum konum = new Konum(kullaniciAdi, location.getLatitude(), location.getLongitude());
			NetworkManager.konumKaydet(konum);
		}
		
		if(yakinlikUyarisi) {
			return getYakinArkadasKonumFromArkadasList(kullaniciAdi, location, yakinlikOlcusu);
		}
		
		return null;
		
	}
	
	private Konum getYakinArkadasKonumFromArkadasList(String kullaniciAdi, Location location, String yakinlikOlcusu) {
		
		List<Konum> arkadasKonumListesi = NetworkManager.konumSorgula(kullaniciAdi);
		if(arkadasKonumListesi == null || arkadasKonumListesi.size() == 0)
			return null;
		
		Iterator<Konum> iterator = arkadasKonumListesi.iterator();
		while (iterator.hasNext()) {
			Konum konum = (Konum) iterator.next();
			
			Location arkadasLocation = new Location("");
			arkadasLocation.setLatitude(konum.getEnlem());
			arkadasLocation.setLongitude(konum.getBoylam());
			
			float uzaklik = location.distanceTo(arkadasLocation);
			float yakinlikOlcusuFloat = Float.valueOf(yakinlikOlcusu) * 1000;
			
			if(uzaklik <= yakinlikOlcusuFloat)
				return konum;
		}
		
		return null;
		
	}
	
	@Override
	protected void onPostExecute(Konum konum) {
		
		if(konum == null)
			return;
		
		notificationGoster(konum);
		
	}

	@SuppressWarnings("deprecation")
	private void notificationGoster(Konum konum) {
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		String notificationBaslik = context.getResources().getString(R.string.yakinlik_notification_baslik);
		String notificationAyrinti = context.getResources().getString(R.string.yakinlik_notification_ayrinti, konum.getKullaniciAdi());
		Uri bildirimTonuUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		String bildirimTonu = tercihler.getString("PREF_BILDIRIM_TONU", null);
		if(!TextUtils.isEmpty(bildirimTonu))
			bildirimTonuUri = Uri.parse(bildirimTonu);
		
		Intent intent = new Intent(context, HaritaActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(HaritaActivity.ARKADAS_INTENT_EXTRA, konum.getKullaniciAdi());
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		notificationBuilder = new Notification.Builder(context);
		notificationBuilder.setAutoCancel(true)
						.setContentIntent(contentIntent)
						.setSound(bildirimTonuUri)
						.setContentTitle(notificationBaslik)
						.setContentText(notificationAyrinti)
						.setSmallIcon(R.drawable.ic_notification);
		
		notificationManager.notify(0, notificationBuilder.getNotification());
		
	}
	
	private String getKullaniciAdi() {
		
		DatabaseManager manager = new DatabaseManager(context);
		
		Profil profil = manager.profilSorgula(null);
		
		if(profil == null)
			return null;
		
		return profil.getKullaniciAdi();
	}
	
}
