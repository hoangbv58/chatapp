package com.TeamHoangDangTu.ChatApp;


import java.io.UnsupportedEncodingException;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.TeamHoangDangTu.ChatApp.interfacer.Manager;
import com.TeamHoangDangTu.ChatApp.serve.MessagingService;
import com.TeamHoangDangTu.ChatApp.toolBox.ControllerOfFriend;
import com.TeamHoangDangTu.ChatApp.toolBox.StorageManipulater;
import com.TeamHoangDangTu.ChatApp.typo.InfoOfFriend;
import com.TeamHoangDangTu.ChatApp.typo.InfoOfMessage;
import com.TeamHoangDangTu.ChatApp.R;

import static com.TeamHoangDangTu.ChatApp.R.*;


public class PerformingMessaging extends Activity {

	private static final int MESSAGE_CANNOT_BE_SENT = 0;
	public String username;
	private EditText messageText;
	private EditText messageHistoryText;
	private Button sendMessageButton;
	private Manager imService;
	private InfoOfFriend friend = new InfoOfFriend();
	private StorageManipulater localstoragehandler; 
	private Cursor dbCursor;
	
	private ServiceConnection mConnection = new ServiceConnection() {
      
		
		
		public void onServiceConnected(ComponentName className, IBinder service) {          
            imService = ((MessagingService.IMBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
        	imService = null;
            Toast.makeText(PerformingMessaging.this, string.local_service_stopped,
                    Toast.LENGTH_SHORT).show();
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(layout.message); //messaging_screen);
				
		messageHistoryText = (EditText) findViewById(id.messageHistory);
		
		messageText = (EditText) findViewById(id.message);
		
		messageText.requestFocus();			
		
		sendMessageButton = (Button) findViewById(id.sendMessageButton);
		
		Bundle extras = this.getIntent().getExtras();
		
		
		friend.userName = extras.getString(InfoOfFriend.USERNAME);
		friend.ip = extras.getString(InfoOfFriend.IP);
		friend.port = extras.getString(InfoOfFriend.PORT);
		String msg = extras.getString(InfoOfMessage.MESSAGETEXT);

		setTitle("" + friend.userName);
		final int[] color = {Color.RED, Color.BLUE,Color.GREEN,Color.WHITE};
		localstoragehandler = new StorageManipulater(this);
		dbCursor = localstoragehandler.get(friend.userName, MessagingService.USERNAME );

		final LinearLayout bgElement = (LinearLayout) findViewById(R.id.container);
		bgElement.setBackgroundColor(color[1]);

		if (dbCursor.getCount() > 0){
		int noOfScorer = 0;
		dbCursor.moveToFirst();
		    while ((!dbCursor.isAfterLast())&&noOfScorer<dbCursor.getCount()) 
		    {
		        noOfScorer++;

				this.appendToMessageHistory(dbCursor.getString(2) , dbCursor.getString(3),1);
		        dbCursor.moveToNext();
		    }
		}
		localstoragehandler.close();
		
		if (msg != null) 
		{
			this.appendToMessageHistory(friend.userName , msg,1);
			((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancel((friend.userName+msg).hashCode());
		}


		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				array.color, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					bgElement.setBackgroundColor(color[0]);
				}
				if (position == 1) {
					bgElement.setBackgroundColor(color[1]);
				}
				if (position == 2) {
					bgElement.setBackgroundColor(color[2]);
				}
				if (position == 3) {
					bgElement.setBackgroundColor(color[3]);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});



		sendMessageButton.setOnClickListener(new OnClickListener(){
			CharSequence message;
			Handler handler = new Handler();
			public void onClick(View arg0) {
				message = messageText.getText();
				if (message.length()>0) 
				{		
					appendToMessageHistory(imService.getUsername(), message.toString(),1);
					
					localstoragehandler.insert(imService.getUsername(), friend.userName, message.toString());
								
					messageText.setText("");
					Thread thread = new Thread(){					
						public void run() {
							try {
								if (imService.sendMessage(imService.getUsername(), friend.userName, message.toString()) == null)
								{
									
									handler.post(new Runnable(){	

										public void run() {
											
									        Toast.makeText(getApplicationContext(), string.message_cannot_be_sent, Toast.LENGTH_LONG).show();

										}
										
									});
								}
							} catch (UnsupportedEncodingException e) {
								Toast.makeText(getApplicationContext(), string.message_cannot_be_sent, Toast.LENGTH_LONG).show();

								e.printStackTrace();
							}
						}						
					};
					thread.start();
										
				}
				
			}});
		
		messageText.setOnKeyListener(new OnKeyListener(){
			public boolean onKey(View v, int keyCode, KeyEvent event) 
			{
				if (keyCode == 66){
					sendMessageButton.performClick();
					return true;
				}
				return false;
			}
			
			
		});
				
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		int message = -1;
		switch (id)
		{
		case MESSAGE_CANNOT_BE_SENT:
			message = string.message_cannot_be_sent;
		break;
		}
		
		if (message == -1)
		{
			return null;
		}
		else
		{
			return new AlertDialog.Builder(PerformingMessaging.this)       
			.setMessage(message)
			.setPositiveButton(string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})        
			.create();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(messageReceiver);
		unbindService(mConnection);
		
		ControllerOfFriend.setActiveFriend(null);
		
	}

	@Override
	protected void onResume() 
	{		
		super.onResume();
		bindService(new Intent(PerformingMessaging.this, MessagingService.class), mConnection , Context.BIND_AUTO_CREATE);
				
		IntentFilter i = new IntentFilter();
		i.addAction(MessagingService.TAKE_MESSAGE);
		
		registerReceiver(messageReceiver, i);
		
		ControllerOfFriend.setActiveFriend(friend.userName);		
		
		
	}
	
	
	public class  MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) 
		{		
			Bundle extra = intent.getExtras();
			String username = extra.getString(InfoOfMessage.USERID);			
			String message = extra.getString(InfoOfMessage.MESSAGETEXT);
			
			if (username != null && message != null)
			{
				if (friend.userName.equals(username)) {
					appendToMessageHistory(username, message,1);
					localstoragehandler.insert(username,imService.getUsername(), message);
					
				}
				else {
					if (message.length() > 15) {
						message = message.substring(0, 15);
					}
					Toast.makeText(PerformingMessaging.this,  username + " says '"+
													message + "'",
													Toast.LENGTH_SHORT).show();		
				}
			}			
		}
		
	};
	private MessageReceiver messageReceiver = new MessageReceiver();
	
	public  void appendToMessageHistory(String username, String message, int pos) {
		if( pos == 0){
			if (username != null && message != null) {
				messageHistoryText.append(username + ":\n");
				messageHistoryText.setTextColor(Color.BLACK);
				messageHistoryText.append(message + "\n");
				messageHistoryText.setTextColor(Color.BLACK);
			}
		}
		if (pos == 1){
			if (username != null && message != null) {
				messageHistoryText.append(username + ":\n");
				messageHistoryText.setTextColor(Color.RED);
				messageHistoryText.append(message + "\n");
				messageHistoryText.setTextColor(Color.RED);
			}
		}

		if(pos ==3){
			if (username != null && message != null) {
				messageHistoryText.append(username + ":\n");
				messageHistoryText.setTextColor(Color.GREEN);
				messageHistoryText.append(message + "\n");
				messageHistoryText.setTextColor(Color.GREEN);
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (localstoragehandler != null) {
	    	localstoragehandler.close();
	    }
	    if (dbCursor != null) {
	    	dbCursor.close();
	    }
	}
	

}
