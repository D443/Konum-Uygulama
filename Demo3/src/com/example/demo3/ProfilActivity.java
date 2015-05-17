package com.example.demo3;




import com.example.demo3.background.ProfilKaydetAsyncTask;
import com.example.demo3.db.DatabaseManager;
import com.example.demo3.db.Profil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



public class ProfilActivity extends BaseActivity {
private DatabaseManager manager;
private static final int CAMERA_REQUEST=1;
private ImageButton profilImageButton;
private Bitmap profilPhoto;
private EditText kullaniciAdiEditText;
private EditText sifre; 
private EditText adEditText;
private EditText soyadEditText;
private EditText telefonEditText;
private EditText emailEditText;
@Override
public void onCreate (Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.uye_ol);
	
	ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
	ekranKontrolleriniOlustur();
	
	
}
private void ekranKontrolleriniOlustur() {
	manager=new DatabaseManager(this);
	final Profil profil=manager.profilSorgula(null);
	profilImageButton =(ImageButton)findViewById(R.id.profilImageButton);
	kullaniciAdiEditText=(EditText)findViewById(R.id.kullaniciAdiEditText);
	sifre=(EditText) findViewById(R.id.kullanici_sifre);
	adEditText=(EditText)findViewById(R.id.adEditText);
	soyadEditText=(EditText)findViewById(R.id.soyadEditText);
	telefonEditText=(EditText)findViewById(R.id.telefonEditText);
	emailEditText=(EditText)findViewById(R.id.emailEditText);
	ekranGuncelle(profil);
Button kaydetButton=(Button)findViewById(R.id.kaydetButton);
kaydetButton.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Profil kaydedilecekProfil=ekranDegerleriniOku();
		int sonuc=manager.profilKaydetGuncelle(kaydedilecekProfil);
		
		if(DatabaseManager.BASARILI==sonuc)
		{
			ProfilKaydetAsyncTask task=new ProfilKaydetAsyncTask(ProfilActivity.this);
			task.execute(kaydedilecekProfil);
			
		}
		else {
			String profilKaydetSonucMessage=getProfilKaydetSonucMessage(sonuc);
			Toast.makeText(getApplicationContext(), profilKaydetSonucMessage, Toast.LENGTH_LONG).show();
			
		}
			
		
	}
});
profilImageButton.setOnClickListener(new View.OnClickListener() {
	
	

public void onClick(View v) {
	Intent cameraIntent=new Intent (android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	startActivityForResult(cameraIntent, CAMERA_REQUEST);
	
}

});
}

private void ekranGuncelle(Profil profil) {
	if(profil == null)
		return;
	kullaniciAdiEditText.setText(profil.getKullaniciAdi());
	kullaniciAdiEditText.setEnabled(false);
	sifre.setText(profil.getSifre());
	sifre.setEnabled(false);
	adEditText.setText(profil.getAd());
	soyadEditText.setText(profil.getSoyad());
	telefonEditText.setText(profil.getTelefon());
	emailEditText.setText(profil.getEmail());
	if(profil.getProfilPhoto()!=null)
	{
		profilImageButton.setImageBitmap(profil.getProfilPhoto());
	} }

private Profil ekranDegerleriniOku() {
	
	Profil profil = new Profil();
	
	if(kullaniciAdiEditText.getText() != null)
		profil.setKullaniciAdi(kullaniciAdiEditText.getText().toString());
	if(sifre.getText() != null)
		profil.setSifre(sifre.getText().toString());
	
	if(adEditText.getText() != null)
		profil.setAd(adEditText.getText().toString());
	
	if(soyadEditText.getText() != null)
		profil.setSoyad(soyadEditText.getText().toString());
	
	if(telefonEditText.getText() != null)
		profil.setTelefon(telefonEditText.getText().toString());
	
	if(emailEditText.getText() != null)
		profil.setEmail(emailEditText.getText().toString());
	
	if(profilPhoto != null)
		profil.setProfilPhoto(profilPhoto);
	
	return profil;
}
private String getProfilKaydetSonucMessage(int sonuc) {
	
	if(DatabaseManager.BASARILI == sonuc)
		return getResources().getString(R.string.toast_profil_kaydet_basarili);
	
	if(DatabaseManager.PROFIL_VALIDASYON_HATASI == sonuc)
		return getResources().getString(R.string.toast_profil_validasyon_hatasi);
	
	return getResources().getString(R.string.toast_bilinmeyen_hata);
		
}
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	switch (requestCode) {
		case CAMERA_REQUEST:
			
			if(resultCode == Activity.RESULT_OK) {
				profilPhoto = (Bitmap) data.getExtras().get("data");
				
				if(profilPhoto != null) {
					
					profilImageButton.setImageBitmap(null);
					profilImageButton.setImageBitmap(profilPhoto);
				}
			}
			
			break;
	}

}
private Bitmap profilPhotoYenidenBoyutlandir(Bitmap profilPhoto) {
	
	int en = profilPhoto.getWidth();
	int boy = profilPhoto.getHeight();
	
	int yeniEn = (en >= boy) ? 100: (int) ((float) 100 * ((float) en / (float) boy));
    int yeniBoy = (boy >= en) ? 100 : (int) ((float) 100 * ((float) boy / (float) en));

    return Bitmap.createScaledBitmap(profilPhoto, yeniEn, yeniBoy, true);
	
}
public boolean onOptionsItemSelected(MenuItem item) {
    
	switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(this, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	
}
}