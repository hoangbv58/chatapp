package com.TeamHoangDangTu.ChatApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.TeamHoangDangTu.ChatApp.interfacer.Manager;
import com.TeamHoangDangTu.ChatApp.serve.MessagingService;
import com.TeamHoangDangTu.ChatApp.R;


public class SigningUp extends Activity {
	
	private static final int FILL_ALL_FIELDS = 0;
	protected static final int TYPE_SAME_PASSWORD_IN_PASSWORD_FIELDS = 1;
	private static final int SIGN_UP_FAILED = 9;
	private static final int SIGN_UP_USERNAME_CRASHED = 3;
	private static final int SIGN_UP_SUCCESSFULL = 4;
	protected static final int USERNAME_AND_PASSWORD_LENGTH_SHORT = 5;
	
	
	private static final String SERVER_RES_RES_SIGN_UP_SUCCESFULL = "1";
	private static final String SERVER_RES_SIGN_UP_USERNAME_CRASHED = "2";
	
	
	
	private EditText usernameText;
	private EditText passwordText;
	private EditText eMailText;
	private EditText passwordAgainText;
	private Manager imService;
	private Handler handler = new Handler();
	
	private ServiceConnection mConnection = new ServiceConnection() {
        

		public void onServiceConnected(ComponentName className, IBinder service) {
            imService = ((MessagingService.IMBinder)service).getService();
            
            
        }

        public void onServiceDisconnected(ComponentName className) {
        	imService = null;
            Toast.makeText(SigningUp.this, R.string.local_service_stopped,
                    Toast.LENGTH_SHORT).show();
        }
    };

	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);    

	    
	               
	        setContentView(R.layout.signingup);
	        setTitle("Sign up");
	        
	        Button signUpButton = (Button) findViewById(R.id.signUp);
	        Button cancelButton = (Button) findViewById(R.id.cancel_signUp);
	        usernameText = (EditText) findViewById(R.id.userName);
	        passwordText = (EditText) findViewById(R.id.password);  
	        passwordAgainText = (EditText) findViewById(R.id.passwordAgain);  
	        eMailText = (EditText) findViewById(R.id.email);
	        
	        signUpButton.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0) 
				{						
					if (usernameText.length() > 0 &&		
						passwordText.length() > 0 && 
						passwordAgainText.length() > 0 &&
						eMailText.length() > 0
						)
					{

						if (passwordText.getText().toString().equals(passwordAgainText.getText().toString())){

							if (usernameText.length() >= 5 && passwordText.length() >= 5) {

									Thread thread = new Thread(){
										String result = new String();
										@Override
										public void run() {
											result = imService.signUpUser(usernameText.getText().toString(), 
													passwordText.getText().toString(), 
													eMailText.getText().toString());

											handler.post(new Runnable(){

												public void run() {
													if ((result != null) && result.equals(SERVER_RES_RES_SIGN_UP_SUCCESFULL)) {
														Toast.makeText(getApplicationContext(),R.string.signup_successfull, Toast.LENGTH_LONG).show();
													}
													else if ( (result != null)  && result.equals(SERVER_RES_SIGN_UP_USERNAME_CRASHED) ){
														Toast.makeText(getApplicationContext(),R.string.signup_username_crashed, Toast.LENGTH_LONG).show();
													}
													else
													{
														Toast.makeText(getApplicationContext(),R.string.signup_failed, Toast.LENGTH_LONG).show();
													}
												}

											});
										}
		
									};
									thread.start();
							}
							else{
								Toast.makeText(getApplicationContext(),R.string.username_and_password_length_short, Toast.LENGTH_LONG).show();
							}
						}
						else {
							Toast.makeText(getApplicationContext(),R.string.signup_type_same_password_in_password_fields, Toast.LENGTH_LONG).show();
						}
						
					}
					else {
						Toast.makeText(getApplicationContext(),R.string.signup_fill_all_fields, Toast.LENGTH_LONG).show();

					}				
				}       	
	        });
	        
	        cancelButton.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0) 
				{						
					finish();					
				}	        	
	        });
	        
	        
	    }
	
	
	protected Dialog onCreateDialog(int id) 
	{    	
		  	
		switch (id) 
		{
			case TYPE_SAME_PASSWORD_IN_PASSWORD_FIELDS:			
				return new AlertDialog.Builder(SigningUp.this)       
				.setMessage(R.string.signup_type_same_password_in_password_fields)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})        
				.create();			
			case FILL_ALL_FIELDS:				
				return new AlertDialog.Builder(SigningUp.this)       
				.setMessage(R.string.signup_fill_all_fields)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})        
				.create();
			case SIGN_UP_FAILED:
				return new AlertDialog.Builder(SigningUp.this)       
				.setMessage(R.string.signup_failed)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})        
				.create();
			case SIGN_UP_USERNAME_CRASHED:
				return new AlertDialog.Builder(SigningUp.this)       
				.setMessage(R.string.signup_username_crashed)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})
				.create();
			case SIGN_UP_SUCCESSFULL:
				return new AlertDialog.Builder(SigningUp.this)       
				.setMessage(R.string.signup_successfull)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				})        
				.create();	
			case USERNAME_AND_PASSWORD_LENGTH_SHORT:
				return new AlertDialog.Builder(SigningUp.this)       
				.setMessage(R.string.username_and_password_length_short)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})        
				.create();
			default:
				return null;

		}


	}

	@Override
	protected void onResume() {
		bindService(new Intent(SigningUp.this, MessagingService.class), mConnection , Context.BIND_AUTO_CREATE);
		   
		super.onResume();
	}
	
	@Override
	protected void onPause() 
	{
		unbindService(mConnection);
		super.onPause();
	}
	
	

}
