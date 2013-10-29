package com.example.tokentest;
import java.io.IOException;
import java.io.InputStream;

import com.example.tokentest.ListenerAwareAsyncTask.OnCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateActivity extends Activity {

	private String 		_strToken;
	private Simplecta 	_simplecta;
	private FeedManager _feedManager;
	private ProgressBar _progressBar;
	
	private static final String TAG = 	"CC UpdateActivity";
	
	static ListenerAwareAsyncTask<Integer, Integer, String > _myTask = null;
	
	private OnCompleteListener<Integer, String> _myAuthorizeTaskListener = new OnCompleteListener<Integer, String>() {

	      @Override
	      public void onComplete( String result) {
	          Toast.makeText( UpdateActivity.this, "Done!", Toast.LENGTH_LONG ).show();
	          finish();
	      }

	      @Override
	      public void onProgress(Integer... progress) {
	          _progressBar.setProgress(progress[0]);
	      }
	  };
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		_strToken = null;
		Bundle extras = getIntent().getExtras();
		if ( extras != null ) {
		    _strToken = extras.getString( "userToken" );
		}
		
		//if (  _strToken != null )
			//_simplecta = new Simplecta( this.getApplicationContext(), _strToken );
		//else
			//this.finish();
		
		// Get the html from simplecta and then pass it to the feedManager
		// the jsoup parse can parse an input stream but also a url as well.
		// e.g "http://porluz.sytes.net:8888/index_2.html"
		//doc = Jsoup.connect("http://porluz.sytes.net:8888/index_2.html").get();
		
		_simplecta = new Simplecta( this.getApplicationContext(), _strToken );
		_feedManager = FeedManager.getInstance();
		
		//Document doc = _feedManager.getDocument(); // this is calling a temporary url to parse
		
		// this will fill the articles and feeds which can be accessible from other classes.
		//_feedManager.fillArticles( doc );
		
		// Create a listener for update finished or error updating, let peeps know
		
	}
	
	public void update( Simplecta simplecta ) {
		InputStream is = simplecta.getHTMLStream();
		try {
			_feedManager.updateFeeds( is );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e( TAG, e.getMessage());
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}

	
	@Override
	  protected void onDestroy() {
	      super.onDestroy();
	      _progressBar = null;
	  }
	
	@Override
	  protected void onPause() {
	      super.onPause();
	      
	      if( _myTask != null) {
	    	  _myTask.unregister();
	      }
	  }
	
	@Override
	  protected void onResume() {
	      super.onResume();
	
	      if( _myTask == null ) {
	    	  _myTask = new ListenerAwareAsyncTask<Integer, Integer, String>( this, _myAuthorizeTaskListener ) {
	
	              @Override
	              protected String doInBackground(Integer... params) {
	                  int totalTime = params[0];
	                  int count = 0;
	                  
	                  while(count <= totalTime && !isCancelled()) {
	                      try {
	                    	  //update();
	                    	  
	                      } catch (Exception e) {
	                          // don't care!
	                      }
	                      
	                      this.publishProgress((int)(((float)count++ / (float)totalTime) * 100f));
	                  }
	                  return null;
	              }
	          };
	          _myTask.execute( 120 );
	      } else {
	          _myTask.register( this, _myAuthorizeTaskListener );
	      }
	  }

}
