package com.example.demo3.background;

import java.sql.Timestamp;

public class Konum {
	
	private String kullaniciAdi;
	private double enlem;
	private double boylam;
	private Timestamp guncellemeZamani;
	
	public Konum() {}

	public Konum(String kullaniciAdi, double enlem, double boylam) {
		this.kullaniciAdi = kullaniciAdi;
		this.enlem = enlem;
		this.boylam = boylam;
	}

	public String getKullaniciAdi() {
		return kullaniciAdi;
	}

	public void setKullaniciAdi(String kullaniciAdi) {
		this.kullaniciAdi = kullaniciAdi;
	}

	public double getEnlem() {
		return enlem;
	}

	public void setEnlem(double enlem) {
		this.enlem = enlem;
	}

	public double getBoylam() {
		return boylam;
	}

	public void setBoylam(double boylam) {
		this.boylam = boylam;
	}

	public Timestamp getGuncellemeZamani() {
		return guncellemeZamani;
	}

	public void setGuncellemeZamani(Timestamp guncellemeZamani) {
		this.guncellemeZamani = guncellemeZamani;
	}
}
