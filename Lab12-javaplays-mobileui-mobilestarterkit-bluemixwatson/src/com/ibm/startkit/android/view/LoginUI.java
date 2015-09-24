/*
 * Copyright 2015 IBM Corp. All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.startkit.android.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.android.asynctask.TaskReceiver;
import com.ibm.startkit.android.app.R;
import com.ibm.startkit.android.app.BlueMobileApp;
import com.ibm.startkit.android.bluemix.LoginTask;

import com.ibm.startkit.android.view.apptasks.SearchUI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/*

Facilitates user login process
Calls the login service asynchronously
 */

public class LoginUI extends Activity  implements TaskReceiver   {

	//key to read from the properties file; these have to match exactly as
	//in starterkit.properties file
	public static String _LOGIN_MSG_RESPONSE = "loginresponse";
	public static String _LOGIN_MSG_FAILED   = "loginfailedmessage";
	public static String _LOGIN_MSG_MISSING  = "loginparammissingmessage";


	public static final String CLASS_NAME = LoginUI.class.getSimpleName();

	private static String  _loginResponse            = null;
	private static String  _loginFailedMessage       = null;
	private  static String _loginParamMissingMessage = null;

	ProgressDialog _pDialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//read some values from the property value
		readProperties();

		//set the ui design as the View for this activity
		setContentView(R.layout.loginui);

		//assign the button's click listener
		setButtonEvent();

	}

	private void setButtonEvent(){

		Button bt_Login    = (Button)   findViewById( R.id.bt_Login    );
		Button bt_Register = (Button)   findViewById( R.id.bt_Register );

		_pDialog = new ProgressDialog( this );

		bt_Login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				checkLogin();


			}
		});

		bt_Register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginUI.this, RegisterUI.class);
				startActivity(intent);
			}
		});

	}
	
	@Override
	protected void onResume(){
		
		super.onResume();
		enterApplication();
		
		
	}
	
	//define what to do once the login task in completed
	@Override
	public void receiveResult(String response, String source) {
		
		EditText et_UserID  = (EditText) findViewById( R.id.et_UserID   );
		
		Button bt_Login     = (Button)   findViewById( R.id.bt_Login    );
        Button bt_Register  = (Button)   findViewById( R.id.bt_Register );
        
        Log.i(_loginResponse, response);
        
		_pDialog.dismiss();

		//first, check if the returned task has the correct ID
		if( source.equals( LoginTask._LOGIN_TASK_ID ) ){
        	
			bt_Login.setEnabled(true);
        	bt_Register.setEnabled(true);
        	et_UserID.setEnabled(true);
			
			if( response.equals( "Successful" ) ){

				//if the login is successfull, let the user enter the application
    			String userID = String.valueOf( et_UserID.getText() );
    			
    			BlueMobileApp.setUserID( userID );
    			
    			enterApplication();
			}
			
			else{
				
				Toast.makeText( this, _loginFailedMessage, Toast.LENGTH_LONG).show();
				Log.i("Login Status" , "Failed " );
			}
			
		}
	}
	
	//execute the login task asynchronously
	public void checkLogin(){
		
		EditText et_User_ID  = ( EditText ) findViewById ( R.id.et_UserID    );
        EditText et_Password = ( EditText ) findViewById ( R.id.et_Password  );
        String loginURL = "login";
		
		String userID    = String.valueOf( et_User_ID.getText()  );
        String password  = String.valueOf( et_Password.getText() );
        
        if( userID.equals("") || password.equals( "" ) ){
        	Toast.makeText( this, _loginParamMissingMessage, Toast.LENGTH_LONG ).show();
        	return;
        }
        
        _pDialog.setTitle("Logging in..");
		_pDialog.setMessage("Please wait..");
		_pDialog.show();
        
		//create an asynchronous task, assign this as the receiver and execute
		//the task
        LoginTask loginTask = new LoginTask( BlueMobileApp.getLoginURI( getApplicationContext() ) );
		loginTask.setReceiver(this);
        loginTask.execute(userID, password);
        
	}

	private void enterApplication(){

		if( BlueMobileApp.isLoggedIn() ){

			//create a new intent with the search UI
			Intent intent = new Intent( LoginUI.this,SearchUI.class );

			//make sure that the activity history is cleared so that the
			//user cannot come back to the login UI using the device's back button
			intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
			//start the new activity and finish off this one
			startActivity( intent );
			finish();

		}
	}

	private void readProperties(){

		Context context = getApplicationContext();
		Properties props = new java.util.Properties();
		try {
			AssetManager assetManager = context.getAssets();
			props.load(assetManager.open( BlueMobileApp.PROPS_FILE));

			System.out.println("Inside Properties File value for the URL is" + props.getProperty("baseuri"));
			Log.i(CLASS_NAME, "Found configuration file: " + BlueMobileApp.PROPS_FILE );
		} catch (FileNotFoundException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file was not found.", e);
		} catch (IOException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file could not be read properly.", e);
		}
		this._loginResponse            = props.getProperty( _LOGIN_MSG_RESPONSE );
		this._loginFailedMessage       = props.getProperty( _LOGIN_MSG_FAILED   );
		this._loginParamMissingMessage = props.getProperty( _LOGIN_MSG_MISSING  );
	}
	
}
