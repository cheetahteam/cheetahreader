package gui;

 
import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import com.example.tokentest.R;
 


public class MainActivity extends Activity {
	
	
	
	private class Sample {
        private CharSequence title;
        private Class<? extends Activity> activityClass;

        public Sample(int titleResId, Class<? extends Activity> activityClass) {
            this.activityClass = activityClass;
            this.title = getResources().getString(titleResId);
        }

        @Override
        public String toString() {
            return title.toString();
        }
    }
	
	private static Sample[] mSamples;
	
	
	
	
	

   Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);
		
		 mSamples = new Sample[]{
	               
	                new Sample(R.string.title_screen_slide, MainFragment.class),
	               
	        };
		
		addListenerOnButton();
	}

	



public void addListenerOnButton() {
 
		button = (Button) findViewById(R.id.button1);
 
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
				 Intent i = new Intent(MainActivity.this, MainFragment.class);
			        startActivity(i);
 
			}
 
		});
 
	}


	 }
