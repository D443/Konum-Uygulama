package com.example.demo3.db;

import android.graphics.Bitmap;

public class Profil {
	private int id;
	private String kullaniciAdi;
	private String sifre;
	private String ad;
	private String soyad;
	private String telefon;
	private String email;
	private Bitmap profilPhoto;
	public Profil() {
		
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKullaniciAdi() {
		return kullaniciAdi;
	}

	public void setKullaniciAdi(String kullaniciAdi) {
		this.kullaniciAdi = kullaniciAdi;
	}
	public String getSifre() {
		return sifre;
	}



	public void setSifre(String sifre) {
		this.sifre = sifre;
	}
	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public String getSoyad() {
		return soyad;
	}

	public void setSoyad(String soyad) {
		this.soyad = soyad;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Bitmap getProfilPhoto() {
		return profilPhoto;
	}

	public void setProfilPhoto(Bitmap profilPhoto) {
		this.profilPhoto = profilPhoto;
	}
	
}