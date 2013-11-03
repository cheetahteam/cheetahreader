package com.example.tokentest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.auth.AuthenticatedAppEngineContext;

import android.app.Activity;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class Simplecta {
	
	static final String BASE_URL = "https://simplecta.appspot.com";
	static final int CONNECTION_RETRY = 5;
	private static final String TAG = "CC Simplecta";
	
	Context context;
	//String cookies = null;
	boolean isLogedin = false;
	boolean connectionError = true;
	private static Simplecta instance = null;
	//private String _cookie = null;
	DefaultHttpClient http_client;
	List<org.apache.http.cookie.Cookie> cookies;
	HttpContext httpContext;
	AndroidHttpClient client;
	
	private Simplecta() {

	}
	
	public static Simplecta getInstance() {

		if( instance == null ) {
	        instance = new Simplecta();
	        
	      }
	    return instance;
	}
	
	public void init( Context context, String accessToken ){
		
		// allocate
		this.context = context;
		http_client = new DefaultHttpClient();
		
		int loginAttempt = 0;
		while(loginAttempt < CONNECTION_RETRY){
			if(login(context, accessToken)) {
				connectionError = false;
				break;
			}
			loginAttempt++;
		}
	}
	

	/*
	 * the access token from google server being passed for login cookie
	 */
	private boolean login(Context context, String accessToken){
		try {
			/*
			 * the url connects with a https connection that takes in two get parameters 
			 * "auth" for the access token and
			 * "continue" for the redirect url
			 */
			client = AndroidHttpClient.newInstance("IntegrationTestAgent", context );
			httpContext = AuthenticatedAppEngineContext.newInstance( context, BASE_URL, accessToken);
			    }
		catch(Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return true;
			    
	}
	
	public String getAllURL() {
		//return 
		return null;
	}
	/*
	 * prepared a connection by setting the cookies and returns a URLConnection that is ready to connect
	 */
	private HttpResponse prepConnection(String subURL) throws Exception{
		HttpResponse response = null;
		String strUrl = BASE_URL+subURL;
		try {
			HttpGet get = new HttpGet( strUrl );
			// the cookie is stored in the httpcontext and ready to execute
			response = client.execute( get, httpContext );
		}
		catch( Exception e ) {
			Log.e(TAG, e.getMessage());
		}
        
		return response;
	}
	
	
	/*
	 * home page loads all feed
	 */
	public String showAll(){
		HttpResponse result = null;
		StringBuilder whole = new StringBuilder();
		try {
			result = prepConnection("/showAll/");

            BufferedReader in = new BufferedReader(new InputStreamReader(result.getEntity().getContent()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                whole.append(inputLine);
                Log.d(TAG, inputLine);
            }
            in.close();
        } 
		catch (Exception e) {
            Log.e(TAG, e.getMessage());
		}
		
		return whole.toString();
	}
	
	
	/*
	 * add rss subscription
	 */
	public boolean addRSS(String rssUrl){
		try {
			HttpResponse result = prepConnection("/addRSS/?url="+rssUrl);
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * add atom subscription
	 */
	public boolean addAtom(String atomUrl){
		try {
			HttpURLConnection connection = (HttpURLConnection) prepConnection("/addAtom/?url="+atomUrl);
			
			//if connection returned some kind of error
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				return false;
			}
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * unsubscribe from feed
	 */
	public boolean unsubscribe(String feedUrl){
		try {
			HttpURLConnection connection = (HttpURLConnection) prepConnection(feedUrl);
			
			//if connection returned some kind of error
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				return false;
			}
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * mark as unread
	 */
	public boolean markUnread(String key){
		 try {
			HttpURLConnection connection = (HttpURLConnection) prepConnection("/markUnread/?"+key);
			
			//if connection returned some kind of error
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				return false;
			}
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * mark as read
	 */
	public boolean markRead(String key){
		 try {
			HttpURLConnection connection = (HttpURLConnection) prepConnection("/markRead/?"+key);
			
			//if connection returned some kind of error
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				return false;
			}
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * see all the subscriptions
	 
	ArrayList<Feed> feeds(){
		try {
			HttpURLConnection connection = prepConnection("/feed/");
			
			//if connection returned some kind of error
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				return null;
			}
			
			//reader to read in html code line by line
			BufferedReader reader  = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			
			//holds the list of feed
			ArrayList<Feed> feed = new ArrayList<Feed>();
			
			String line = null;
			String block = "";
			while ((line = reader.readLine()) != null) {
				//save the feed
	            System.out.println(line);
	            if(line.contains("item")){
	            	//i//f(!block.isEmpty()){
	            		//feed.add(new Feed(block));
	            		//block = "";
	            	//}
	            	block = block+line;
	            }
	        }
			return feed;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/

}

	
/*
 * stores cookies
 */
class Cookie{
	String name = null;
	String value = null;
	
	Cookie(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	//Spiting up a cookie string "name=value"
	Cookie(String cookieString){
        this.name = cookieString.substring(0, cookieString.indexOf("="));
        this.value = cookieString.substring(cookieString.indexOf("=") + 1, cookieString.length());
	}
	
	//returns the cookie string
	String getCookie(){
		return name+"="+value+"; ";
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
}

/*
 * holds the article info that is parsed for the html
 */
class ArticleData{
	
	private String name;
	private String url;
	
	ArticleData(String articleName, String articleURL){
		this.setName(articleName);
		this.setUrl(articleURL);
	}
	
	ArticleData(String htmlBlock){
		String articleName = null,
				articleURL = null;
		
		//do the parsing
		
		
		
		this.setName(articleName);
		this.setUrl(articleURL);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
