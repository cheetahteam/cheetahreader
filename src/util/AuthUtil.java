package util;

import java.io.IOException;

import com.example.tokentest.FeedActivity;
import com.example.tokentest.R;
import com.example.tokentest.Simplecta;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.auth.AuthPreferences;

public class AuthUtil {
	 
	private AuthPreferences 						authPreferences;
	private AccountManager 							accountManager;
	private Spinner 								accountTypesSpinner;
	private String[] 								accountNames;
	private static final String TAG = 				"CC AuthUtil";
	private static final String SCOPE = 			"ah";//"oauth2:https://www.googleapis.com/auth/userinfo.profile";
  	private static final int AUTHORIZATION_CODE = 	1993;
  	private Activity activity;
  	
  	public boolean isLoggedIn = false;
	
  	/** progress dialog to show user that the update is processing. */
    //ProgressDialog dialog;
    
	public AuthUtil(Activity activity, int spinnerId, int spinnerItemId) {
		
		accountManager = AccountManager.get(activity);
		accountNames = getAccountNames( activity );
		authPreferences = new AuthPreferences(activity);
		this.activity = activity;
	    
		// TODO if there are no account names,
		// ask the user to create one
		initializeSpinner(spinnerId, spinnerItemId, accountNames );
		
	}
 
	private void initializeSpinner( int spinnerId, int spinnerItemId, String[] values ) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.activity,spinnerItemId, values);
        Spinner spinner = (Spinner) activity.findViewById(spinnerId);
        spinner.setAdapter(adapter);
        accountTypesSpinner =  spinner;
    }
	
	public String[] getAccountNames( Activity activity ) {
        Account[] accounts = accountManager.getAccountsByType( GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE );
        String[] names = new String[accounts.length];
        for ( int i = 0; i < names.length; i++ ) {
            names[i] = accounts[i].name;
        }
        return names;
    }
	
	
	
	public void launchActivity() {
	}

 
	/**
	   * Get a authentication token if one is not available. Throws an exception or non empty string
	 * @throws IOException 
	   */
	  public String fetchToken( Activity activity, String strEmail, String strScope ) throws IOException {
	      try {
	    	   return GoogleAuthUtil.getToken(
	        		  activity, strEmail, strScope);
	      } 
	      catch (GooglePlayServicesAvailabilityException playEx) {
	          Dialog alert = GooglePlayServicesUtil.getErrorDialog(
	              playEx.getConnectionStatusCode(),
	              activity,
	              AUTHORIZATION_CODE);
	          return null;
	      }
	      catch (UserRecoverableAuthException userRecoverableException ) {
	    		  activity.startActivityForResult(userRecoverableException.getIntent(), AUTHORIZATION_CODE );
	    		  return null;
	      }
	      catch (IOException transientEx) {
	          // network or server error, the call is expected to succeed if you try again later.
	          // Don't attempt to call again immediately - the request is likely to
	          // fail, you'll hit quotas or back-off.
	          throw transientEx;
	       }
	      catch (GoogleAuthException fatalException) {
	    	  Log.e( TAG, "Unrecoverable error " + fatalException.getMessage(), fatalException);
	    	  return null;
	      }
	  }
	
	private void executeRequest() {
		int accountIndex = accountTypesSpinner.getSelectedItemPosition();
        if (accountIndex < 0) {
            // this happens when the sample is run in an emulator which has no google account
            // added yet.
            //show("No account available. Please add an account to the phone first.");
            return;
        }
        authPreferences.setUser( accountNames[accountIndex] );
        getTokenFromAccount();
    }

 
	private void getTokenFromAccount() {
		AbstractGetTokenTask taskFetchToken = new AbstractGetTokenTask( activity, authPreferences.getUser(), SCOPE, 101 );
		taskFetchToken.execute();	
	}
	
	private class AbstractGetTokenTask extends AsyncTask<Void, Void, Void>{
	    private static final String TAG = "TokenInfoTask";
	    //private static final String NAME_KEY = "given_name";
	    protected Activity activity;

	    protected String strScope;
	    protected String strEmail;
	    protected int nRequestCode;
	    protected String strToken;
	    /** progress dialog to show user that the update is processing. */
	    private ProgressDialog dialog;
	    
	    
	    
	    AbstractGetTokenTask( Activity activity, String email, String scope, int requestCode) {
	        this.activity = activity;
	        this.strScope = scope;
	        this.strEmail = email;
	        this.nRequestCode = requestCode;
	        this.dialog = new ProgressDialog(activity);
	        
	       
	        //auth = new AuthHelper();
	    }

	    @Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    	
	    	
	    	this.dialog.setTitle("Signing in...");
	    	this.dialog.setMessage("Please wait.");
	    	this.dialog.setCancelable(false);
	    	this.dialog.setIndeterminate(true);
	    	this.dialog.show();
	    }
	    
	    @Override
	    protected Void doInBackground(Void... params) {

	    	try {
				strToken = fetchToken( activity, strEmail, strScope );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	if ( strToken != null ) {
	    		Log.d(TAG,  strToken );
		    	authPreferences.setToken( strToken );
		    	Simplecta simplecta = Simplecta.getInstance();
		    	Log.d(TAG,  "Simplecta initializing" );
				simplecta.init( activity, strToken );
	    	}
			
			
	    	return null;
	    }
	    
	    @Override
	    public void onPostExecute(Void result) {
	    	if (this.dialog.isShowing()) {
	    		this.dialog.dismiss();
	        }
	    	if ( strToken != null ) {
	    		Toast.makeText(activity, "token: " + strToken, Toast.LENGTH_SHORT  ).show();
	    		isLoggedIn = true;
	    		launchActivity();
	    		
	    	}
        }
	}
	
}

