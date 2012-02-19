package com.techin.speakii;

 

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SplashActivity extends Activity implements OnClickListener {
	
	
	MediaPlayer player;
	  boolean playerUsed = false;
	ImageView continueButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		  player = new MediaPlayer();

		try {
			// currentWord
			AssetFileDescriptor afd = getAssets().openFd("startup.mp3");
		
			player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			player.prepare();
			player.start();
			playerUsed = true;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		
		// Inflate our UI from its XML layout description.
		setContentView(R.layout.splash);

		// Get display items for later interaction
		continueButton = (ImageView) findViewById(R.id.btnContinue);
		 
		continueButton.setOnClickListener(this);
		 
		
		TranslateAnimation anim = new TranslateAnimation(0,10, 0, 0);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(700);

		// Start animating the image
		final ImageView splash = (ImageView) findViewById(R.id.btnContinue);
		splash.startAnimation(anim);

		

		
		
	}

	/**
	 * Handle the click on the start recognition button.
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.btnContinue) {
			
			
			
			if(playerUsed){
				player.stop();
			}
			 int activityID = 0x100;

			 Intent intent;

			 intent = new Intent().setClass(this, SpeakiiActivity.class);

			 startActivityForResult(intent, activityID);
	   	
			
			 
		}
	}
	
	

}
