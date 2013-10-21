package com.View;
 
import java.io.IOException;
 
import android.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
 
public class AccountActivity extends Activity {
    private AccountManager accountMgr = null;
     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        try {
            accountMgr = AccountManager.get(this);
            Account [] accounts = accountMgr.getAccounts();
            String accountsList = "Accounts: " + accounts.length + "\n";
            for (Account account : accounts) {
                accountsList += account.toString() + "\n";
            }
            setMessage(accountsList);
             
        } catch (Exception e) {
            setMessage(e.toString());
        }
         
        Button loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(mLoginListener);
    }
     
    private OnClickListener mLoginListener = new OnClickListener() {
         
        @SuppressLint("NewApi")
		public void onClick(View v) {
            try {
                Account [] accounts = accountMgr.getAccounts();
                if (accounts.length == 0) {
                    setResult("No Accounts");
                    return;
                }
                 
                Account account = accounts[0];
                
                
                if( Build.VERSION.SDK_INT < 14)
                	accountMgr.getAuthToken(account, "mail", false, new GetAuthTokenCallback(), null);
                else
                	accountMgr.getAuthToken(account, "mail", null , false, new GetAuthTokenCallback(), null);
                 
            } catch (Exception e) {
                setResult(e.toString());
            }
             
             
        }
    };
     
    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {
        public void run(AccountManagerFuture<Bundle> result) {
                Bundle bundle;
                try {
                        bundle = result.getResult();
                        Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
                        if(intent != null) {
                            // User input required
                            startActivity(intent);
                        } else {
                            setResult("Token: " + bundle.getString(AccountManager.KEY_AUTHTOKEN));
                        }
                } catch (Exception e) {
                    setResult(e.toString());
                }
        }
    };
     
    public void setResult(String msg) {
        TextView tv = (TextView) this.findViewById(R.id.result);
        tv.setText(msg);
    }
     
    public void setMessage(String msg) {
        TextView tv = (TextView) this.findViewById(R.id.message);
        tv.setText(msg);
    }
}
