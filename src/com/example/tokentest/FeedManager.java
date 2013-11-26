package com.example.tokentest;

import gui.AdapterArticle;
import gui.AdapterFeed;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;


public class FeedManager {
	
	private static String TAG = "CC FeedManager";
	private ArrayList<Article> _alArticles;
	private ArrayList<Feed> _feeds;
	private final static int NOT_FOUND = 	-1;
	private static FeedManager instance = 	null;
	public boolean isUpdated = false;
	
	public ArrayList<Article> feedArticles;
	public AdapterFeed feedAdapter;
	public AdapterArticle articleAdapter;
	public AdapterArticle feedArticleAdapter;
	
	
	private FeedManager() {
		_alArticles = new ArrayList<Article>();
		_feeds = new ArrayList<Feed>();

	}
	
	public static FeedManager getInstance() {

		if( instance == null ) {
	        instance = new FeedManager();
	        
	      }
	    return instance;
	}
	
	public ArrayList<Article>  getArticles() {
		return _alArticles;
	}
	public ArrayList<Feed>  getFeeds() {
		return _feeds;
	}
	
	public Feed  getFeed( int index) {
		return _feeds.get(index);
	}

	public Feed getFeed( String strFeedKey ) {
		int index = findFeed( strFeedKey );
		if ( index < 0 || index > _feeds.size() )
			throw new ArrayIndexOutOfBoundsException( "Feed not found:" + strFeedKey );
		else
			return _feeds.get( index );
	}
	public Article getArticle( String strArticleKey ) {
		int index = findArticle( strArticleKey );
		if ( index < 0 || index > _alArticles.size() )
			throw new ArrayIndexOutOfBoundsException( "Article not found:" + strArticleKey );
		else
			return _alArticles.get( index );
	}
	
	public Article getArticle( int nIndex ) {
		if ( nIndex < 0 || nIndex > _alArticles.size() ) {
			throw new ArrayIndexOutOfBoundsException( "The article index is out of bounds:" + nIndex );
		}
		Article article = _alArticles.get( nIndex );
		return article;
	}
	public boolean setArticles( String strHTML ) throws Exception {
		
		Document doc = null;
		if ( strHTML == null ) {
			Log.e( TAG, "The HTML string is null. cannot parse" );
			return false;
		}
		if ( strHTML == "" ) {
			Log.e( TAG, "The HTML string is empty. cannot parse" );
			return false;
		}

		try {
			 doc = getDocument( strHTML );
			 _alArticles = extractArticles( doc );
		}
		catch( Exception ex ) {
			Log.e(TAG, ex.getMessage());
			return false;
		}
    	isUpdated = true;
    	return true;
	}
	
public boolean setFeeds( String strHTML ) throws Exception {
		
		Document doc = null;
		if ( strHTML == null ) {
			Log.e( TAG, "The HTML string is null. cannot parse" );
			return false;
		}
		if ( strHTML == "" ) {
			Log.e( TAG, "The HTML string is empty. cannot parse" );
			return false;
		}

		try {
			 doc = getDocument( strHTML );
			 _feeds = extractFeeds( doc );
		}
		catch( Exception ex ) {
			Log.e(TAG, ex.getMessage());
			return false;
		}
    	isUpdated = true;
    	return true;
	}
	
	public boolean setArticleFeeds( String strHTML ) throws Exception {
		
		Document doc = null;
		if ( strHTML == null ) {
			Log.e( TAG, "The HTML string is null. cannot parse" );
			return false;
		}
		if ( strHTML == "" ) {
			Log.e( TAG, "The HTML string is empty. cannot parse" );
			return false;
		}

		try {
			 doc = getDocument( strHTML );
			 this.feedArticles = extractArticles( doc );
		}
		catch( Exception ex ) {
			Log.e(TAG, ex.getMessage());
			return false;
		}
    	isUpdated = true;
    	return true;
	}
	
public ArrayList<Article> getAtricleList( String strHTML ) throws Exception {
		
		Document doc = null;
		if ( strHTML == null ) {
			Log.e( TAG, "The HTML string is null. cannot parse" );
			return null;
		}
		if ( strHTML == "" ) {
			Log.e( TAG, "The HTML string is empty. cannot parse" );
			return null;
		}

		try {
			 doc = getDocument( strHTML );
			 return extractArticles( doc );
		}
		catch( Exception ex ) {
			Log.e(TAG, ex.getMessage());
			return null;
		}
	}

	/*
	 * Temporary testing function
	 */
	private InputStream getHTMLInputStream( String strURL ) {
		InputStream is = null;
		HttpURLConnection con;

        try {
            con = (HttpURLConnection) new URL( strURL ).openConnection();
            is = con.getInputStream();
        }
        catch( Exception ex ) {
        	Log.e( TAG, "Error getting an input stream from the HTML URL");
        }
		return is;
	}

	/*
	 * Uses The JSoup parse to convert an HTML inputStream into a JSoup Document node
	 */
	private Document getDocument( String strHTML ) throws Exception {
		
		//inputStream = getHTMLInputStream( "http://127.0.0.1:8888/index_2.html" );
		Document doc = null;
		if ( strHTML == null ) {
			String err = "The HTML string is null and will be unable to parse.";
			Log.e( TAG, err );
			throw new NullPointerException( err );			
		}	
		
		try {
			doc = Jsoup.parse( strHTML );
			
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Unable to parse html using JSoup:" + strHTML );
		}
		return doc;
	}
	
	
	/*
	 * Takes the substring of a href start from http:
	 
	private String TrimLink( String strLink ) { 
		
		int start = strLink.indexOf("h");
		String strURL = strLink.substring(start, strLink.length() );

		String strNewLink = null;
		try {
			strNewLink = java.net.URLDecoder.decode( strURL, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			Log.e( TAG, e.getMessage() );
		}
		return strNewLink;
		
	}
	*/
	
	private String trimHref( String strHref, int nStart, int nEnd ) {
		
		String key = strHref.substring( nStart, nEnd );
		return key;
		
	}
	
	/*
	 * Getting data key from parser for consistency instead
	 */
	private String getFeedKeyFromHref( String strHref ) {
		
		int start = strHref.indexOf("h");
		String key = trimHref( strHref, start, strHref.length() );
		return key;
		
	}
	
	private String getArticleKeyFromHref( String strHref ) {
		
		int start = strHref.indexOf("=") + 1;
		String key = trimHref( strHref, start, strHref.length() );
		return key;
		
	}
	
	private ArrayList<Article> extractArticles( Document doc ) {

		if ( doc == null )
		{
			throw new NullPointerException("Document is null. Unable to Parse html.");
		}
		
		/***** TODO: change this from a iterator to a loop to increase speed *****/
		
		// The data starts after the <body> of the html
		Element content = doc.body();
		Elements items = content.getElementsByClass("item");
		ArrayList<Article> temArticles = new ArrayList<Article>();
		// Parse and Add every article item to _articles
		for (Element item : items) {
			
		  // Fill the Feed attr
		  Elements feedLink = item.getElementsByClass("feedlink");
		  String feedTitle =  feedLink.text();
		  String feedLinkHref = feedLink.attr("href");

		  // Article attr
		  Element item_link = item.getElementsByClass("item_links").first();
		  Element readLink = item_link.getElementsByClass("read_link").first();
		  String readLinkHref = readLink.attr("href");
		  String articleKey = getArticleKeyFromHref( readLinkHref );
		  String articleTitle = readLink.text();
		  Element peekLink = item_link.getElementsByClass("peek").first();
		  String peekLinkHref = peekLink.attr("href");
		  Element dataKey = item_link.getElementsByClass("ajax_link").first();
		  String strDataKey = dataKey.attr("data-key");
		  
		  // TODO Validate article
		  
		  Article article = new Article( feedTitle, feedLinkHref, articleKey, readLinkHref, articleTitle, peekLinkHref, strDataKey );
		  temArticles.add( article );
		  
		}
		
		return temArticles;
		
	}
	
	private ArrayList<Feed> extractFeeds( Document doc ) {

		if ( doc == null )
		{
			throw new NullPointerException("Document is null. Unable to Parse html.");
		}
		
		/***** TODO: change this from a iterator to a loop to increase speed *****/
		
		// The data starts after the <body> of the html
		Element content = doc.body();
		Elements feeds = content.getElementsByClass("largefeedlink");
		ArrayList<Feed> temFeeds = new ArrayList<Feed>();
		// Parse and Add every article item to _articles
		for (Element feed : feeds) {
			
		  // Fill the Feed attr
		  String feedTitle =  feed.text();
		  String feedLinkHref = feed.attr("href");
		  Element unsubLink = feed.siblingElements().first();
		  String feedUnsubLink = unsubLink.attr("href");
		  
		  // TODO Validate feed
		  Feed newFeed = new Feed( feedTitle, feedLinkHref, feedUnsubLink );
		  temFeeds.add( newFeed );
		  
		}
		
		return temFeeds;
		
	}
	
	public int findArticle( Article article ) {
		
		return _alArticles.indexOf( article );
	}
	public int findArticle( String strArticleKey ) {
		Article article = new Article();
		article.setKey( strArticleKey );
		return _alArticles.indexOf( article );
	}
	public int findFeed( Feed feed ) {
		
		return _feeds.indexOf( feed );
	}
	public int findFeed( String strFeedLink ) {
		Feed feed = new Feed();
		feed.setFeedLink( strFeedLink );
		return _feeds.indexOf( feed );
	}
	
	/*private void fillFeeds() {
		
		for( int i = 0; i < _alArticles.size(); ++i ) {
			
			// Get the feed it belongs to
			Article currArticle = _alArticles.get( i );
			String strFeedName = currArticle.getFeedName();
			
			// Create a feed 
			Feed feed = new Feed();
			feed.setFeedName( strFeedName );
			feed.setFeedLink( currArticle.getFeedLink() );
			feed.setFeedKey( currArticle.getDataKey() );
			
			// Now check if the feed exists, if yes, add the article to the existing feed, if not, add the (new feed and newarticle)
			int feedIndex = findFeed( feed );
			if ( feedIndex == NOT_FOUND ) {
				feed.AddArticle( currArticle );
				_feeds.add( feed );
			}
			else {
				feed = _feeds.get( feedIndex );
				feed.AddArticle( currArticle );
				_feeds.set( feedIndex, feed );
			}
			
		}
		
	}
	*/

}
