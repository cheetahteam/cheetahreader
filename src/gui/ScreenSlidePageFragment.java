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

import android.app.Fragment;

import android.content.Context;
import android.content.Intent;


import android.os.Bundle;
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

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class ScreenSlidePageFragment extends Fragment {
	// modified spot
	 TextView view ;
	 TextView view1 ;
	 EditText te;
	 ListView listView,listView1;
	 RelativeLayout pg1,pg2;
	 Context con;
	 
	 FeedManager _feedManager;
	 ArrayList<Article> _articles;
	 
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
    
     private String textContent="";
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
       
        _feedManager = FeedManager.getInstance();
    
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
    
    listView = (ListView) rootView.findViewById(R.id.listV);
    listView1 = (ListView) rootView.findViewById(R.id.listV1);
    
    AdapterArticle adapter = new AdapterArticle(this.getActivity(),R.layout.row, _feedManager );
    
    
   listView.setAdapter(adapter );
   listView1.setAdapter(adapter);
        
        
   listView.setOnItemClickListener(new OnItemClickListener() {
	   
		private Context context = ApplicationContextProvider.getContext();

		//context = this.context;
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				    // getting article
				//	String product = ((TextView) view).getText().toString();
		         Article article = new Article();
				// article =  (Article) parent.getAdapter().getItem(position);
			     article = (Article) listView.getItemAtPosition(position);
			    String url = article.getFeedLink();
			    Intent intent = new Intent(this.context, WebViewActivity.class);
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
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                getString(R.string.title_template_step, mPageNumber + 1,"News Feeds:RSS/ATOM"));
       //text
      //  view.setVisibility(View.INVISIBLE);
       // view1.setVisibility(View.INVISIBLE);
        
        // page layout
        pg2.setVisibility(View.INVISIBLE);
        pg1.setVisibility(View.VISIBLE);
        
        
        }else
        {
        	((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.title_template_step, mPageNumber + 1,"Feed Management"));
        	//Text
        	//view.setVisibility(View.INVISIBLE);
        	//view1.setVisibility(View.INVISIBLE);
        	
        	// page layout
        	
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
}
