package gui;
import com.auth.AuthActivity;
import com.example.tokentest.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
 
public class WebViewActivity extends Activity  implements OnClickListener{
 
	private WebView webView;
	Button contentfeed, feedmanage;
 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		// Getting the URL from the Onclick Article Class
		String url = (String) getIntent().getSerializableExtra("URL");
		Log.d("web view shit", "url:"+url);
		
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		// loading the URL and display the webpage
		webView.loadUrl(url);
		
		contentfeed = (Button) findViewById(R.id.line1);
		feedmanage = (Button) findViewById(R.id.line2);
		contentfeed.setOnClickListener(this);
		feedmanage.setOnClickListener(this);
 
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v == contentfeed)
		{
			
			Intent i = new Intent(this, MainFragment.class);
			this.startActivity(i);
			 
		}
		else if (v== feedmanage)
		{
			Intent i = new Intent(this, AuthActivity.class);
			this.startActivity(i);
			
		}
		
	}
 
}