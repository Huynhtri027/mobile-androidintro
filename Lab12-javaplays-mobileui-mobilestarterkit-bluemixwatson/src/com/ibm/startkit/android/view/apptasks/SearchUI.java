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

Facilitates search process
Calls the search service asynchronously
 */


public class SearchUI extends BaseView implements TaskReceiver {

	public static String _SEARCH_MSG_PROP = "searchmessage";
	public static String _SEARCH_NO_RES_PROP = "searchnoresult";
	public static String _SEARCH_LOADING_PROP = "searchloading";

	private String _seeds;
	private static final String CLASS_NAME = SearchUI.class.getSimpleName();
	public static String _searchMessage = null;
	public static String _searchLoading = null;
	public static String _searchNoResults = null;

	ProgressDialog _pDialog;

	@Override
	/**
	 * onCreate called when main activity is created.
	 *
	 * Sets up the itemList, application, and sets listeners.
	 *
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//read some values from the properties file
		readProperties();

		//set the searchui as the view for this actvity
		setContentView(R.layout.searchui);

		_pDialog = new ProgressDialog(this);

		setupUIComponent();

	}

	//make the error text button invisible and assign the buttons
	//click listener
	private void setupUIComponent() {


		TextView txtError = (TextView) findViewById(R.id.errorLabel);
		txtError.setVisibility(TextView.INVISIBLE);

		Button bt = (Button) findViewById(R.id.searchButton);

		bt.setOnClickListener(new View.OnClickListener() {

			  @Override
			  public void onClick(View v) {


				  doSearch();
			  }
		  }

		);
	}


	//execute the search task asynchronously
	public void doSearch(){

		String searchURL="searchtips";
		TextView txtError = (TextView) findViewById( R.id.errorLabel );
		txtError.setVisibility( TextView.INVISIBLE );
		
		_pDialog.setCancelable(false);
		_pDialog.setTitle(_searchLoading);
		_pDialog.setMessage(_searchMessage);
		_pDialog.show();

		EditText et = ( EditText ) findViewById( R.id.inputKeyword );
		Button   bt = ( Button )   findViewById( R.id.searchButton );
		_seeds = et.getText().toString();
		
		SearchTipsTask stt = new SearchTipsTask( BlueMobileApp.getSearchURI( getApplicationContext() ) );
		stt.setReceiver(this);
		stt.execute(_seeds);
	
		
		bt.setEnabled(false);
	}

	//do something when the search is complete
	@Override
	public void receiveResult( String response, String source ) {
		
		_pDialog.dismiss();
		TextView txtError = (TextView) findViewById( R.id.errorLabel );
		
		Button   bt = ( Button )   findViewById( R.id.searchButton );
		bt.setEnabled( true );

		//make sure the task ID is the search task
		if( source.equals( SearchTipsTask._SEARCH_TIPS_ID) ){

			//if there is anything wrong
			if( response.isEmpty() || response == null || response.equals( "Failed") ){
				txtError.setVisibility( TextView.VISIBLE );
				txtError.setText(_searchNoResults);
				return;
			}
			//if the response has some tips
			else{
				Log.i( "Response", response );
				txtError.setText( "" );
				txtError.setVisibility( TextView.INVISIBLE );

				//initiate the output UI
				Intent responseIntent = new Intent( SearchUI.this, OutputUI.class );

				//add some variables to be passed to the output UI
				//first the response received
				responseIntent.putExtra( BlueMobileApp._SEARCH_RESULT_MSG, response );
				//and then the original keyword of the search
				responseIntent.putExtra( BlueMobileApp._KEYWORD_MSG, _seeds   );

				//start the activity
				startActivity( responseIntent );
			}
		}

	}

	private void readProperties(){

		Context context = getApplicationContext();
		Properties props = new java.util.Properties();
		try {
			AssetManager assetManager = context.getAssets();
			props.load(assetManager.open( BlueMobileApp.PROPS_FILE));

			System.out.println("Inside Properties File value for the URL is" + props.getProperty("baseuri"));
			Log.i(CLASS_NAME, "Found configuration file: " + BlueMobileApp.PROPS_FILE);
		} catch (FileNotFoundException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file was not found.", e);
		} catch (IOException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file could not be read properly.", e);
		}
		this._searchMessage   =  props.getProperty( _SEARCH_MSG_PROP    );
		this._searchNoResults =  props.getProperty( _SEARCH_NO_RES_PROP );
		this._searchLoading   =  props.getProperty( _SEARCH_LOADING_PROP);



	}
	
}
