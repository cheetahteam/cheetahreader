package gui;


import com.example.tokentest.Article;
import com.example.tokentest.FeedManager;
import com.example.tokentest.R;
import com.example.tokentest.Simplecta;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedArticleActivity extends ListActivity {
	
	private Activity activity = this;
	private Simplecta simplecta;
	private FeedManager feedManager;
	private ProgressDialog dialog;
	private String feedUrl= "";
	
	
	 @Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    feedUrl = (String) getIntent().getSerializableExtra("URL");
	    feedManager = FeedManager.getInstance();
		simplecta = Simplecta.getInstance();
		updateFeed();
		Log.d("shit", "settng addepter");
		feedManager.feedArticleAdapter = new FeedArticleAdapter(this,R.layout.row, feedManager );
	    setListAdapter(feedManager.feedArticleAdapter);
		
	  }
	 
	 public void showDialog(){
		this.dialog = new ProgressDialog( this );
		this.dialog.setTitle("Downloading Feeds...");
		this.dialog.setMessage("Please wait.");
		this.dialog.setCancelable(false);
		this.dialog.setIndeterminate(true);
		this.dialog.show();
	 }
	 
	 public void dismissDialog(){
		 if (this.dialog.isShowing()) {
			 this.dialog.dismiss();
		 }
		 feedManager.feedArticleAdapter.notifyDataSetChanged();
	 }
	 
	 public void updateFeed(){
		AbstractUpdateFeedTask task = new AbstractUpdateFeedTask();
		task.execute();
	 }
	 
	 
	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	  }
		
	  public  class AbstractUpdateFeedTask extends AsyncTask<Void, Void, Void>{
		  	private static final String TAG = "CC AbstractUpdateFeedTask";
	
			@Override
			protected void onPreExecute() {
				showDialog();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
			
				
				//InputStream is = simplecta.getHTMLStream();
				String strArticlesHTML = null;
				try {
					// Get the HTML String from the Simplecta connection
					strArticlesHTML = simplecta.getArticleByFeed(feedUrl);
					feedManager.feedArticles = feedManager.getAtricleList( strArticlesHTML );
					//_feedManager.updateFeeds( _simplecta.getAllURL() );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e( TAG, e.getMessage() );
				}
				return null;
			
			}
			
			@Override
			public void onPostExecute(Void result) {
				dismissDialog();
				Log.d("shit", "updated feeds");
			}
	}
}


