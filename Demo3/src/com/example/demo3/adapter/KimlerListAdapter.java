package com.example.demo3.adapter;

import java.util.List;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.demo3.db.Profil;
import com.example.demo3.HaritaActivity;
import com.example.demo3.R;

public class KimlerListAdapter extends ArrayAdapter <Profil> {

	
	private List<Profil> arkadasList;
	private Context context;
	private int layoutResourceId;
	
	public KimlerListAdapter(Context context, int layoutResourceId, List<Profil> arkadasList) {
		super(context, layoutResourceId, arkadasList);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.arkadasList = arkadasList;
	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layoutResourceId, null);
		}
		
		Profil profil = arkadasList.get(position);
		
		TextView kimlerListKullaniciAdiTextView = (TextView) view.findViewById(R.id.kimlerListKullaniciAdiTextView);
		TextView kimlerListAdSoyadTextView = (TextView) view.findViewById(R.id.kimlerListAdSoyadTextView);
		
		kimlerListKullaniciAdiTextView.setText(profil.getKullaniciAdi());
		kimlerListAdSoyadTextView.setText(profil.getAd() + " " + profil.getSoyad());

		return view;
	}
	
	
	
	
	
}
