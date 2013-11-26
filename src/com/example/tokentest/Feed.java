package com.example.tokentest;

import java.util.ArrayList;

public class Feed {
	
	private String 				_strFeedName;
	private String 				_strFeedLink;
	private String 				_strFeedUnsubLink;
	
	
	public void setFeedName(String strFeedName){_strFeedName=strFeedName;}
	public String getFeedName() {return _strFeedName;}
	public void setFeedLink(String strFeedLink){_strFeedLink=strFeedLink;}
	public String getFeedLink() {return _strFeedLink;}
	public void setFeedUnsubLink(String unsubLink){_strFeedUnsubLink= unsubLink;}
	public String getFeedUnsubLink() {return _strFeedUnsubLink;}
	
	public Feed( String strFName, String strFLink, String unsubLink ) {
		_strFeedName = strFName;
		_strFeedLink = strFLink;
		_strFeedUnsubLink = unsubLink;
		
	}
	public Feed( ) {
		_strFeedName = "";
		_strFeedLink = "";
		_strFeedUnsubLink = "";
		
	}
	@Override 
	public boolean equals( Object rhs ) {
		if (rhs == null) return false;
		
		if ( this == rhs ) return true;
		
		if ( !(rhs instanceof Feed) ) return false;
		
	    //cast to native object is now safe
		Feed that = (Feed)rhs;
		if ( _strFeedLink.equals( that.getFeedLink() ))
			return true;
			
		return false;
	}
}
