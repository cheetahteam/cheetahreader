package com.example.tokentest;

import java.util.LinkedList;
import java.util.Queue;

import android.os.AsyncTask;

/*
 * only Action manager should be interacting with Simplecta
 */
public class ActionManager {
	private Simplecta simplecta;
	private Queue< Action > queue;
	private FeedManager feedManager;
	public boolean dataUpdated = false;
	private boolean queueExecuting = false;

	private static ActionManager instance = null;

	public static ActionManager getInstance() {
		if ( instance == null ) {
			instance = new ActionManager();
		}
		return instance;
	}

	private ActionManager() {
		this.queue = new LinkedList< Action >();
		simplecta = Simplecta.getInstance();
		feedManager = FeedManager.getInstance();
	}

	// updates screen imediatly
	public void updateNow() {
		//adding unsubscription and read
		updateArticleData();
		updateFeedData();
		
		queue.add( new Action( Action.ACTION.UPDATE_ARTICLES, null ) );
		queue.add( new Action( Action.ACTION.UPDATE_FEEDS, null ) );
		execute();
	}
	
	public void addRSSFeed( String strFeed ) 
	{
		queue.add( new Action( Action.ACTION.ADD_RSS, strFeed ) );
	}
	public void addATOMFeed( String strFeed ) 
	{
		queue.add( new Action( Action.ACTION.ADD_ATOM, strFeed ) );
	}


	// adds a action to the queue
	public void addAction( Action action ) {
		queue.add( action );
	}

	public void addAction( Action.ACTION type, String data ) {
		queue.add( new Action( type, data ) );
	}

	// removes a action from the queue
	public void removeAction( Action action ) {
		queue.remove( action );
	}

	// calls the apropriate functions in simplecta
	private boolean runAction( Action action ) {
		switch ( action.type ) {
		case READ:
			return this.simplecta.markRead( action.data );
		case ADD_ATOM:
			return this.simplecta.addAtom( action.data );
		case ADD_RSS:
			return this.simplecta.addRSS( action.data );
		case UNSUBSCRIBE:
			return this.simplecta.unsubscribe( action.data );
		case UPDATE_ARTICLES:
			try {
				return this.feedManager.setArticles( this.simplecta.showAll() );
			} catch ( Exception e ) {
				e.printStackTrace();
				return false;
			}
		case UPDATE_FEEDS:
			try {
				return this.feedManager.setFeeds( this.simplecta.feeds() );
			} catch ( Exception e ) {
				e.printStackTrace();
				return false;
			}

		}

		return false;

	}

	private void execute() {
		ActionWorker worker = new ActionWorker();
		worker.execute();
	}
	
	void updateFeedData(){
		int size = feedManager.getArticles().size();
		for(int i =0; i<size; i++){
			if(feedManager.getArticle(i).getMarkread() == true){
				queue.add( new Action( Action.ACTION.READ, feedManager.getArticle(i).getKey() ) );
			}
		}
	}
	
	void updateArticleData(){
		int size = feedManager.getArticles().size();
		for(int i =0; i<size; i++){
			if(feedManager.getFeed(i).getMarkSubscribe() == true){
				queue.add( new Action( Action.ACTION.READ, feedManager.getFeed(i).getFeedLink() ) );
			}
		}
	}

	// goes through the entire queue and runs every action
	private boolean executeQueue() {
		if ( queueExecuting )
			return false;
		queueExecuting = true;
		Action action = null;
		while ( this.queue.size() != 0 ) {
			action = this.queue.peek();
			if ( this.runAction( action ) != false ) {
				this.queue.remove( action );
				break;
			}
		}
		queueExecuting = false;
		dataUpdated = true;
		return true;
	}

	class ActionWorker extends AsyncTask< Void, Void, Void > {
		@Override
		protected Void doInBackground( Void... params ) {
			// TODO sleep for 20 seconds
			executeQueue();
			return null;
		}
	}

}
