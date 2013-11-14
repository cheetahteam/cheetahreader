package com.example.tokentest;

import java.util.LinkedList;
import java.util.Queue;

import android.os.AsyncTask;

public class ActionManager extends AsyncTask<Void, Void, Void>{
	private Simplecta simplecta;
	private Queue<Action> queue;
	private String page;
	
	private boolean isNewPage;
	
	ActionManager(){
		this.queue = new LinkedList<Action>();
		simplecta = Simplecta.getInstance();
	}
	
	public boolean updateNow(){
		queue.add(new Action(Action.ACTION.UPDATE, null));
		return this.executeQueue();
	}
	
	
	//adds a action to the queue
	public void addAction(Action action){
			queue.add(action);
	}
	
	public void addAction(Action.ACTION type, String data){
		queue.add(new Action(type, data));
}
	
	
	//removes a action from the queue
	public void removeAction(Action action){
		queue.remove(action);
	}
	
	//sees if a updated page is available
	public boolean isNewPageAvailable(){
		return this.isNewPage;
	}
	
	public String getPage(){
		return this.page;
	}
	
	//calls the apropriate functions in simplecta
	private boolean runAction(Action action){
		switch(action.type){
			case READ:
				return this.simplecta.markRead(action.data);
			case UNREAD:
				return this.simplecta.markUnread(action.data);
			case ADD_ATOM:
				return this.simplecta.addAtom(action.data);
			case ADD_RSS:
				return this.simplecta.addRSS(action.data);
			case UNSUBSCRIBE:
				return this.simplecta.unsubscribe(action.data);
			case UPDATE:
				this.page = this.simplecta.getAllURL();
				if(page.isEmpty())
					return false;
				else {
					this.isNewPage = true;
					return true;
				}
				
		}
		
		return false;
		
	}
	
	//goes through the entire queue and runs every action
	private boolean executeQueue(){
		
		Action action = null;
		while(this.queue.size()!=0){
			action = this.queue.peek();
			if(this.runAction(action)==false){
				this.queue.remove();
				break;
			}
		}
		return true;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO sleep for 20 seconds
		this.executeQueue();
		return null;
	}
}
