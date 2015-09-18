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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.android.asynctask.TaskReceiver;
import com.ibm.startkit.android.app.R;
import com.ibm.startkit.android.app.BlueMobileApp;

import com.ibm.startkit.android.bluemix.LoginTask;
import com.ibm.startkit.android.bluemix.SearchTipsTask;
import com.ibm.startkit.android.view.apptasks.SearchUI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/*

	UI for the user login process
	This extends the Android's Activity class and
	implements the TaskReceiver interface to respond
	to the LoginTask

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
	private ProgressDialog _pDialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//read some values from the properties file
		readProperties();

		//TODO: to set the UI content here, uncomment the following line
		//setContentView( R.layout.loginui);
		
		setButtonEvent();
	}
	
	@Override
	protected void onResume(){
		
		super.onResume();
		enterApplication();

		
	}

	private void setButtonEvent(){
		//TODO: Set the button's click event here
		return;
	}

	//helper function : If the user is logged in,
	//bypass the login UI and enter the application
	private void enterApplication(){
		
		if( BlueMobileApp.isLoggedIn() ){

			//create the new intent with the SearchUI
			Intent intent = new Intent( LoginUI.this,SearchUI.class );

			//clear the history; blocks the user to come back to login page
			//using the device's back button if the login is succcessfull
			intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
			//start the new activity
	        startActivity( intent );

			//finish this one
			finish();
			
		}
	}

	/*

		Makes an Asynchronous call to the LoginTask to check the user's credential
	 */
	public void checkLogin(){

		//TODO: execute the login task asynchronously

		return;

	}

	/*
		Implemented function of the TaskReceiver interface
		This is executed once the LoginTask is completed.
	 */
	@Override
	public void receiveResult(String response, String source) {
		//TODO: do something once the credential is checked
		//and the response is available
		return;
	}

	//read the property file to load some required variables
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
		this._loginResponse            = props.getProperty( LoginUI._LOGIN_MSG_RESPONSE );
		this._loginFailedMessage       = props.getProperty( LoginUI._LOGIN_MSG_FAILED   );
		this._loginParamMissingMessage = props.getProperty( LoginUI._LOGIN_MSG_MISSING  );
	}
	
}
