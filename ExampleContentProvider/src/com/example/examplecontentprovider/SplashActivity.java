package com.example.examplecontentprovider;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Timer().schedule(
				
				new TimerTask() {
					
					@Override
					public void run() {
						finish();
						
						Intent intent = new Intent();
						intent.setClass(SplashActivity.this, MainActivity.class);
						startActivity(intent);
						
					}
				}
				
				, 6000);
	}
	
}
