package com.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import android.app.Activity;

public class Simplecta {
	
	static final String BASE_URL = "website.com";
	static final int CONNECTION_RETRY = 5;
	
	Activity context;
	ArrayList<Cookie> cookies = new ArrayList<Cookie>();
	boolean isLogedin = false;
	boolean connectionError = true;
	
	
	Simplecta(Activity context, String accessToken){
		this.context = context;
		int loginAttempt = 0;
		while(loginAttempt < CONNECTION_RETRY){
			if(login(accessToken))
				connectionError = false;
		}
	}
	

	//the access token from google server being passed for login cookie
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
			
			isLogedin = true;
			return false;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		isLogedin = false;
		return false;
	
	}
	
	//prepared a connection by setting the cookies and returns a URLConnection that is ready to connect
	private URLConnection prepConnection(String subURL) throws Exception{
		URL myUrl = new URL(BASE_URL+subURL);
		URLConnection urlCon = myUrl.openConnection();
		
		String cookieString = "";
		for(Cookie cookie: this.cookies){
			cookieString = cookieString + cookie.getCookie();
		}
		
		//setting the cookies
		urlCon.setRequestProperty("Cookie", cookieString);
		
		//returns the prepared connection ready to be connected
		return urlCon;
	}
	
	
	//home page loads all feed
	ArrayList<String> showAll(){
		try {
			URLConnection urlCon = prepConnection("/showAll/");
			urlCon.connect();//sends in the cookies
			
			//reader to read in html code line by line
			BufferedReader reader  = new BufferedReader(
					new InputStreamReader(urlCon.getInputStream()));
			
			//holds the list of articles
			ArrayList<ArticleData> articles = new ArrayList<ArticleData>();
			
			String line = null;
			String block = "";
			while ((line = reader.readLine()) != null) {
				//save the feed
	            System.out.println(line);
	            if(line.contains("")){
	            	if(!block.isEmpty()){
	            		articles.add(new ArticleData(block));
	            		block = "";
	            	}
	            	block = block+line;
	            }
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	//add rss subscription
	void addRSS(){
		
	}
	
	
	//add atom subscription
	void addAtom(){
		
	}
	
	
	//unsubscribe from feed
	void unsubscribe(){
		
	}
	
	
	//mark as read
	void mark(){
		
	}
	
	
	//mark as unread
	void unMark(){
		
	}
	
	
	//see all the subscriptions
	void feeds(){
		
	}
	
	//update to check for feed
	String update(){
		return "";
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
