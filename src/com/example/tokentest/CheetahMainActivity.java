package com.example.tokentest;

import java.io.IOException;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auth.AuthActivity;

public class CheetahMainActivity extends Activity {

	
	private static final String TAG = "CR CheetahMainActivity";
	private boolean _bCreated;
	

	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token);
		
		_bCreated = true;
		
		// First try to Login with a Google Account and retrieve the access token
		Intent intent = new Intent( this, AuthActivity.class );
		startActivity( intent );
		//int result = 10001;
		//startActivityForResult( intent, result );
		
		// After AuthActivty is done, onActivityResult gets called with the token extra.
		// validate the token, then launch feed activty with the token
		// feedactivty on create will call it's update method

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.token, menu);
		return true;
	}
	


}
