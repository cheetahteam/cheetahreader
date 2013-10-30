package com.example.tokentest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;

public class FeedActivity extends Activity {

	private Simplecta 					_simplecta;
	private FeedManager 				_feedManager;
	private ProgressBar 				_progressBar;
	private AuthPreferences 			_authPreferences;
	private static final String TAG = 	"CC FeedActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token);
		
		_feedManager = FeedManager.getInstance();
		_authPreferences = new AuthPreferences(this);
		_simplecta = Simplecta.getInstance();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.token, menu);
		return true;
	}
	
	public void drawFeeds() {
		// Get the current articles
		ArrayList<Article> articles = _feedManager.getArticles();
		// Draw Text Button Links
		
	}
	public  class AbstractUpdateFeedTask extends AsyncTask<Void, Void, Void>{
	    private static final String TAG = "CC TokenInfoTask";
	    //private static final String NAME_KEY = "given_name";
	    protected FeedActivity activity;
	    protected String strScope;
	    protected String strEmail;
	    protected int nRequestCode;
	    protected AuthHelper authHelper;
	    protected String strToken;
	    
	    AbstractUpdateFeedTask( FeedActivity activity, String email, String scope, int requestCode) {
	        this.activity = activity;
	        this.strScope = scope;
	        this.strEmail = email;
	        this.nRequestCode = requestCode;
	        authHelper = new AuthHelper();
	    }

	    @Override
	    protected Void doInBackground(Void... params) {

	    	
	    	//InputStream is = simplecta.getHTMLStream();
	    	InputStream is = null;
	    	try {
	    		_simplecta.init( getApplicationContext(), _authPreferences.getToken() );
	    		is = _simplecta.getHTMLStream();
				_feedManager.updateFeeds( is );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e( TAG, e.getMessage() );
			}
			return null;

	    }
	    
	    @Override
	    public void onPostExecute(Void result) {
	    	drawFeeds();
        }
	}
}
