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

package com.ibm.startkit.android.view.apptasks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.ibm.startkit.android.bluemix.RetrieveProfileTask;
import com.ibm.startkit.android.bluemix.UpdateProfileTask;
import com.ibm.startkit.android.entities.Ethnicity;
import com.ibm.startkit.android.entities.Fitness;
import com.ibm.startkit.android.entities.UserProfile;

public class UpdateProfileUI extends BaseView implements TaskReceiver  {

	public static String _PROFILE_RETRIEVE_PROP    = "profileretrieve";
	public static String _NO_PROFILE_PROP          = "noprofile";
	public static String _PROFILE_UPDATE_LOCADING  = "profileupdateloading";
	public static String _PROFILE_UPDATE_SUCCESS   = "profileupdate";
	public static String _PROFILE_UPDATE_ERROR     = "profileupdateerror";



	private static final String CLASS_NAME = SearchUI.class.getSimpleName();
	private static String _profileRetrieve=null;
	private static String _noProfile=null;
	private static String _profileUpdate=null;
	private static String _profileUpdateLoading=null;
	private static String _profileUpdateError=null;

	ProgressDialog _pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//read some values from the properties file
		readProperties();

		//this time we will use the same UI as the registration
		setContentView( R.layout.registerui);

		_pDialog = new ProgressDialog(this);

		//then set up some UI components
		setupUIComponent();
	}


	//set up some UI components
	private void setupUIComponent(){

		//user's id cannot be updated; so disable it

		//user id cannot change
		EditText etUserID = (EditText) findViewById( R.id.et_UserID );
		etUserID.setEnabled( false );

		//re-use the registration xml but simply change the button text
		//to "Update" instead of "Register"
		Button btUpdate = (Button) findViewById(R.id.bt_Register);
		btUpdate.setText( "Update" );

		//then setup its click listener
		btUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				updateUser();

			}
		});

		//finally populate all the fields
		populateUserProfile();
	}

	//retrieve the current user profile asynchronously
	//and populate all the UI components
	private void populateUserProfile(){
		
		//populate the ethnicity and fitness list boxes
        populateListBoxes();

        EditText etUserID = (EditText) findViewById( R.id.et_UserID );
        
        etUserID.setText( BlueMobileApp.getUserID() );
        
        _pDialog.setTitle(_profileRetrieve);
		_pDialog.show();
		
		Button btUpdate = (Button) findViewById(R.id.bt_Register);
        btUpdate.setEnabled( false );

		//execute the task asynchronously
        RetrieveProfileTask rpt = new RetrieveProfileTask( BlueMobileApp.getUserProfileURI( getApplicationContext() ) );
        rpt.setReceiver(this);
        rpt.execute(BlueMobileApp.getUserID());
        
        
	}

	//populate the ethnicity and fitness list boxes
	private void populateListBoxes(){
		
		List< String > ethnicities     = Ethnicity.getEthnicities();
		List< String > fitnessRoutines = Fitness  .getFitnessRoutines();
		if( ethnicities == null ){
			
			//call the rest api here
			EthnicityListTask elt = new EthnicityListTask( BlueMobileApp.getEthnicitiesListURL( getApplicationContext() ) );
	        elt.setReceiver( this );
	        elt.execute( "" );
		}
		else{
			
			Spinner ethnicitySpinner = (Spinner) findViewById( R.id.sp_Ethnicity );
			setSpinnerAdapter( ethnicities, ethnicitySpinner );
		}
		if( fitnessRoutines == null ){
			
			//call the rest api here
			FitnessRoutineListTask frlt = new FitnessRoutineListTask( BlueMobileApp.getFitnessListURL( getApplicationContext() ) );
	        frlt.setReceiver( this );
	        frlt.execute( "" );
		}
		else{
			
			Spinner fitnessSpinner = (Spinner) findViewById( R.id.sp_Fitness );
			setSpinnerAdapter( fitnessRoutines, fitnessSpinner );
		}
		
	}
	
	private void setSpinnerAdapter( List< String > data, Spinner sp ){
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sp.setAdapter( adapter );
	}

	//call the update service asynchronously
	public void updateUser(){
		String updateUserProfile="updateUserProfile";
		_pDialog.setTitle(_profileUpdateLoading);

		_pDialog.show();
		
		Button btUpdate = (Button) findViewById(R.id.bt_Register);
        btUpdate.setEnabled( false );
        
        EditText etUserID = (EditText) findViewById(R.id.et_UserID);
        
		EditText et_Password   = (EditText) findViewById( R.id.et_Password    );
        EditText et_Name       = (EditText) findViewById( R.id.et_Name        );
        EditText et_Zip        = (EditText) findViewById( R.id.et_Zip         );
        EditText et_Age        = (EditText) findViewById( R.id.et_Age         );
        EditText et_Profession = (EditText) findViewById( R.id.et_Profession  );
        EditText et_Interest   = (EditText) findViewById( R.id.et_Interests   );
        Spinner  sp_Ethnicity  = (Spinner)  findViewById( R.id.sp_Ethnicity   );
        Spinner  sp_Fitness    = (Spinner)  findViewById( R.id.sp_Fitness     );
        
        
        String twitterID  = String.valueOf( etUserID.getText() );
        String password   = String.valueOf( et_Password.getText()       );
        String name       = String.valueOf( et_Name.getText()           );
        String zip        = String.valueOf( et_Zip.getText()            );
        String interest   = String.valueOf( et_Interest.getText()       );
        String age        = String.valueOf( et_Age.getText()            );
        String profession = String.valueOf( et_Profession.getText()     );
        String ethnicity  = (String) sp_Ethnicity.getSelectedItem()     ;
        String fitness    = (String) sp_Fitness.getSelectedItem()       ;

		//execute the task asynchronously
        UpdateProfileTask upt = new UpdateProfileTask( BlueMobileApp.getUpdateUserProfileURI( getApplicationContext() ) );
        upt.setReceiver( this );
        upt.execute( twitterID, password, name, age, zip, profession, interest, ethnicity, fitness );
        
        
	}

	//do something when the asynchronous task is complete
	//NOTE: there are four asynchronous tasks in the activity
	@Override
	public void receiveResult(String response, String source) {

		//check if the completed task is the retreival of the profile
		if( source.equals( RetrieveProfileTask._RETRIEVE_PROFILE_TASK_ID ) ){
        	
			_pDialog.dismiss();
			Log.i("Retreive Status", response );
			
			if( response.equals( "Failed" ) ){
    			
    			Toast.makeText( this, _noProfile, Toast.LENGTH_LONG ).show();
    			
		        
			}
			
			else{
				Log.i("Retreive Status" , "Successful " );
				
				JSONObject profile;
				try {
					profile = new JSONObject( response );
					
					String userID           = profile.getString( UserProfile._userIDKey    );
					String password         = profile.getString( UserProfile._passwordKey  );
					String name             = profile.getString( UserProfile._nameKey      );
					String age              = profile.getString( UserProfile._ageKey       );
					String zip              = profile.getString( UserProfile._zipKey       );
					String profession       = profile.getString( UserProfile._professionKey);
					String interest         = profile.getString( UserProfile._interestsKey );
					String ethnicity        = profile.getString( UserProfile._ethnicityKey );
					String fitness          = profile.getString( UserProfile._fitnessKey   );
					
					EditText et_Password   = (EditText) findViewById( R.id.et_Password    );
			        EditText et_Name       = (EditText) findViewById( R.id.et_Name        );
			        EditText et_Zip        = (EditText) findViewById( R.id.et_Zip         );
			        EditText et_Age        = (EditText) findViewById( R.id.et_Age         );
			        EditText et_Profession = (EditText) findViewById( R.id.et_Profession  );
			        EditText et_Interest   = (EditText) findViewById( R.id.et_Interests   );
			        Spinner  sp_Ethnicity  = (Spinner)  findViewById( R.id.sp_Ethnicity   );
			        Spinner  sp_Fitness    = (Spinner)  findViewById( R.id.sp_Fitness     );
			        
			        et_Password  .setText( password   );
			        et_Name      .setText( name       );
			        et_Age       .setText( age        );
			        et_Zip       .setText( zip        );
			        et_Profession.setText( profession );
			        et_Interest  .setText( interest   );
			        
			        ArrayAdapter<CharSequence> ethnicityAdapter = (ArrayAdapter<CharSequence>) sp_Ethnicity.getAdapter();
			        if( !ethnicity.equals( null ) ){
			        	int spinnerPosition = ethnicityAdapter.getPosition( ethnicity );
			        	sp_Ethnicity.setSelection( spinnerPosition );
			        	spinnerPosition = 0;
			        }
			        
			        ArrayAdapter<CharSequence> fitnessAdapter = (ArrayAdapter<CharSequence>) sp_Fitness.getAdapter();
			        if( !fitness.equals( null ) ){
			        	int spinnerPosition = fitnessAdapter.getPosition( fitness );
			        	sp_Fitness.setSelection( spinnerPosition );
			        	spinnerPosition = 0;
			        }
					
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText( this, "Your profile could not be retreived", Toast.LENGTH_LONG ).show();
				}
				
				
				
			
			}
			
			Button btUpdate = (Button) findViewById(R.id.bt_Register);
	        btUpdate.setEnabled( true );
			
		}

		//check if the completed task is the update of the profile
		if( source.equals( UpdateProfileTask._UPDATE_PROFILE_TASK_ID ) ){
        	
			_pDialog.dismiss();
			Log.i("Update Status", response );
			
			if( response.equals( "Successful" ) ){
    			Log.i("Update Status", response );
    			
    			Toast.makeText( this, _profileUpdate, Toast.LENGTH_LONG ).show();
			}
			
			else{
				Log.i("Registration Status" , "Failed " );
    			Toast.makeText( this, _profileUpdateError, Toast.LENGTH_LONG ).show();
			}
			
			Button btUpdate = (Button) findViewById(R.id.bt_Register);
	        btUpdate.setEnabled( true );
			
		}

		//check if the completed task is a ethnicity list retrieval task
		if( source.equals( EthnicityListTask._ETHNICITY_RETRIEVE_TASK_ID ) ){
			
			if( !response.equals( "Failed") ){
				
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

	private void readProperties(){

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
		this._profileRetrieve      =  props.getProperty( _PROFILE_RETRIEVE_PROP   );
		this._noProfile            =  props.getProperty( _NO_PROFILE_PROP         );
		this._profileUpdate        =  props.getProperty( _PROFILE_UPDATE_SUCCESS  );
		this._profileUpdateError   =  props.getProperty( _PROFILE_UPDATE_ERROR    );
		this._profileUpdateLoading =  props.getProperty( _PROFILE_UPDATE_LOCADING );
	}
}
