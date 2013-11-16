package com.example.tokentest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.auth.AuthPreferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class FeedActivity extends Activity {

	/* Manages API calls to Google App Engine app: Simplecta */
	private Simplecta 					_simplecta;
	private FeedManager 				_feedManager;
	private AuthPreferences 			_authPreferences;
	private static final String TAG = 	"CC FeedActivity";
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.id.listV1);
		
		_feedManager = FeedManager.getInstance();
		_authPreferences = new AuthPreferences(this);
		_simplecta = Simplecta.getInstance();
		
		initiliazeUpdateFeedsBtn();
		
		// Run the first update
		updateFeeds();
		
	}
	
	private void initiliazeUpdateFeedsBtn() {
        Button getToken = (Button) findViewById(R.id.btnFeed);
        getToken.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	updateFeeds();
            }
        });
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
		AbstractUpdateFeedTask task = new AbstractUpdateFeedTask( this );
		task.execute();
	}
	
	public  class AbstractUpdateFeedTask extends AsyncTask<Void, Void, Void>{
	    private static final String TAG = "CC AbstractUpdateFeedTask";

	    /** progress dialog to show user that the update is processing. */
	    ProgressDialog dialog;
	    Activity activity;
	    
	    
	    public AbstractUpdateFeedTask(Activity activity) {
	        this.activity = activity;
	        this.dialog = new ProgressDialog( activity );
	    }
	    
	    @Override
	    protected void onPreExecute() {
	    	this.dialog.setTitle("Downloading Feeds...");
	    	this.dialog.setMessage("Please wait.");
	    	this.dialog.setCancelable(false);
	    	this.dialog.setIndeterminate(true);
	    	this.dialog.show();
	    }

	    @Override
	    protected Void doInBackground(Void... params) {

	    	
	    	//InputStream is = simplecta.getHTMLStream();
	    	String strArticlesHTML = null;
	    	try {
	    		// Get the HTML String from the Simplecta connection
	    		strArticlesHTML = _simplecta.showAll();
	    		// Parse the html into the article/feed objects
				_feedManager.updateFeeds( strArticlesHTML );
	    		//_feedManager.updateFeeds( _simplecta.getAllURL() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e( TAG, e.getMessage() );
			}
			return null;

	    }
	    
	    @Override
	    public void onPostExecute(Void result) {
	    	if (this.dialog.isShowing()) {
	    		this.dialog.dismiss();
	        }
	    	drawFeeds();
        }
	}
	
	@Override 
    protected void onDestroy() {
    	//if (dialog!=null) {
    	//	dialog.dismiss();
			//b.setEnabled(true);
		//}
    	super.onDestroy();
    }
	
}
