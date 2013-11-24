package gui;

import com.example.tokentest.R;

import util.AuthUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingsActivity extends Activity implements OnClickListener {
	
	AuthUtil authUtil;
	@Override
	public void onCreate(Bundle bundle){
		setContentView(R.layout.activity_auth);
		
		//views
		
		//setting up account dropdown and select button
		authUtil = new AuthUtil(this,  R.id.accounts_tester_account_types_spinner,  
				android.R.layout.simple_spinner_item);
		
		//
		
	}
	
	@Override
	public void onClick(View v) {
		/*
		switch (v.getId()){
	    	case R.id.advanceButton:
	    		
	    		doAdvance();
				break;
	    	case R.id.okayButton:
	    		
	    		doOkay();
				break;
				
	    	case R.id.undoButton:
	    		doUndo();
	    		break;
    	}*/
	}
	
	
	

}
