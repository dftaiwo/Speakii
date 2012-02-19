package com.techin.speakii;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SuccessActivity extends Activity implements OnClickListener{
	
	
	
	
	private ImageView continueButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Inflate our UI from its XML layout description.
		setContentView(R.layout.success);
		
		continueButton = (ImageView) findViewById(R.id.btnGo);
		
		
		 
		continueButton.setOnClickListener(this);
		 
		
//		TranslateAnimation anim = new TranslateAnimation(0,10, 0, 0);
//		anim.setInterpolator(new LinearInterpolator());
//		anim.setRepeatCount(Animation.INFINITE);
//		anim.setDuration(700);
//
//		// Start animating the image
//		final ImageView splash = (ImageView) findViewById(R.id.btnContinue);
//		splash.startAnimation(anim);
		
		
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
			finish();
		
	}

}
