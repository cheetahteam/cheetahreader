package com.example.tokentest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;

public class Simplecta {
	
	static final String BASE_URL = "www.simplecta.com";
	static final int CONNECTION_RETRY = 5;
	
	Context context;
	String cookies = null;
	boolean isLogedin = false;
	boolean connectionError = true;
	private static Simplecta instance = null;
	
	private Simplecta() {

	}
	
	public static Simplecta getInstance() {

		if( instance == null ) {
	        instance = new Simplecta();
	        
	      }
	    return instance;
	}
	
	public void init( Context context, String accessToken ){
		this.context = context;
		int loginAttempt = 0;
		while(loginAttempt < CONNECTION_RETRY){
			if(login(accessToken))
				connectionError = false;
		}
	}
	
	public InputStream getHTMLStream() {
		return null;
	}
	/*
	 * the access token from google server being passed for login cookie
	 */
	private boolean login(String accessToken){
		try {
			/*
			 * the url connects with a https connection that takes in two get parameters 
			 * "auth" for the access token and
			 * "continue" for the redirect url
			 */
			URL myUrl = new URL("https://"+BASE_URL+"/_ah/login?continue=http://"+BASE_URL+"/&auth=" + accessToken);
			HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
			connection.connect();
			
			//if connection returned some kind of error
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				return false;
			}
			
			cookies = connection.getHeaderField("Set-Cookie");
			/*
			//splits the cookies by their separator ';'
	 		String[] cookies = connection.getHeaderField("Set-Cookie").split(";");
	 		
	 		//saves the cookies in the cookies arraylist
	 		for( String cookie : cookies ){
	 			this.cookies.add(new Cookie(cookie));
	 		}
	 		
	 		//no cookies were set login failed
	 		if(this.cookies.size() == 0){
	 			return false;
	 		}
	 		*/
			
			isLogedin = true;
			return false;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		isLogedin = false;
		return false;
	
	}
	
	/*
	 * prepared a connection by setting the cookies and returns a URLConnection that is ready to connect
	 */
	private HttpURLConnection prepConnection(String subURL) throws Exception{
		URL myUrl = new URL(BASE_URL+subURL);
		HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
		/*
		String cookieString = "";
		for(Cookie cookie: this.cookies){
			cookieString = cookieString + cookie.getCookie();
		}*/
		
		//setting the cookies
		connection.setRequestProperty("Cookie", cookies);
		
		connection.connect();//sends in the cookies
		cookies = connection.getHeaderField("Set-Cookie");
		//returns the prepared connection ready to be connected
		return connection;
	}
	
	
	/*
	 * home page loads all feed
	 */
	public ArrayList<ArticleData> showAll(){
		try {
			HttpURLConnection connection = prepConnection("/showAll/");
			
			//if connection returned some kind of error
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				return null;
			}
			
			//reader to read in html code line by line
			BufferedReader reader  = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			
			//holds the list of articles
			ArrayList<ArticleData> articles = new ArrayList<ArticleData>();
			
			String line = null;
			String block = "";
			while ((line = reader.readLine()) != null) {
				//save the feed
	            System.out.println(line);
	            if(line.contains("item")){
	            	//if(!block.isEmpty()){
	            		//articles.add(new ArticleData(block));
	            		//block = "";
	            	//}
	            	block = block+line;
	            }
	        }
			
			return articles;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/*
	 * add rss subscription
	 */
	public boolean addRSS(String rssUrl){
		try {
			HttpURLConnection connection =  prepConnection("/addRSS/?url="+rssUrl);
			
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
	 */
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
