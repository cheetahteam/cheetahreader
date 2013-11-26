package com.example.tokentest;

public class Action {

	enum ACTION{
		READ,ADD_ATOM,ADD_RSS,UNSUBSCRIBE,UPDATE_ARTICLES, UPDATE_FEEDS
	}
	
	String data;
	ACTION type;
	
	Action(ACTION type, String data){
		this.type = type;
		this.data = data;
	}
	
}
