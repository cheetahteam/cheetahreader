package gui;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tokentest.Feed;
import com.example.tokentest.FeedManager;
import com.example.tokentest.R;

public class AdapterFeed extends ArrayAdapter<Feed> {
    private Activity activity;
    private FeedManager _feedManager = FeedManager.getInstance();
    private static LayoutInflater inflater = null;
    private final int rowResourceId;

    public AdapterFeed (Activity activity, int textViewResourceId, FeedManager fm ) {
    	//_feedManager = FeedManager.getInstance();
    	
        super(activity, textViewResourceId, fm.getFeeds());
        _feedManager = fm;
        rowResourceId = textViewResourceId;
        
        try {
            this.activity = activity;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return _feedManager.getFeeds().size();
    }
/*
    public Article getItem(Article position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_number;             

    }
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	View rowView = inflater.inflate(rowResourceId, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        //final ImageView markreadButton = (ImageView) rowView.findViewById(R.id.markread);
		

        final Feed feed = _feedManager.getFeed( position );
        textView.setText( feed.getFeedName() );
        //markreadButton.setBackgroundResource(R.drawable.listviewbuttons);
        
        rowView.setTag( position );
        
        textView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String url = feed.getFeedLink();
				Intent intent = new Intent(  activity, FeedArticleActivity.class);
				intent.putExtra("URL", url);
				
				v.getContext().startActivity(intent);
			}
        	
        });
		
		/*
		  markreadButton.setOnClickListener(new OnClickListener(){


			@Override
			public void onClick(View v) {
				
				if( feed.toggleMarkSubscribe() ){
					//markreadButton.setBackgroundResource(R.drawable.listviewbuttons2);
				} else {
					//markreadButton.setBackgroundResource(R.drawable.listviewbuttons);
				}
			}
        	
        });
        */
        return rowView;
    }
}