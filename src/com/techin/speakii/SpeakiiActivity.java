package com.techin.speakii;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;

public class SpeakiiActivity extends Activity implements OnClickListener {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	private ListView mList;
	ImageView speakButton, nextButton,btnRepeat;
	TextView txtWord, txtReceivedMatches;
	String wordsStore[], currentWord;
	List<Integer> wordsList = new LinkedList<Integer>();
	ArrayList<String> proMatches;
	MediaPlayer player;
	  boolean playerUsed = false;

	private int successActivityID=1011;

	/**
	 * Called with the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.main);
		  player = new MediaPlayer();
		// Get display items for later interaction
		speakButton = (ImageView) findViewById(R.id.btnSpeak);
		nextButton = (ImageView) findViewById(R.id.btnNext);
		btnRepeat = (ImageView) findViewById(R.id.btnRepeat);
		txtWord = (TextView) findViewById(R.id.txtWord);
		txtReceivedMatches = (TextView) findViewById(R.id.txtReceivedMatches);
		wordsStore = new String[] { 

				"church",
				"market",
				"sit",
				"seat",
				"honor",
				"please",
				"pressure",
				"pot",
				"port",
				"oil",
				"look",
				"took",
				"sleep",
				"slip",
				"mother",
				"suite",
				"fat",
				"choir",
				"grace",
				"graze",
				"pain",
				"cow",
				"shift",
				"cat",
				"kiss",
				"fence",
				"house",
				"table",
				"sing",
				"cheese",
				"cheat",
				"peep",
				"morning",
				"money"
		
		};
		mList = (ListView) findViewById(R.id.list);

		// Check to see if a recognition activity is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {
			speakButton.setOnClickListener(this);
		} else {
			// speakButton.setEnabled(false);
			// speakButton.setText("Recognizer not present");
		}

		nextButton.setOnClickListener(this);
		btnRepeat.setOnClickListener(this);

		generateList();

		//This is a quick hack
		
		displayNextWord();
		

		
	}

	/**
	 * Handle the click on the start recognition button.
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.btnSpeak) {
			startVoiceRecognitionActivity();
		} else if (v.getId() == R.id.btnNext) {
			displayNextWord();
		}else if (v.getId() == R.id.btnRepeat) {
			playCurrentWord();
		}
	}

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speakii");
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			proMatches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			enableNext();

			showWordMatches(proMatches);
			checkResponse();

			mList.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, proMatches));
		}else if(requestCode==successActivityID){
			
			
			displayNextWord();
			
			
		}

		super.onActivityResult(requestCode, resultCode, data);
		
		
	}

	private void showWordMatches(ArrayList<String> matchesReturned) {
		// TODO Auto-generated method stub
		txtReceivedMatches.setText(matchesReturned.toString());
	}

	/**
	 * Checks the response of the user to the word.
	 */

	private void checkResponse() {
		// TODO Auto-generated method stub

		boolean correctlyPronounced = false;

		for (String word : proMatches) {
			if (word.contains(currentWord)) {
				// we have a match
				correctlyPronounced = true;
				break;

			}
		}

		if (correctlyPronounced) {

			double passRate = Math.ceil(100 / proMatches.size());
			//showMessage("Success!. Mark: " + passRate + "%");
			 showSuccess();
//			displayNextWord();
		} else {
			showRetry();
		}

	}

	/**
	 * Show a success message/Sound
	 */
	private void showSuccess() {
		// TODO Auto-generated method stub
//		showMessage("Correct!");
		

		 Intent intent;

		 intent = new Intent().setClass(this, SuccessActivity.class);

		 startActivityForResult(intent, successActivityID);
	}

	private void showMessage(String messageText) {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(messageText);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();

	}

	/**
	 * Show a failure message/Sound
	 */
	private void showFailed() {
		// TODO Auto-generated method stub
		showMessage("Failed!");

	}

	/**
	 * Show a retry message/Sound
	 */
	private void showRetry() {
		// TODO Auto-generated method stub
		showMessage("Please Try Again.\n What you said sounds like: \n"+proMatches.toString());
		
	}

	/**
	 * 
	 * Reshows the word
	 * 
	 */
	protected void displayCurrentWord() {

	}

	/**
	 * Moves the user to the next word
	 * 
	 */

	protected void displayNextWord() {
		
		Collections.shuffle(wordsList);

		int shuffle = wordsList.get(0);
		currentWord = wordsStore[shuffle];
		txtWord.setText(currentWord);
		imageizeCurrentWord();
		playCurrentWord();
		// disableNext();

	}

	private void imageizeCurrentWord() {
		// TODO Auto-generated method stub
		ImageView currentImageObj = (ImageView) findViewById(R.id.currentImage);
 
       
        
		try {
			  InputStream ims2 = getAssets().open("blank.png");
		         // load image as Drawable
		         Drawable d2 = Drawable.createFromStream(ims2, null);
		         // set image to ImageView
		        currentImageObj.setImageDrawable(d2);
			
			
             // get input stream
 	            InputStream ims = getAssets().open(currentWord+".jpg");
 	            // load image as Drawable
 	            Drawable d = Drawable.createFromStream(ims, null);
 	            // set image to ImageView
 	           currentImageObj.setImageDrawable(d);
 	        } catch(IOException ex) {
 	            return;
 	        }
	}

	protected void playCurrentWord() {
			//if(1>0) return ;
		try {
			// currentWord
			AssetFileDescriptor afd = getAssets().openFd(currentWord + ".mp3");
			
			if(playerUsed){
				player.reset();
			}
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

	}

	private void disableNext() {

		nextButton.setEnabled(false);

	}

	private void enableNext() {

		nextButton.setEnabled(true);

	}

	private void generateList() {
		for (int y = 0; y < wordsStore.length; y++) {
			wordsList.add(y);
		}
	}

}