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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
//modified
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auth.AuthActivity;
import com.example.tokentest.ActionManager;
import com.example.tokentest.Article;
import com.example.tokentest.Feed;
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
public class ScreenSlidePageFragment extends Fragment implements OnClickListener {
	// modified spot
	TextView view ;
	TextView view1 ;
	EditText te;
	ListView listView,listView1;
	RelativeLayout pg1,pg2;
	
	FeedManager _feedManager;
	ActionManager _actionManager;
	ArrayList<Article> _articles;
	 
	
	private Activity _activity;
	
	
	// change 1
		Button logout,  logout1;
		// change 2
		EditText inputSearch;
	
	//does the screen update
	private boolean updateFlag = false;
	private Handler handeler = new Handler();
	private Runnable dataUpdateChecker = new Runnable(){
	
		@Override
		public void run() {
			if(_actionManager.dataUpdated == true){
				Log.d("shit", "data update checker");
				
				_actionManager.dataUpdated = false;
				_feedManager.articleAdapter.notifyDataSetChanged();
				_feedManager.feedAdapter.notifyDataSetChanged();
				
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
	    _actionManager = ActionManager.getInstance();
	    if(mPageNumber==0){
	    	_feedManager.articleAdapter = new AdapterArticle(this.getActivity(),R.layout.row, _feedManager );
	    } else if(mPageNumber==1){
	    	_feedManager.feedAdapter = new AdapterFeed(this.getActivity(), R.layout.row, _feedManager);
	    }
		_actionManager.updateNow();
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
		listView = (ListView) rootView.findViewById(R.id.listV);
		listView1 = (ListView) rootView.findViewById(R.id.listV1);
		//articles
		listView.setAdapter(_feedManager.articleAdapter );
		//feeds 
		//add this back when parsing feed works
		//listView1.setAdapter(_feedManager.articleAdapter);
		// change 2
		inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);
		

		logout = (Button) rootView.findViewById(R.id.button1);
		logout.setOnClickListener(this);
		
		logout1 = (Button) rootView.findViewById(R.id.button3);
		logout1.setOnClickListener(this);
        
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
		
		listView1.setOnItemClickListener(new OnItemClickListener() {
			   
			private Context context = ApplicationContextProvider.getContext();
	
			//context = this.context;
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// getting article
				//	String product = ((TextView) view).getText().toString();
				Feed feed = new Feed();
				// article =  (Article) parent.getAdapter().getItem(position);
				feed = _feedManager.getFeed((int)id);
				String feedUrl = feed.getFeedLink();
				
				Intent intent = new Intent(getActivity(),FeedArticleActivity.class);
				intent.putExtra("feedUrl", feedUrl);
				
				startActivity(intent);
			}
		});
		
		// change2 
		inputSearch.addTextChangedListener(new TextWatcher() {
		     
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changed the Text
		    	_feedManager.articleAdapter.getFilter().filter(cs);   
		    }
		     
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) {
		        // TODO Auto-generated method stub
		         
		    }
		     
		    @Override
		    public void afterTextChanged(Editable arg0) {
		        // TODO Auto-generated method stub                          
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

	@Override
	public void onClick(View v) {
		
		if(v==logout)
		{
			Intent i = new Intent(this.getActivity(), AuthActivity.class);
			this.getActivity().startActivity(i);
			
		}
		else if(v==logout1)
		{
			Intent i = new Intent(this.getActivity(), AuthActivity.class);
			this.getActivity().startActivity(i);
			
		}	
		
		
	}
}
