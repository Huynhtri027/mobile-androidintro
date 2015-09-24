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
package com.ibm.startkit.android.bluemix;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;


import com.ibm.android.asynctask.AsyncServiceTask;
import com.ibm.startkit.android.entities.UserProfile;

public class RetrieveProfileTask extends AsyncServiceTask{
	
public static String _RETRIEVE_PROFILE_TASK_ID = "Retrive profile";
	
	private String _retreiveProfileURL;
	
	public RetrieveProfileTask( String serviceURL ){
		
		super( _RETRIEVE_PROFILE_TASK_ID );
		
		_retreiveProfileURL = serviceURL;
	}
	
	
	@Override
	protected String performTask(String... params) throws IOException,
			JSONException, URISyntaxException {
		
		String userID  = params[0];
		
		
		JSONObject userIDObj = new JSONObject();
		userIDObj.put( UserProfile._userIDKey  , userID  );
		
		
		return performPost( _retreiveProfileURL, userIDObj.toString(), null, null );
		
		
	}

}
