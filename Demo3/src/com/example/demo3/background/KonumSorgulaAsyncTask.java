package com.example.demo3.background;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.demo3.HaritaActivity;
import com.example.demo3.R;
import com.example.demo3.db.DatabaseManager;
import com.example.demo3.db.Profil;

public class KonumSorgulaAsyncTask extends AsyncTask<Void, Void, List<Konum>>{

	private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private Context context;
	private GoogleMap googleMap;
	private Location location;
	
	public KonumSorgulaAsyncTask(Context context, Location location) {
		super();
		this.context = context;
		this.location = location;
		
		SupportMapFragment supportMapFragment = (SupportMapFragment) ((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = supportMapFragment.getMap();
	}

	@Override
	protected List<Konum> doInBackground(Void... params) {
		return getKonumList();
	}
	
	private List<Konum> getKonumList() {
		
		String kullaniciAdi = getKullaniciAdi();
		if(TextUtils.isEmpty(kullaniciAdi))
			return new ArrayList<Konum>();
		
		
		return NetworkManager.konumSorgula(kullaniciAdi);
	}
	
	@Override
	protected void onPostExecute(List<Konum> arkadasKonumListesi) {
		googleMap.clear();
		
		if(arkadasKonumListesi == null || arkadasKonumListesi.size() == 0)
			return;
        
        arkadaslariHaritayaEkle(arkadasKonumListesi);
		mevcutKonumuHaritayaEkle(location);
		haritayiOrtala(arkadasKonumListesi, location);
		
	}
	
	private void arkadaslariHaritayaEkle(List<Konum> arkadasKonumListesi) {
		String haritadaGosterilecekArkadas = getHaritadaGosterilecekArkadas();
		
		Iterator<Konum> iterator = arkadasKonumListesi.iterator();
		while (iterator.hasNext()) {
			Konum konum = (Konum) iterator.next();
			LatLng latLng = new LatLng(konum.getEnlem(), konum.getBoylam());
			String guncellemeZamani = context.getResources().getString(R.string.son_guncelleme, format.format(konum.getGuncellemeZamani()));
			
			MarkerOptions markerOptions = new MarkerOptions()
					.position(latLng)
					.title(konum.getKullaniciAdi())
					.snippet(guncellemeZamani)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_arkadas_konum));
			
			if(TextUtils.isEmpty(haritadaGosterilecekArkadas))
				googleMap.addMarker(markerOptions);
			else if(haritadaGosterilecekArkadas.equalsIgnoreCase(konum.getKullaniciAdi())) {
				googleMap.addMarker(markerOptions);
				break;
			}
		}
	}
	
	private void mevcutKonumuHaritayaEkle(Location location) {
		LatLng mevcutKonumLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		googleMap.addMarker(new MarkerOptions()
        .position(mevcutKonumLatLng)
        .anchor(0.5f, 0.5f)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mevcut_konum)));
	}
	
	private void haritayiOrtala(List<Konum> arkadasKonumListesi, Location location) {
		
		arkadasKonumListesi.add(new Konum(null, location.getLatitude(), location.getLongitude()));
		
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		
		Iterator<Konum> iterator = arkadasKonumListesi.iterator();
		while (iterator.hasNext()) {
			Konum konum = (Konum) iterator.next();
			builder.include(new LatLng(konum.getEnlem(), konum.getBoylam()));
		}
		
		LatLngBounds bounds = builder.build();
		
		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
	}

	private String getHaritadaGosterilecekArkadas() {
		
		Intent intent = ((Activity)context).getIntent();
        
        if(intent.getExtras() == null || intent.getExtras().getString(HaritaActivity.ARKADAS_INTENT_EXTRA) == null)
        	return null;
        
        return intent.getExtras().getString(HaritaActivity.ARKADAS_INTENT_EXTRA);
	}

	private String getKullaniciAdi() {
		
		DatabaseManager manager = new DatabaseManager(context);
		
		Profil profil = manager.profilSorgula(null);
		
		if(profil == null)
			return null;
		
		return profil.getKullaniciAdi();
	}
	
	
}
