package com.example.tokentest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.auth.AuthPreferences;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class FeedActivity extends Activity {

	/* Manages API calls to Google App Engine app: Simplecta */
	private Simplecta 					_simplecta;
	private FeedManager 				_feedManager;
	private ProgressBar 				_progressBar;
	private AuthPreferences 			_authPreferences;
	private static final String TAG = 	"CC FeedActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		
		_feedManager = FeedManager.getInstance();
		_authPreferences = new AuthPreferences(this);
		_simplecta = Simplecta.getInstance();
		updateFeeds();
		
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
		TextView list = (TextView) findViewById(R.id.list);
		String feed = "";
		for (int i = 0; i < articles.size(); i++ ) {
			feed = feed + articles.get(i).getTitle() + "\n";
		}
		list.setText(feed);
		
	}
	
	public void updateFeeds() {
		AbstractUpdateFeedTask task = new AbstractUpdateFeedTask();
		task.execute();
	}
	
	public  class AbstractUpdateFeedTask extends AsyncTask<Void, Void, Void>{
	    private static final String TAG = "CC AbstractUpdateFeedTask";


	    @Override
	    protected Void doInBackground(Void... params) {

	    	
	    	//InputStream is = simplecta.getHTMLStream();
	    	String strHTML = null;
	    	try {
	    		strHTML = _simplecta.showAll();
				_feedManager.updateFeeds( strHTML );
	    		//_feedManager.updateFeeds( _simplecta.getAllURL() );
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
