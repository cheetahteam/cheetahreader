package com.example.tokentest;

public class Article {
	
	private String _strFeedName;
	private String _strFeedLink;
	private String _strKey;
	private String _strReadLink;
	private String _strTitle;
	private String _strPeekLink;
	private String _strDatakey;
	
	// change 1
		static private boolean _markread; // markread
		
		public void setMarkread(boolean mk) 
		{
			 _markread = mk;
			
		}
		
		public boolean getMarkread()
		{
			return _markread;
		}
	
	public void setFeedName(String strFeedName){_strFeedName=strFeedName;}
	public String getFeedName() {return _strFeedName;}
	public void setFeedLink(String strFeedLink){_strFeedLink=strFeedLink;}
	public String getFeedLink() {return _strFeedLink;}
	public void setKey(String strKey){_strKey=strKey;}
	public String getKey() {return _strKey;}
	public void setReadLink( String strReadLink){_strReadLink=strReadLink;}
	public String getReadLink() {return _strReadLink;}
	public void setTitle( String strTitle){_strTitle=strTitle;}
	public String getTitle() {return _strTitle;}
	public void setPeekLink( String strPeekLink){_strPeekLink=strPeekLink;}
	public String getPeekLink() {return _strPeekLink;}
	public void setDataKey( String strDatakey){_strDatakey=strDatakey;}
	public String getDataKey() {return _strDatakey;}
	
	public Article( ) {
		
		_strFeedName = "";
		_strFeedLink = "";
		_strKey = "";
		_strReadLink = "";
		_strTitle = "";
		_strPeekLink = "";
		_strDatakey = "";
	}
	public Article( String strFeedName, String strFeedLink, String strKey, String strReadLink, String strTitle, String strPeekLink, String strDataKey ) {
		
		_strFeedName = strFeedName;
		_strFeedLink = strFeedLink;
		_strKey = strKey;
		_strReadLink = strReadLink;
		_strTitle = strTitle;
		_strPeekLink = strPeekLink;
		_strDatakey = strDataKey;
	}
	@Override 
	public boolean equals( Object rhs ) {
		if ( this == rhs ) return true;
		
		if ( !(rhs instanceof Article) ) return false;
	    //cast to native object is now safe
		Article that = (Article)rhs;
		
		if ( this._strKey.equals( that.getKey() ) )
			return true;
		
		return false;
	}
	
	@Override 
	public String toString( ) {
		return this._strTitle;
	}

}
