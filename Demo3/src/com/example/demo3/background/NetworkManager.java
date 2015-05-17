package com.example.demo3.background;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.TextUtils;
import android.util.Log;

import com.example.demo3.BaseActivity;
import com.example.demo3.Fonksiyonlar;
import com.example.demo3.db.Profil;

public class NetworkManager {
private static String sifre;
private static final String TAG = "NetworkManager";
	
	public static String profilKaydet(Profil profil) {
		
		BufferedReader in = null;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(BaseActivity.DUZCE_UNÝ_NERDE_PROFIL_KAYDET_URL);
			sifre=profil.getSifre();
			sifre=Fonksiyonlar.sha1(sifre);
			List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
			parametreList.add(new BasicNameValuePair("kullanici_adi", profil.getKullaniciAdi()));
			parametreList.add(new BasicNameValuePair("sifre",sifre));
			parametreList.add(new BasicNameValuePair("ad", profil.getAd()));
			parametreList.add(new BasicNameValuePair("soyad", profil.getSoyad()));
			parametreList.add(new BasicNameValuePair("telefon", profil.getTelefon()));
			parametreList.add(new BasicNameValuePair("email", profil.getEmail()));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList,"UTF-8");
			request.setEntity(entity);
						
			HttpResponse response = client.execute(request);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
			
			return in.readLine();
		
		} catch (Exception e) {
			Log.d(TAG, "Profil kaydedilirken hata oluþtu", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}


public static List<Profil> arkadasSorgula(String kullaniciAdi) {
	
	
	InputStream content=null;
	try {
		HttpClient client=new DefaultHttpClient();
		HttpPost request=new HttpPost(
				BaseActivity.DUZCE_UNÝ_NERDE_ARKADAS_LÝSTELE_URL);
		List <NameValuePair> parametreList=new ArrayList<NameValuePair>();
		parametreList.add(new BasicNameValuePair("kullanici_adi", kullaniciAdi));
		UrlEncodedFormEntity entity=new UrlEncodedFormEntity(parametreList);
		request.setEntity(entity);
		HttpResponse response=client.execute(request);
		content=response.getEntity().getContent();
		List <Profil> arkadasList=getArkadasListInputStream(content);
		return arkadasList;
		
	} catch (Exception e) {
		// TODO: handle exception
		Log.d(TAG,"Http Baðlantýsý kurulurken hataoluþtu",e);
		
	}
	finally {
		if(content!=null)
		{
			try{content.close();}
			catch(Exception e) { e.printStackTrace(); }
		}
			
			
	}
	return new ArrayList<Profil>();
}

private static List<Profil> getArkadasListInputStream(InputStream content) {
	
	List<Profil> arkadasList = new ArrayList<Profil>();
	
	try {
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(content);
		Element root = document.getDocumentElement();
		NodeList arkadasNodeList = root.getElementsByTagName("arkadas");
		
		if(arkadasNodeList == null || arkadasNodeList.getLength() == 0)
			return arkadasList;
		
		int arkadasSayisi = arkadasNodeList.getLength();
		
		for (int i = 0; i < arkadasSayisi; i++) {
			Element arkadas = (Element) arkadasNodeList.item(i);
			Node kullaniciAdiNode = arkadas.getElementsByTagName("kullanici_adi").item(0);
			String kullaniciAdi = kullaniciAdiNode.getFirstChild().getNodeValue();
			
			
			
			Node adNode = arkadas.getElementsByTagName("ad").item(0).getFirstChild();
			String ad = adNode.getNodeValue();
			
			Node soyadNode = arkadas.getElementsByTagName("soyad").item(0).getFirstChild();
			String soyad = soyadNode.getNodeValue();
			
			Node telefonNode = arkadas.getElementsByTagName("telefon").item(0).getFirstChild();
			String telefon = (telefonNode != null) ? telefonNode.getNodeValue() : "";
			
			Node emailNode = arkadas.getElementsByTagName("email").item(0).getFirstChild();
			String email = (emailNode != null) ? emailNode.getNodeValue() : "";
			
			Profil profil = new Profil();
			profil.setKullaniciAdi(kullaniciAdi);
			
			profil.setAd(ad);
			profil.setSoyad(soyad);
			profil.setTelefon(telefon);
			profil.setEmail(email);
			
			arkadasList.add(profil);
		}
		
	} catch (Exception e) {
		Log.d(TAG, "XML parse edilirken hata oluþtu", e);
	}
	
	return arkadasList;
	
}

public static String arkadasEkle(String kullaniciAdi, String arkadasKullaniciAdi) {
	
	BufferedReader in = null;
	try {

		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(BaseActivity.DUZCE_UNÝ_NERDE_ARKADAS_KAYDET_URL);
		
		List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
		parametreList.add(new BasicNameValuePair("kullanici_adi", kullaniciAdi));
		parametreList.add(new BasicNameValuePair("arkadas_kullanici_adi", arkadasKullaniciAdi));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
		request.setEntity(entity);
					
		HttpResponse response = client.execute(request);
		
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		return in.readLine();
	
	} catch (Exception e) {
		Log.d(TAG, "Arkadaþ eklenirken hata oluþtu", e);
	} finally {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	return null;
	
}
public static String konumKaydet(Konum konum) {
	
	BufferedReader in = null;
	try {

		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(BaseActivity.DUZCE_UNÝ_NERDE_KONUM_KAYDET);
		
		List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
		parametreList.add(new BasicNameValuePair("kullanici_adi", konum.getKullaniciAdi()));
		parametreList.add(new BasicNameValuePair("enlem", String.valueOf(konum.getEnlem())));
		parametreList.add(new BasicNameValuePair("boylam", String.valueOf(konum.getBoylam())));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
		request.setEntity(entity);
					
		HttpResponse response = client.execute(request);
		
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		return in.readLine();
	
	} catch (Exception e) {
		Log.d(TAG, "Konum kaydedilirken hata oluþtu", e);
	} finally {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	return null;
	
}
public static List<Konum> konumSorgula(String kullaniciAdi) {
	
	if(TextUtils.isEmpty(kullaniciAdi))
		return new ArrayList<Konum>();
	
	InputStream content = null;
	
	try {
		
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(BaseActivity.DUZCE_UNÝ_NERDE_KONUM_SORGULA);
		
		List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
		parametreList.add(new BasicNameValuePair("kullanici_adi", kullaniciAdi));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
		request.setEntity(entity);
					
		HttpResponse response = client.execute(request);

		content = response.getEntity().getContent();
		
		List<Konum> konumList = getKonumListInputStream(content);
		
		return konumList;
		
		
	} catch (Exception e) {
		Log.d(TAG, "HTTP baðlantýsý kurulurken hata oluþtu", e);
	} finally {
		if (content != null) {
			try {
				content.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	return new ArrayList<Konum>();
	
}
private static List<Konum> getKonumListInputStream(InputStream content) {
	
	List<Konum> konumList = new ArrayList<Konum>();
	
	try {
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(content);
		Element root = document.getDocumentElement();
		NodeList konumNodeList = root.getElementsByTagName("arkadas");
		
		if(konumNodeList == null || konumNodeList.getLength() == 0)
			return konumList;
		
		int arkadasSayisi = konumNodeList.getLength();
		
		for (int i = 0; i < arkadasSayisi; i++) {
			Element arkadas = (Element) konumNodeList.item(i);
			Node kullaniciAdiNode = arkadas.getElementsByTagName("kullanici_adi").item(0);
			String kullaniciAdi = kullaniciAdiNode.getFirstChild().getNodeValue();
			
			Node enlemNode = arkadas.getElementsByTagName("enlem").item(0).getFirstChild();
			String enlem = enlemNode.getNodeValue();
			
			Node boylamNode = arkadas.getElementsByTagName("boylam").item(0).getFirstChild();
			String boylam = boylamNode.getNodeValue();
			
			Node guncellemeZamaniNode = arkadas.getElementsByTagName("guncelleme_zamani").item(0).getFirstChild();
			String guncellemeZamani = guncellemeZamaniNode.getNodeValue(); 
			
			Konum konum = new Konum();
			konum.setKullaniciAdi(kullaniciAdi);
			konum.setEnlem(Double.valueOf(enlem));
			konum.setBoylam(Double.valueOf(boylam));
			konum.setGuncellemeZamani(Timestamp.valueOf(guncellemeZamani));
			
			konumList.add(konum);
		}
		
	} catch (Exception e) {
		Log.d(TAG, "Konum XML parse edilirken hata oluþtu", e);
	}
	
	return konumList;
	
}
}











