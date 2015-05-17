package com.example.demo3.db;

import android.provider.BaseColumns;

public final class DatabaseContract {

	public static final String DATABASE_NAME ="duzceuninerde";
	public static final String TABLE_NAME ="profil";
	public static final int DATABASE_VERSION=1;
	
	public static class Profil implements BaseColumns{
		private Profil() {}
		public static final String COLUMN_KULLANICI_ADI="kullanici_adi";
		public static final String COLUMN_SÝFRE="sifre";
		public static final String COLUMN_AD="ad";
		public static final String COLUMN_SOYAD = "soyad";
		public static final String COLUMN_TELEFON="telefon";
		public static final String COLUMN_EMAIL="email";
		public static final String DEFAULT_SORT_ORDER="ad ASC";
		public static final String[] FULL_PROJECTION=new String [] {_ID,
			COLUMN_KULLANICI_ADI,
			COLUMN_SÝFRE,
			COLUMN_AD,
			COLUMN_SOYAD,
			COLUMN_TELEFON,
			COLUMN_EMAIL};
		
	}
	}
