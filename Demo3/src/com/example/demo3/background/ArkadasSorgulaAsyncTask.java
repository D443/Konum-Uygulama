package com.example.demo3.background;

import java.util.ArrayList;
import java.util.List;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.demo3.adapter.KimlerListAdapter;
import com.example.demo3.db.DatabaseManager;
import com.example.demo3.db.Profil;
import com.example.demo3.R;
import com.example.demo3.background.NetworkManager;

public class ArkadasSorgulaAsyncTask extends AsyncTask<Void, String, List<Profil>> {

	public static final String PROFIL_BULUNAMADI_ERROR = "-1";
	private String sonucKodu;
	private Context context;
	private ProgressDialog progressDialog;
	private ListFragment kimlerListFragment;
	public ArkadasSorgulaAsyncTask(Context context, ListFragment kimlerListFragment) {
		super();
		this.context = context;
		this.kimlerListFragment = kimlerListFragment;
	}
	
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "Lütfen Bekleyin...", "Ýþlem Yürütülüyor...", true, true);
	}

	@Override
	protected List<Profil> doInBackground(Void... params) {
		return getArkadasList();
	}

	private List<Profil> getArkadasList() {
		
		String kullaniciAdi = getKullaniciAdi();
		if(TextUtils.isEmpty(kullaniciAdi)) {
			sonucKodu = PROFIL_BULUNAMADI_ERROR;
			return new ArrayList<Profil>();
		}
		
		publishProgress("Arkadaþ listesi sorgulanýyor...");
		
		return NetworkManager.arkadasSorgula(kullaniciAdi);
		
	}
	
	protected void onProgressUpdate(String... progress) {
		progressDialog.setMessage(progress[0]);
	}

	@Override
	protected void onPostExecute(List<Profil> result) {
		
		String sonucMessage = getArkadasSorgulaSonucMessage(sonucKodu);
		
		if(!TextUtils.isEmpty(sonucMessage)) {
			Toast.makeText(context, sonucMessage, Toast.LENGTH_LONG).show();
			progressDialog.cancel();
			return;
		}
		
		if(result == null || result.size() == 0) {
    		String mesaj = context.getResources().getString(R.string.toast_arkadas_bulunamadi);
    		Toast.makeText(context, mesaj, Toast.LENGTH_LONG).show();
    		progressDialog.cancel();
			return;
    	}
		
		KimlerListAdapter adapter = new KimlerListAdapter(context, R.layout.arkadaslar, result);
		kimlerListFragment.getListView().setAdapter(adapter);
		kimlerListFragment.setListShown(true);
    	progressDialog.cancel();
    	
	}
	
	private String getArkadasSorgulaSonucMessage(String sonuc) {
		
		if(PROFIL_BULUNAMADI_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_kayitli_profil_yok);
		
		return null;
		
	}
	
	private String getKullaniciAdi() {
		
		DatabaseManager manager = new DatabaseManager(context);
		
		Profil profil = manager.profilSorgula(null);
		
		if(profil == null)
			return null;
		
		return profil.getKullaniciAdi();
	}






}
