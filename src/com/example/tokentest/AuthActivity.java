package com.example.tokentest;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class AuthActivity extends Activity {
	
	private AuthHelper 					_authHelper;
	private String[] 					_accountNames;
	private static final String TAG = 	"CC AuthActivity";
	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
	private String 						_strToken;
	private FeedManager 				_feedManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token);
		
		_authHelper = new AuthHelper();
		_accountNames = _authHelper.getAccountNames( this );
		//if ( _accountNames.length == 0 ) {
		getTokenFromAccount();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.token, menu);
		return true;
	}
	
	public String getToken() {
		return _strToken;
	}
	private void getTokenFromAccount() {
		AbstractGetTokenTask taskFetchToken = new AbstractGetTokenTask( this, _accountNames[0], SCOPE, 101 );
		taskFetchToken.execute();	
	}
	
	private void startUpdateActivity() {
		
		Intent intent = new Intent( this, UpdateActivity.class );
		intent.putExtra("userToken", _strToken );
		startActivity( intent );
	}
	
	public  class AbstractGetTokenTask extends AsyncTask<Void, Void, Void>{
	    private static final String TAG = "CC AbstractGetTokenTask";
	    //private static final String NAME_KEY = "given_name";
	    protected AuthActivity activity;

	    protected String strScope;
	    protected String strEmail;
	    protected int nRequestCode;
	    protected AuthHelper authHelper;
	    protected String strToken;
	    
	    AbstractGetTokenTask( AuthActivity activity, String email, String scope, int requestCode) {
	        this.activity = activity;
	        this.strScope = scope;
	        this.strEmail = email;
	        this.nRequestCode = requestCode;
	        authHelper = new AuthHelper();
	    }

	    @Override
	    protected Void doInBackground(Void... params) {

	    	_strToken = authHelper.fetchToken( activity, strEmail, strScope );
			return null;

	    }
	    
	    @Override
	    public void onPostExecute(Void result) {
	    	if ( _strToken != null ) {
	    		//Toast.makeText(getApplicationContext(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       ), "token: " + _strToken, Toast.LENGTH_SHORT  ).show();
	    		// notify the user is authenticated, token is recieved
	    		//startUpdateActivity();
	    		finish();
	    	}
        }
	}
	
}
