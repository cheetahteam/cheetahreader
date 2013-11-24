package com.example.tokentest;

public class Action {

	enum ACTION{
		READ,UNREAD,ADD_ATOM,ADD_RSS,UNSUBSCRIBE,UPDATE
	}
	
	String data;
	ACTION type;
	
	Action(ACTION type, String data){
		this.type = type;
		this.data = data;
	}
	
}
