package com.ibm.startkit.android.bluemix;

import java.io.IOException;
import java.net.URISyntaxException;



import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ibm.android.asynctask.AsyncServiceTask;

public class SearchTipsTask  extends AsyncServiceTask {

	public static String _SEARCH_TIPS_ID = "Search Tips";
	
	private String _searchTipsURL;
	
	public SearchTipsTask( String serviceURL ){
		
		super(_SEARCH_TIPS_ID);
		
		_searchTipsURL = serviceURL;
		
		
	}
	
	@Override
	protected String performTask(String... params) throws IOException,
			JSONException, URISyntaxException {
		
		String keyword = params[0];
		
		JSONObject searchParams = new JSONObject();
		searchParams.put( "keyword" , keyword );
		
		Log.i( "Keyword", searchParams.toString() );
		
		//return "Successful";
		
		return performPost( _searchTipsURL , searchParams.toString(), null, null );

	}

}
