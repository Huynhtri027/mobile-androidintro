package com.ibm.startkit.android.bluemix;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

/*

Class to call the server side Search REST API
and retrieve the tips for the supplied question
 */


import com.ibm.android.asynctask.AsyncServiceTask;

public class SearchTipsTask  extends AsyncServiceTask {

	public static String _SEARCH_TIPS_ID = "Search Tips";
	
	private String _searchTipsURL;
	
	public SearchTipsTask( String searchURL ){

		//call the base class' constructor to assign a String
		//ID for this task
		super(_SEARCH_TIPS_ID);
		
		_searchTipsURL = searchURL;
		
		
	}
	
	@Override
	protected String performTask(String... params) throws IOException,
			JSONException, URISyntaxException {

		//TODO: call the server's searchtips REST API
		return null;
	}

}
