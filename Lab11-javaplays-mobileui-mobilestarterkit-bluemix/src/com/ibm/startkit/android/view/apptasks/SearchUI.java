package com.ibm.startkit.android.view.apptasks;

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


import com.ibm.startkit.android.bluemix.SearchTipsTask;
import com.ibm.android.asynctask.TaskReceiver;
import com.ibm.startkit.android.app.BlueMobileApp;
import com.ibm.startkit.android.app.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/*

	UI for the user to search tips for some keywords
	This extends the BaseView class
	implements the TaskReceiver interface to respond
	to the SearchTipsTask

 */

public class SearchUI extends BaseView implements TaskReceiver  {

	//key to read from the properties file; these have to match exactly as
	//in starterkit.properties file
	public static String _SEARCH_MSG_PROP     = "searchmessage"  ;
	public static String _SEARCH_MSG_NORESULT = "searchnoresults";
	public static String _SEARCH_MSG_LOADING  = "searchloading"  ;

	private static final String CLASS_NAME = SearchUI.class.getSimpleName();

    public static String _searchMessage   = null;
    public static String _searchLoading   = null;
    public static String _searchNoResults = null;

    ProgressDialog _pDialog;
	private String _seeds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate( savedInstanceState );

		//read some values from the properties file
		readProperties();

		_pDialog = new ProgressDialog(this);

		//sets the searchui.xml as its UI
		setContentView( R.layout.searchui );

		setupUIComponents();
	}

	private void setupUIComponents(){

		//TODO: make the error text box invisible and
		//setup the button's click event
		//retrieve the error textview and set it invisible
		TextView txtError = (TextView) findViewById( R.id.errorLabel );
		txtError.setVisibility(TextView.INVISIBLE);

		//retrieve the search button from the UI
		Button bt = ( Button ) findViewById( R.id.searchButton );

		//and set its click listener to execute the search Task
		bt.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doSearch();
			}
		});


		return;
	}

	/*
		Makes an Asynchronous call to the SearchTipsTask to find tips
	 */
	public void doSearch(){

		//TODO: execute the search task asynchronously
		TextView txtError = (TextView) findViewById( R.id.errorLabel );
		txtError.setVisibility(TextView.INVISIBLE);

		_pDialog.setCancelable(false);
		_pDialog.setTitle(_searchLoading);
		_pDialog.setMessage(_searchMessage);
		_pDialog.show();

		//retrieve the user input
		EditText et = ( EditText ) findViewById( R.id.inputKeyword );
		_seeds = et.getText().toString();

		//Steps to make the call to an Aysnchronous task
		// 1. Create the task (the task class inherits the AsyncServiceTask class)
		SearchTipsTask stt = new SearchTipsTask( BlueMobileApp.getSearchURI(getApplicationContext()) );
		// 2. Sets itself as the task's receiver
		stt.setReceiver( this );
		//3. Supply necessary parameters (String) and execute the task
		stt.execute(_seeds);

		Button   bt = ( Button )   findViewById( R.id.searchButton );
		bt.setEnabled(false);
		return;
	}

	/*
    Implemented function of the TaskReceiver interface
    This is executed once the SearchTipsTask is completed.
 	*/
	@Override
	public void receiveResult( String response, String source ) {
		//TODO: do something when the search response is available
		_pDialog.dismiss();
		TextView txtError = (TextView) findViewById( R.id.errorLabel );

		Button   bt = ( Button )   findViewById( R.id.searchButton );
		bt.setEnabled( true );

		//first verify that the task completed is indeed the SearchTipsTask
		if( source.equals( SearchTipsTask._SEARCH_TIPS_ID ) ){

			//if the task is not successful
			if( response.isEmpty() || response == null || response.equals( "Failed" ) ){
				txtError.setVisibility( TextView.VISIBLE );
				txtError.setText( _searchNoResults );
				return;
			}
			//if it is successful
			else{

				Log.i( "Response", response );
				txtError.setText( "" );
				txtError.setVisibility( TextView.INVISIBLE );

				//creat a new intent with the OutputUI
				Intent responseIntent = new Intent( SearchUI.this, OutputUI.class );
				//add some message for the OutputUI
				//first add what the tips are
				responseIntent.putExtra( BlueMobileApp._SEARCH_RESULT_MSG, response );
				//then add the original keyword
				responseIntent.putExtra(BlueMobileApp._KEYWORD_MSG, _seeds);

				//start the new activity
				startActivity(responseIntent);
			}
		}
		return;

	}

	//read the property file to load some required variables
	private void readProperties(){

		Context context = getApplicationContext();
		Properties props = new java.util.Properties();
		try {

			AssetManager assetManager = context.getAssets();
			props.load(assetManager.open( BlueMobileApp.PROPS_FILE));

			this._searchMessage   = props.getProperty ( SearchUI._SEARCH_MSG_PROP      );
			this._searchNoResults =  props.getProperty( SearchUI._SEARCH_MSG_NORESULT  );
			this._searchLoading   =  props.getProperty( SearchUI._SEARCH_MSG_LOADING   );

		} catch (FileNotFoundException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file was not found.", e);
		} catch (IOException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file could not be read properly.", e);
		}
	}
	
	
}
