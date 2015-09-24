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

import android.util.Log;

import com.ibm.android.asynctask.AsyncServiceTask;
import com.ibm.startkit.android.entities.UserProfile;

/*

Calls the REST API : register
 */

public class RegisterTask  extends AsyncServiceTask {
	
	public static String _REG_TASK_ID = "Register Patient";
	
	private String _registerURL;
	
	public RegisterTask( String serviceURL ){
		
		super( _REG_TASK_ID );
		
		_registerURL = serviceURL;
	}

	//retrieve the data from input, prepare a JSON object
	//and calls POST method
	@Override
	protected String performTask(String... params) throws IOException,
			JSONException, URISyntaxException {
		
		String userID    = params[0];
		String password  = params[1];
		String name      = params[2];
		String ageString = params[3];
		String zip       = params[4];
		String profession= params[5];
		String interest  = params[6];
		String ethnicity = params[7];
		String fitness   = params[8];
		
		int age = Integer.parseInt( ageString );
		
		JSONObject profile = new JSONObject();
		
		
		
		profile.put( UserProfile._userIDKey     , userID     );
		profile.put( UserProfile._passwordKey   , password   );
		profile.put( UserProfile._nameKey       , name       );
		profile.put( UserProfile._ageKey        , age        );
		profile.put( UserProfile._zipKey        , zip        );
		profile.put( UserProfile._professionKey , profession );
		profile.put( UserProfile._interestsKey  , interest   );
		profile.put( UserProfile._ethnicityKey  , ethnicity  );
		profile.put( UserProfile._fitnessKey    , fitness    );
		
		Log.d( "Registration info", profile.toString() );
		
		return performPost( _registerURL, profile.toString(), null, null );

	}

}
