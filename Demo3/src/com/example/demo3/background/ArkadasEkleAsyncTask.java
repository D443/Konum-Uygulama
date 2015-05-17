package com.example.demo3.background;

import java.util.ArrayList;
import java.util.List;

import com.example.demo3.adapter.KimlerListAdapter;
import com.example.demo3.db.DatabaseManager;
import com.example.demo3.db.Profil;
import com.example.demo3.R;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;



public class ArkadasEkleAsyncTask extends AsyncTask<String, String, List<Profil>> {
	public static final String BASARILI = "1";
	public static final String BASARISIZ = "-1";
	public static final String ARKADAS_BULUNAMADI_ERROR = "-2";
	public static final String ARKADAS_ZATEN_MEVCUT_ERROR = "-3";
	public static final String PROFIL_BULUNAMADI_ERROR = "-4";
	private String sonucKodu;
	
	private Context context;
	private ProgressDialog progressDialog;
	private ListFragment kimlerListFragment;
	
	public ArkadasEkleAsyncTask(Context context, ListFragment kimlerListFragment) {
		super();
		this.context = context;
		this.kimlerListFragment = kimlerListFragment;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "Lütfen Bekleyin...", "Ýþlem Yürütülüyor...", true, true);
	}
	
	@Override
	protected List<Profil> doInBackground(String... params) {
		return arkadasEkle(params[0]);
	}
	
	private List<Profil> arkadasEkle(String arkadasKullaniciAdi) {
		
		String kullaniciAdi = getKullaniciAdi();
		if(TextUtils.isEmpty(kullaniciAdi)) {
			sonucKodu = PROFIL_BULUNAMADI_ERROR;
			return new ArrayList<Profil>();
		}
		
		publishProgress("Arkadaþ ekleniyor...");
		
		sonucKodu = NetworkManager.arkadasEkle(kullaniciAdi, arkadasKullaniciAdi);
		
		if(BASARILI.equals(sonucKodu)) {
			publishProgress("Liste güncelleniyor...");
			return NetworkManager.arkadasSorgula(kullaniciAdi);
		}
			
		return new ArrayList<Profil>();
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		progressDialog.setMessage(progress[0]);
	}
	
	@Override
	protected void onPostExecute(List<Profil> result) {
		
		String sonucMessage = getArkadasEkleSonucMessage(sonucKodu);
		Toast.makeText(context, sonucMessage, Toast.LENGTH_LONG).show();
		
		if(result == null || result.size() == 0) {
			progressDialog.cancel();
			return;
		}
		
		KimlerListAdapter adapter = new KimlerListAdapter(context, R.layout.arkadaslar, result);
		kimlerListFragment.getListView().setAdapter(adapter);
		kimlerListFragment.setListShown(true);
    	progressDialog.cancel();
		
	}

	private String getArkadasEkleSonucMessage(String sonuc) {
		
		if(BASARILI.equals(sonuc))
			return context.getResources().getString(R.string.toast_arkadas_ekle_basarili);
		else if(ARKADAS_BULUNAMADI_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_arkadas_profil_bulunamadi);
		else if(ARKADAS_ZATEN_MEVCUT_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_arkadas_zaten_mevcut);
		else if(PROFIL_BULUNAMADI_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_kayitli_profil_yok);
		else
			return context.getResources().getString(R.string.toast_bilinmeyen_hata);
		
	}
	
	private String getKullaniciAdi() {
		
		DatabaseManager manager = new DatabaseManager(context);
		
		Profil profil = manager.profilSorgula(null);
		
		if(profil == null)
			return null;
		
		return profil.getKullaniciAdi();
	}
}
