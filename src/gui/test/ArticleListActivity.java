package gui.test;

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

public class ArticleListActivity extends ListActivity {
	
	private ArticleListActivity activity = this;
	private Simplecta simplecta;
	private FeedManager feedManager;
	private ProgressDialog dialog;
	private ArticleAdapter articleAdapter = null;
	
	private Handler handeler = new Handler();
	private Runnable dataUpdateChecker = new Runnable(){

		@Override
		public void run() {
			if( articleAdapter.getCount() == 100){
				articleAdapter.notifyDataSetChanged();
			}
			
			Log.d("shit","article count:"+articleAdapter.getCount());
			handeler.postDelayed(this, 500);
		}
		
	};
	
	
	 @Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    
	    feedManager = FeedManager.getInstance();
		simplecta = Simplecta.getInstance();
		updateFeed();
		handeler.postDelayed(dataUpdateChecker, 0);
		//SystemClock.sleep(10000);
		Log.d("shit", "settng addepter");
		articleAdapter = new ArticleAdapter(this,R.layout.list_row_item, feedManager );
	    setListAdapter(articleAdapter);
		
	    
	    /*
	    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
	        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
	        "Linux", "OS/2" };
	    // use your own layout
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        R.layout.list_row_item, R.id.label, values);*/
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
					strArticlesHTML = simplecta.showAll();
					
					// Parse the html into the article/feed objects
					feedManager.updateFeeds( strArticlesHTML );
					//_feedManager.updateFeeds( _simplecta.getAllURL() );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e( TAG, e.getMessage() );
				}
				return null;
			
			}
			
			@Override
			public void onPostExecute(Void result) {
				dismissDialog();/*
				runOnUiThread(new Runnable() {
					public void run() {
						articleAdapter.notifyDataSetChanged();
					}
				});*/
				Log.d("shit", "updated feeds");
			}
	}
}

class ArticleAdapter extends ArrayAdapter<Article> {
    private Activity activity;
    private FeedManager _feedManager = FeedManager.getInstance();
    private static LayoutInflater inflater = null;
    private final int rowResourceId;

    public ArticleAdapter (Activity activity, int textViewResourceId, FeedManager fm ) {
    	//_feedManager = FeedManager.getInstance();
    	
        super(activity, textViewResourceId, fm.getArticles() );
        _feedManager = fm;
        rowResourceId = textViewResourceId;
        
        Log.d("shit", "feed size"+getCount());
        
        try {
            this.activity = activity;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return _feedManager.getArticles().size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(rowResourceId, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);

        //String imageFile = Model.GetbyId(id).IconFile;

        Article article = _feedManager.getArticle( position );
        
        textView.setText( article.getTitle() );
        
        rowView.setTag( position );
        
        return rowView;

    }
}


