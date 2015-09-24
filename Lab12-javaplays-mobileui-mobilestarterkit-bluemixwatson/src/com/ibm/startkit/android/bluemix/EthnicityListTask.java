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

import com.ibm.android.asynctask.AsyncServiceTask;
import com.ibm.startkit.android.app.BlueMobileApp;

public class EthnicityListTask extends AsyncServiceTask{
	
	public static String _ETHNICITY_RETRIEVE_TASK_ID = "Retreive ethnicity list";
	
	private String _ethnicityURL ;
	
	
	public EthnicityListTask( String serviceURL ){
		
		super( _ETHNICITY_RETRIEVE_TASK_ID );
		
		_ethnicityURL = serviceURL;
		
	}

	@Override
	protected String performTask(String... params) throws IOException,
			JSONException, URISyntaxException {
		
		return performPost( _ethnicityURL, null, null, null );
	}

}
