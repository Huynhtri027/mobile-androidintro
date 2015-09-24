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

/*

Facilitates user registration process
Calls the registration service asynchronously
Also uses Ethnicity and Fitness list retrieval services asynchronously
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ibm.android.asynctask.TaskReceiver;
import com.ibm.startkit.android.app.R;
import com.ibm.startkit.android.app.BlueMobileApp;
import com.ibm.startkit.android.bluemix.EthnicityListTask;
import com.ibm.startkit.android.bluemix.FitnessRoutineListTask;
import com.ibm.startkit.android.bluemix.RegisterTask;
import com.ibm.startkit.android.entities.Ethnicity;
import com.ibm.startkit.android.entities.Fitness;
import com.ibm.startkit.android.view.apptasks.SearchUI;


public class RegisterUI extends Activity implements TaskReceiver  {

	//key to read from the properties file; these have to match exactly as
	//in starterkit.properties file
	public static String _REGISTER_MSG_EMPTY     = "inputemptyerror";
	public static String _REGISTER_MSG_SUCCESS   = "registrationsuccess";


	public static String _inputEmptyError     = null;
	public static String _registrationSuccess = null;
    
    public static final String CLASS_NAME = RegisterUI.class.getSimpleName();
	ProgressDialog _pDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//read some values from the properties file
		readProperties();

		//set the register ui as the view of this activity
		setContentView(R.layout.registerui);

		//set up some UI components
		setupUIComponent();
	}

	//set the button's click listener
	//and then populate two list boxes
	private void setupUIComponent(){


		Button bt_Register = (Button) findViewById( R.id.bt_Register );

		bt_Register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				registerUser();

			}


		});


		//populate the ethnicity and fitness list boxes
		populateListBoxes();

	}

	//the ethnicity and fitness list are retrived from the server
	private void populateListBoxes(){
		
		List< String > ethnicities     = Ethnicity.getEthnicities();
		List< String > fitnessRoutines = Fitness.getFitnessRoutines();
		if( ethnicities == null ){
			
			//call the rest api to get the ethnicities
			EthnicityListTask elt = new EthnicityListTask( BlueMobileApp.getEthnicitiesListURL(getApplicationContext()) );
	        elt.setReceiver( this );
	        elt.execute( "" );
		}
		else{
			
			Spinner ethnicitySpinner = (Spinner) findViewById( R.id.sp_Ethnicity );
			setSpinnerAdapter( ethnicities, ethnicitySpinner );
		}
		if( fitnessRoutines == null ){

			//call the rest api to get the fitness list
			FitnessRoutineListTask frlt = new FitnessRoutineListTask( BlueMobileApp.getFitnessListURL( getApplicationContext() ));
	        frlt.setReceiver( this );
	        frlt.execute( "" );
		}
		else{
			Spinner fitnessSpinner = ( Spinner ) findViewById( R.id.sp_Fitness );
			setSpinnerAdapter( fitnessRoutines, fitnessSpinner );
		}
		
	}
	
	private void setSpinnerAdapter( List< String > data, Spinner sp ){
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		
		sp.setAdapter( adapter );
	}
	
	//call the registration service asynchronously
	public void registerUser(){

		if( _pDialog == null ){
			_pDialog = new ProgressDialog( this );
		}
        
        EditText et_UserID     = (EditText) findViewById( R.id.et_UserID     );
        EditText et_Password   = (EditText) findViewById( R.id.et_Password   );
        EditText et_Name       = (EditText) findViewById( R.id.et_Name       );
        EditText et_Zip        = (EditText) findViewById( R.id.et_Zip        );
        EditText et_Age        = (EditText) findViewById( R.id.et_Age        );
        EditText et_Profession = (EditText) findViewById( R.id.et_Profession );
        EditText et_Interest   = (EditText) findViewById( R.id.et_Interests  );
        Spinner  sp_Ethnicity  = (Spinner)  findViewById( R.id.sp_Ethnicity  );
        Spinner  sp_Fitness    = (Spinner)  findViewById( R.id.sp_Fitness    );
          
        String userID     = String.valueOf( et_UserID.getText()         );
        String password   = String.valueOf( et_Password.getText()       );
        String name       = String.valueOf( et_Name.getText()           );
        String zip        = String.valueOf( et_Zip.getText()            );
        String interest   = String.valueOf( et_Interest.getText()       );
        String age        = String.valueOf( et_Age.getText()            );
        String profession = String.valueOf( et_Profession.getText()     );
        String ethnicity  = (String) sp_Ethnicity.getSelectedItem()     ;
        String fitness    = (String) sp_Fitness.getSelectedItem()       ;
        
        String validationResult = isValid( userID, password, name, zip, interest, age, profession, ethnicity, fitness );
        if( !validationResult.equals( RegisterUI._registrationSuccess ) ){
        	

        	Toast.makeText( this, validationResult, Toast.LENGTH_LONG ).show();
        	return;
        }
        
		_pDialog.setTitle("Registering..");
		_pDialog.setMessage("Registering your detail...\r");
		_pDialog.show();
		
		Button bt_Register = (Button) findViewById( R.id.bt_Register );
		bt_Register.setEnabled( false );

        //call the registration service asynchronously
        RegisterTask rt = new RegisterTask( BlueMobileApp.getRegisterURI( getApplicationContext() ) );
        rt.setReceiver( this );
        rt.execute( userID, password, name, age, zip, profession, interest, ethnicity, fitness );
        
        
	}


	//do something when the asynchronous task is complete
	//NOTE: there are three asynchronous tasks in the activity
	@Override
	public void receiveResult(String response, String source) {

		//check if the completed task is a registration task
		if( source.equals( RegisterTask._REG_TASK_ID ) ){

			EditText et_UserID  = (EditText) findViewById( R.id.et_UserID );
			_pDialog.dismiss();
			//if the registratio is successful, finish this task
			//clear the activity history and let the user enter into the application
			if( response.equals( "Successful" ) ){
				String twitterID = String.valueOf( et_UserID.getText() );
				BlueMobileApp.setUserID( twitterID );
				enterApplication();
			}

			else{

				Toast.makeText( getApplicationContext(), "Registration failed.", Toast.LENGTH_LONG).show();
			}


			Button bt_Register = (Button) findViewById( R.id.bt_Register );
			bt_Register.setEnabled( true );
			et_UserID.setEnabled( true );

		}

		//check if the completed task is a ethnicity list retrieval task
		if( source.equals( EthnicityListTask._ETHNICITY_RETRIEVE_TASK_ID ) ){

			if( !response.equals( "Failed") ){
				//is successful populate the list box
				try{
					List< String > ethnicitiesList = new ArrayList< String >();

					JSONObject ethnicities = new JSONObject( response );

					JSONArray ethnicityArray = ethnicities.getJSONArray( "ethnicities" );

					for( int e = 0 ; e < ethnicityArray.length() ; e++ ){

						JSONObject ethnicity = (JSONObject) ethnicityArray.get( e );
						ethnicitiesList.add( ethnicity.getString( "name" ) );

					}

					Ethnicity.setEthnicities( ethnicitiesList );
					Spinner ethnicitySpinner = (Spinner) findViewById( R.id.sp_Ethnicity );
					setSpinnerAdapter( ethnicitiesList, ethnicitySpinner );

				}
				catch( Exception e ){

				}
			}
		}

		//check if the completed task is a fitness list retrieval task
		if( source.equals( FitnessRoutineListTask._FITNESS_RETRIEVE_TASK_ID ) ){

			if( !response.equals( "Failed") ){
				//if successful, populate the fitness list box
				try{
					List< String > fitnessRoutineList = new ArrayList< String >();

					JSONObject fitness = new JSONObject( response );

					JSONArray fitnessArray = fitness.getJSONArray( "fitnessRoutines" );

					for( int e = 0 ; e < fitnessArray.length() ; e++ ){

						JSONObject ethnicity = (JSONObject) fitnessArray.get( e );
						fitnessRoutineList.add( ethnicity.getString( "name" ) );

					}


					Fitness.setFitnessRoutines( fitnessRoutineList );
					Spinner fitnessSpinner = (Spinner) findViewById( R.id.sp_Fitness );
					setSpinnerAdapter( fitnessRoutineList, fitnessSpinner );

				}
				catch( Exception e ){

				}
			}
		}


	}
	
	private String isValid( String userID    , 
			                String password  , 
			                String name      , 
			                String zip       , 
			                String interest  , 
			                String age       ,
			                String profession, 
			                String ethnicity , 
			                String fitness ){
		
		if( userID     .equals( "" ) ||
			password   .equals( "" ) ||
			name       .equals( "" ) ||
			zip        .equals( "" ) ||
			interest   .equals( "" ) ||
			age        .equals( "" ) ||
			profession .equals( "" ) ||
			ethnicity  .equals( "" ) ||
			fitness    .equals( "" ) ){
			
			return _inputEmptyError;
		}
		
		return _registrationSuccess;
	}
	

	private void enterApplication(){
		
		if( BlueMobileApp.isLoggedIn() ){
			
			Intent intent = new Intent( RegisterUI.this, SearchUI.class );
			
			intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
	        startActivity( intent );
			finish();
			
		}
	}
	


	private void readProperties() {
		Context context = getApplicationContext();
		Properties props = new java.util.Properties();
		try {
			AssetManager assetManager = context.getAssets();
			props.load(assetManager.open(BlueMobileApp.PROPS_FILE));

			System.out.println("Inside Properties File value for the URL is" + props.getProperty("baseuri"));
			Log.i(CLASS_NAME, "Found configuration file: " + BlueMobileApp.PROPS_FILE);
		} catch (FileNotFoundException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file was not found.", e);
		} catch (IOException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file could not be read properly.", e);
		}
		this._inputEmptyError     = props.getProperty( _REGISTER_MSG_EMPTY   );
		this._registrationSuccess = props.getProperty( _REGISTER_MSG_SUCCESS );
		Log.i(CLASS_NAME, "Found Property :  BaseURI" + props.getProperty("baseuri"));
	}
}
