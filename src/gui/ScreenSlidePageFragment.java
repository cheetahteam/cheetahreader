/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gui;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//modified
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tokentest.Article;
import com.example.tokentest.FeedManager;
import com.example.tokentest.R;
import com.example.tokentest.Simplecta;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class ScreenSlidePageFragment extends Fragment {
	// modified spot
	Simplecta _simplecta;
	TextView view ;
	TextView view1 ;
	EditText te;
	ListView listView,listView1;
	RelativeLayout pg1,pg2;
	
	AdapterArticle adapter;
	
	FeedManager _feedManager;
	ArrayList<Article> _articles;
	 
	
	private Activity _activity;
	
	//does the screen update
	private boolean updateFlag = false;
	private Handler handeler = new Handler();
	private Runnable dataUpdateChecker = new Runnable(){
	
		@Override
		public void run() {
			if( _feedManager.isUpdated == true){
				_feedManager.isUpdated = false;
				adapter.notifyDataSetChanged();
				
			}
			
			//updates every second
			handeler.postDelayed(this, 1000);
		}
		
	};
	 
	// web view
	private WebView _webView;
		 
	 
	/**
	 * The argument key for the page number this fragment represents.
	 */
	public static final String ARG_PAGE = "page";
	
	/**
	 * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
	 */
	private int mPageNumber;
	
	
	
	/**
	 * Factory method for this fragment class. Constructs a new fragment for the given page number.
	 */
	 
	 public static ScreenSlidePageFragment create(int pageNumber) {
		 ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
		 Bundle args = new Bundle();
		 args.putInt(ARG_PAGE, pageNumber);
		 fragment.setArguments(args);
		 return fragment;
	 }
	
	public ScreenSlidePageFragment() {
		_simplecta = Simplecta.getInstance();
		updateFeeds();
		handeler.postDelayed(dataUpdateChecker, 0);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _activity = activity;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mPageNumber = getArguments().getInt(ARG_PAGE);
	   
	    _feedManager = FeedManager.getInstance();
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
		listView = (ListView) rootView.findViewById(R.id.listV);
		listView1 = (ListView) rootView.findViewById(R.id.listV1);
		adapter = new AdapterArticle(this.getActivity(),R.layout.row, _feedManager );
		
		listView.setAdapter(adapter );
		listView1.setAdapter(adapter);
        
		listView.setOnItemClickListener(new OnItemClickListener() {
	   
			private Context context = ApplicationContextProvider.getContext();
	
			//context = this.context;
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// getting article
				//	String product = ((TextView) view).getText().toString();
				Article article = new Article();
				// article =  (Article) parent.getAdapter().getItem(position);
				Log.d("new shit", "id:"+id);
				Log.d("new shit", "position:"+position);
				article = _feedManager.getArticle((int)id);
				
				String url = article.getPeekLink();
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				intent.putExtra("URL", url);
				
				startActivity(intent);
			}
		});
	
	        
	  // view = (TextView) rootView.findViewById(R.id.textshow);
	  // view1 = (TextView) rootView.findViewById(R.id.textshow1);
	     
		pg1 = (RelativeLayout) rootView.findViewById(R.id.page1);
		pg2 = (RelativeLayout) rootView.findViewById(R.id.page2);
		   
		te = (EditText) rootView.findViewById(R.id.urlName);
		    
		if(mPageNumber==0)
		{
			((TextView) rootView.findViewById(android.R.id.text1)).setText(getString(R.string.title_template_step, mPageNumber + 1,"News Feeds:RSS/ATOM"));
		
			// page layout
			pg2.setVisibility(View.INVISIBLE);
			pg1.setVisibility(View.VISIBLE);
			
			
		}else {
			((TextView) rootView.findViewById(android.R.id.text1)).setText(
					getString(R.string.title_template_step, mPageNumber + 1,"Feed Management"));
			pg1.setVisibility(View.GONE);
			pg2.setVisibility(View.VISIBLE);	
	    }

		return rootView;
	}
	
	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
	    return mPageNumber;
	}
	
	public void updateFeeds() {
		AbstractUpdateFeedTask task = new AbstractUpdateFeedTask();
		task.execute();
	}
	
	public  class AbstractUpdateFeedTask extends AsyncTask<Void, Void, Void>{
	    private static final String TAG = "CC AbstractUpdateFeedTask";
	
		/** progress dialog to show user that the update is processing. */
		ProgressDialog dialog;
		Activity activity;
		
		
		public AbstractUpdateFeedTask(){//(Activity activity) {
		 this.activity = getActivity();
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
        }
	}
}
