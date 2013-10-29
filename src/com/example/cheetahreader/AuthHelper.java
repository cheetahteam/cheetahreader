package com.example.tokentest;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;

public class AuthHelper {

	private AccountManager _accountManager;
	
	private static String TAG = "CC AUTH";
	private static int REQUEST_AUTHORIZATION = 1004;


	public String[] getAccountNames( Activity activity ) {
		_accountManager = AccountManager.get( activity );
        Account[] accounts = _accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }
	
	/**
	   * Get a authentication token if one is not available. Throws an exception or non empty string
	   */
	  public String fetchToken( Activity activity, String strEmail, String strScope ) {
	      try {
	    	   return GoogleAuthUtil.getToken(
	        		  activity, strEmail, strScope);
	      } catch (UserRecoverableNotifiedException userRecoverableException) {
	          // Unable to authenticate, but the user can fix this.
	          // Forward the user to the appropriate activity.
	          Log.e( TAG, "Could not fetch token.", null);
	      } catch (UserRecoverableAuthException userRecoverableException ) {
	    		  activity.startActivityForResult(userRecoverableException.getIntent(), REQUEST_AUTHORIZATION);
	      }
	      catch (GoogleAuthException fatalException) {
	    	  Log.e( TAG, "Unrecoverable error " + fatalException.getMessage(), fatalException);
	      }
	      catch (Exception e) {
	    	  Log.e( TAG, e.getMessage() );
	      }
	      return null;
	  }

}
