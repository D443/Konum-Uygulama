package com.example.demo3.db;

import java.io.File;
import java.io.FileOutputStream;

import com.example.demo3.BaseActivity;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import android.util.Log;



public class DatabaseManager {
	public static final int BASARILI = 1;
	public static final int BILINMEYEN_HATA = -1;
	public static final int PROFIL_VALIDASYON_HATASI = -2;
	
	private DatabaseHelper helper;
	private Context context;
	
	public DatabaseManager(Context context) {
		this.context = context;
		helper = new DatabaseHelper(context);
	}
	public Profil profilSorgula (String kullaniciAdi)
	{
		String where = null;
	String [] whereArgs = null;
	
	
	if(!TextUtils.isEmpty(kullaniciAdi)) {
		where = DatabaseContract.Profil.COLUMN_KULLANICI_ADI + "= ?";
    	whereArgs = new String [] {kullaniciAdi};
	}
	else {where=null; whereArgs=null; }
	SQLiteDatabase db = helper.getReadableDatabase();
	Cursor cursor = db.query(DatabaseContract.TABLE_NAME, DatabaseContract.Profil.FULL_PROJECTION, where, whereArgs, null, null, null);
	
	return buildProfil(cursor);
		
		
	}
	private Profil buildProfil(Cursor cursor)
	{//Burayý incele
		if(cursor == null || cursor.getCount() != 1 || !cursor.moveToNext())
    		return null;
		
		Profil profil = new Profil();
		
		int idIndex = cursor.getColumnIndex(DatabaseContract.Profil._ID);
		profil.setId(cursor.getInt(idIndex));
		
		int kullaniciAdiIndex = cursor.getColumnIndex(DatabaseContract.Profil.COLUMN_KULLANICI_ADI);
		profil.setKullaniciAdi(cursor.getString(kullaniciAdiIndex));
		
		int sifreIndex = cursor.getColumnIndex(DatabaseContract.Profil.COLUMN_SÝFRE);
		profil.setSifre(cursor.getString(sifreIndex));
		
		int adIndex = cursor.getColumnIndex(DatabaseContract.Profil.COLUMN_AD);
		profil.setAd(cursor.getString(adIndex));
		
		int soyadIndex = cursor.getColumnIndex(DatabaseContract.Profil.COLUMN_SOYAD);
		profil.setSoyad(cursor.getString(soyadIndex));
		
		int telefonIndex = cursor.getColumnIndex(DatabaseContract.Profil.COLUMN_TELEFON);
		profil.setTelefon(cursor.getString(telefonIndex));
		
		int emailIndex = cursor.getColumnIndex(DatabaseContract.Profil.COLUMN_EMAIL);
		profil.setEmail(cursor.getString(emailIndex));	
		
		Bitmap profilPhoto = profilPhotoSorgula();
		profil.setProfilPhoto(profilPhoto);
		
		return profil;
		
	}
	public int profilKaydetGuncelle(Profil profil) {
		//incele
		if(!isProfilValid(profil))
			return PROFIL_VALIDASYON_HATASI;
		
		ContentValues satir = new ContentValues();
    	satir.put(DatabaseContract.Profil.COLUMN_KULLANICI_ADI, profil.getKullaniciAdi());
    	satir.put(DatabaseContract.Profil.COLUMN_SÝFRE, profil.getSifre());
    	satir.put(DatabaseContract.Profil.COLUMN_AD, profil.getAd());
    	satir.put(DatabaseContract.Profil.COLUMN_SOYAD, profil.getSoyad());
    	satir.put(DatabaseContract.Profil.COLUMN_TELEFON, profil.getTelefon());
    	satir.put(DatabaseContract.Profil.COLUMN_EMAIL, profil.getEmail());
    	
    	profilPhotoKaydet(profil.getProfilPhoto());
    	
		Profil kayitliProfil = profilSorgula(profil.getKullaniciAdi());
		
		if(kayitliProfil != null)
			return profilGuncelle(kayitliProfil.getId(), satir);
		
		return profilKaydet(satir);
		
	}
public int profilKaydet(ContentValues satir) {
		
	SQLiteDatabase db = helper.getWritableDatabase();
	long profilId = db.insert(DatabaseContract.TABLE_NAME, null, satir);
	
	if(profilId == -1)
		return BILINMEYEN_HATA;
	
	return BASARILI;
		
	}
public int profilGuncelle(int id, ContentValues satir) {
	
	SQLiteDatabase db = helper.getWritableDatabase();
	String where = DatabaseContract.Profil._ID + "=" + id;
	
	int guncellenenSatirSayisi = db.update(DatabaseContract.TABLE_NAME, satir, where, null);
	
	if(guncellenenSatirSayisi != 1)
		return BILINMEYEN_HATA;
	
	return BASARILI;
	
}
private boolean isProfilValid(Profil profil) {
	
	if(profil == null)
		return false;
	
	
	if (TextUtils.isEmpty(profil.getKullaniciAdi())
			|| TextUtils.isEmpty(profil.getAd())
			|| TextUtils.isEmpty(profil.getSoyad()))
		return false;
	
	return true;
}
private void profilPhotoKaydet(Bitmap profilPhoto) {
	
	try {
		
		if(profilPhoto == null)
			return;
		
		FileOutputStream fos = context.openFileOutput(BaseActivity.PROFILE_PHOTO_FILE_NAME, Context.MODE_PRIVATE);
		profilPhoto.compress(CompressFormat.JPEG, 100, fos);
		fos.close();
		
    } catch (Exception e) {
        Log.e("DatabaseManager", "Profil Fotografi kaydedilirken hata olustu", e);
    }
	
}
private Bitmap profilPhotoSorgula() {
	
	File icBellekAdres = context.getFilesDir();
	
	if(icBellekAdres == null)
		return null;
	
	File profilPhotoFile = new File(icBellekAdres, BaseActivity.PROFILE_PHOTO_FILE_NAME);
	
	if(!profilPhotoFile.exists())
		return null;
	
	return BitmapFactory.decodeFile(profilPhotoFile.getAbsolutePath());
	
	
}
	
}
