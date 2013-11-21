package com.example.tokentest;

import java.util.LinkedList;
import java.util.Queue;

import android.os.AsyncTask;

/*
 * only Action manager should be interacting with Simplecta
 */
public class ActionManager extends AsyncTask<Void, Void, Void>{
	private Simplecta simplecta;
	private Queue<Action> queue;
	private FeedManager feedManager;
	
	private static ActionManager instance = null;
	
	public static ActionManager getInstance(){
		if(instance == null){
			instance = new ActionManager();
		}
		return instance;
	}
	
	private ActionManager(){
		this.queue = new LinkedList<Action>();
		simplecta = Simplecta.getInstance();
		feedManager = FeedManager.getInstance();
	}
	
	
	//updates screen imediatly
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
				try {
					return this.feedManager.updateFeeds(this.simplecta.getAllURL());
				} catch (Exception e) {
					e.printStackTrace();
					return false;
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
		while(true){
			this.executeQueue();
			
		}
	}
}
