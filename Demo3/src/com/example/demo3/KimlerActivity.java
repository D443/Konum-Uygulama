package com.example.demo3;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo3.R;
import com.example.demo3.background.ArkadasEkleAsyncTask;
import com.example.demo3.background.ArkadasSorgulaAsyncTask;
public class KimlerActivity extends BaseActivity {
private static final int ARKADAS_EKLE_DIALOG = 0;

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.kimler);
	
	ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
	
}

@Override
protected Dialog onCreateDialog(int id) {
	Dialog dialog;
	switch (id) {
		case ARKADAS_EKLE_DIALOG:
			dialog = getArkadasEkleDialog();
			break;
		default:
			dialog = null;
	}
	
	return dialog;
}

private Dialog getArkadasEkleDialog() {
	
	final Dialog arkadasEkleDialog = new Dialog(this);
	
	arkadasEkleDialog.setContentView(R.layout.arkadas_ekle_popup);
	arkadasEkleDialog.setTitle(getResources().getString(R.string.arkadas_ekle));
	
	final EditText arkadasEkleEditText = (EditText) arkadasEkleDialog.findViewById(R.id.kullaniciAdiDialogEditText);
	
	Button arkadasEkleButton = (Button) arkadasEkleDialog.findViewById(R.id.ekleArkadasEkleDialogButton);
	arkadasEkleButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Editable kullaniciAdiEditable = arkadasEkleEditText.getText();
			String kullaniciAdi = (kullaniciAdiEditable != null) ? kullaniciAdiEditable.toString() : "";
        	arkadasEkle(kullaniciAdi);
        	arkadasEkleDialog.dismiss();
        }
    });
	
	Button iptalButton = (Button) arkadasEkleDialog.findViewById(R.id.iptalArkadasEkleDialogButton);
	iptalButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	arkadasEkleDialog.dismiss();
        }
    });
	
	return arkadasEkleDialog;
	
}

private void arkadasEkle(String arkadasKullaniciAdi) {
	
	if(TextUtils.isEmpty(arkadasKullaniciAdi)) {
		String message = getResources().getString(R.string.toast_bos_parametre_hatasi, "Kullanýcý Adý");
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		return;
	}
	
	FragmentManager fragmentManager = getFragmentManager();
	ListFragment kimlerListFragment = (ListFragment) fragmentManager.findFragmentById(R.id.kimlerListFragment);
	ArkadasEkleAsyncTask task = new ArkadasEkleAsyncTask(this, kimlerListFragment);
	task.execute(arkadasKullaniciAdi);
	
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu_arkadas, menu);
	return true;
}

public boolean onOptionsItemSelected(MenuItem item) {
    
	switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(this, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        case R.id.kimlerActionEkleMenuItem:
            showDialog(ARKADAS_EKLE_DIALOG);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	
}
}