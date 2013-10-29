package com.example.tokentest;

import java.util.ArrayList;

public class Feed {
	
	private String 				_strFeedName;
	private String 				_strFeedLink;
	private String 				_strFeedKey;
	private ArrayList<Article> 	_articles;
	
	
	public void setFeedName(String strFeedName){_strFeedName=strFeedName;}
	public String getFeedName() {return _strFeedName;}
	public void setFeedLink(String strFeedLink){_strFeedLink=strFeedLink;}
	public String getFeedLink() {return _strFeedLink;}
	public void setFeedKey(String strFeedKey){_strFeedKey= strFeedKey;}
	public String getFeedKey() {return _strFeedKey;}
	
	public Feed( String strFName, String strFLink, String strFKey ) {
		_articles = new ArrayList<Article>();
		_strFeedName = strFName;
		_strFeedLink = strFLink;
		_strFeedKey = strFKey;
		
	}
	public Feed( ) {
		_articles = new ArrayList<Article>();
		_strFeedName = "";
		_strFeedLink = "";
		_strFeedKey = "";
		
	}
	public void AddArticle( Article article ) {
		// TODO Error Check
		_articles.add( article );
	}
	public ArrayList<Article> GetArticles() {
		return _articles;
	}
	@Override 
	public boolean equals( Object rhs ) {
		if (rhs == null) return false;
		
		if ( this == rhs ) return true;
		
		if ( !(rhs instanceof Feed) ) return false;
		
	    //cast to native object is now safe
		Feed that = (Feed)rhs;
		if ( _strFeedKey.equals( that.getFeedKey() ))
			return true;
			
		return false;
	}
}
