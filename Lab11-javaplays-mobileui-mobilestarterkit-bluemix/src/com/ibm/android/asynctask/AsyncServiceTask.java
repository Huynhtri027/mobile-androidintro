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

/*

Abstract class to define tasks that need to be executed Asynchronously

 **** Sets a unique string type id for the task
 **** Sets a receiver of the task once it is completed
 *
 * This class extends the android.os.AsyncTask and defines the generic input
 * and output types with String types.
 */



package com.ibm.android.asynctask;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import com.ibm.startkit.android.utility.http.PostService;

import android.os.AsyncTask;

public abstract class AsyncServiceTask  extends AsyncTask< String, Void, String >{

	private TaskReceiver _tr = null;
	
	private String _response;
	private String _serviceID;

	//abstract function to define the Aysnchronous task
	protected abstract String performTask( String... params ) throws IOException, JSONException, URISyntaxException;

	//Constructor : assigns the task with a string ID
	public AsyncServiceTask( String serviceID ){
		_serviceID = serviceID;
	}
	
	
	@Override
	protected String doInBackground( String... params ) {

		try {
			return performTask( params );
		} catch (IOException e) {

			return null;
		} catch (JSONException e) {

			return null;
		} catch (URISyntaxException e) {
			return null;
		}

	}

	/*
		The onPostExecute() is triggered if the asynchronous task is complete.
		The overridden function responds to the event and executes the
		receivers receiveResult() function
	*/
	@Override
	protected void onPostExecute( String result ){
		_response = result;
		if( _tr != null ){
			_tr.receiveResult( _response, _serviceID );
		}
	}

	//Sets the receiver of the task completion event
	public void setReceiver( TaskReceiver tr ){
		_tr = tr;
	}

	//utility function to perform a POST method
	public String performPost( String url, String data, HttpParams params, String authString ) 
			                   throws ClientProtocolException, URISyntaxException, IOException{
		
		PostService ps = new PostService( params );
		return ps.performPost( url,  data );
	}
	

}
