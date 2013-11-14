package gui;
import com.example.tokentest.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
 
public class WebViewActivity extends Activity {
 
	private WebView webView;
 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		// Getting the URL from the Onclick Article Class
		String url = (String) getIntent().getSerializableExtra("Artic");
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		// loading the URL and display the webpage
		webView.loadUrl(url);
 
	}
 
}