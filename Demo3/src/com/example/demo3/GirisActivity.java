package com.example.demo3;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.demo3.R;


public class GirisActivity extends BaseActivity {
	EditText kullaniciAdi,sifre;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.giris);
	
	        Button login=(Button)findViewById(R.id.button2);
	        Button uyeol=(Button)findViewById(R.id.button3);
	        kullaniciAdi=(EditText)findViewById(R.id.editText1);
	        sifre=(EditText)findViewById(R.id.editText2);
	        
	        final LinearLayout LoginBox = (LinearLayout) findViewById(R.id.LoginBox);
	        LoginBox.setVisibility(View.GONE);
	        uyeol.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent (GirisActivity.this,ProfilActivity.class);
					startActivity(intent);
					
				}
			});
	        login.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					kullaniciAdi=(EditText)findViewById(R.id.editText1);
				    sifre=(EditText)findViewById(R.id.editText2);
					 Intent mainMenu=new Intent(getApplicationContext(),MenuActivity.class);
						mainMenu.putExtra("login_username", kullaniciAdi.getText().toString());
						mainMenu.putExtra("login_password", sifre.getText().toString());
						startActivity(mainMenu);
				}
			});
	        final  Animation animTranslate  = AnimationUtils.loadAnimation(GirisActivity.this, R.anim.translate);
	        animTranslate.setAnimationListener(new AnimationListener() {
	        	 @Override
	             public void onAnimationStart(Animation arg0) { 
	        	
	        	 }

	             @Override
	             public void onAnimationRepeat(Animation arg0) { }

	             @Override
	             public void onAnimationEnd(Animation arg0) {
	                 LoginBox.setVisibility(View.VISIBLE);
	                 Animation animFade  = AnimationUtils.loadAnimation(GirisActivity.this, R.anim.fade);
	                 LoginBox.startAnimation(animFade);
	               
		}
	          
	        });
		               ImageView imgLogo = (ImageView) findViewById(R.id.imageView1);
		                imgLogo.startAnimation(animTranslate);
		               
		            }
	}



