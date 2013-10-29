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
import com.example.tokentest.ListenerAwareAsyncTask.OnCompleteListener;

public class CheetahMainActivity extends Activity {

	
	private static final String TAG = "CR CheetahMainActivity";
	private boolean bCreated;
	
	private ProgressBar _progressBar = null;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token);
		
		bCreated = true;
		
		// First try to Login with a Google Account and retrieve the access token
		Intent intent = new Intent( this, AuthActivity.class );
		startActivity( intent );
		int result = 10001;
		startActivityForResult( intent, result );
		
		// listen for the event when the token is finished and somewhere, start the Update Activity
		
		// listen for event when update activity is finished, if it is, start the feedactivity

	}
	
@Override
public void onActivityResult( int result, int b, Intent i) {
	
	if ( result == 1001 ) {
		Bundle a = i.getBundleExtra("token");
		a.get("token");
	}

}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.token, menu);
		return true;
	}
	


}
