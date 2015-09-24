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

package com.ibm.startkit.android.utility.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class GetService {
	
	private HttpClient _httpClient;
	
	public GetService( HttpParams params ){
		
		if( params == null ){
			
			_httpClient = new DefaultHttpClient();
		}
		else{
			
			_httpClient = new DefaultHttpClient( params );
		}
	}
	
	public String performGet( String url ) throws URISyntaxException, ClientProtocolException, IOException{
		
		URI uri     = new URI( url ).normalize();
		HttpGet httpGet = new HttpGet( uri );
		
		
		HttpResponse response = _httpClient.execute( httpGet );
		
		return EntityUtils.toString( response.getEntity() );
	}
	
	public String performGet( String url, String authString ) throws URISyntaxException, ClientProtocolException, IOException{
		
		URI uri     = new URI( url ).normalize();
		HttpGet httpGet = new HttpGet( uri );
		
		if( authString != null ){
			httpGet.setHeader( "Authorization", authString );
		}
		
		
		
		HttpResponse response = _httpClient.execute( httpGet );
		
		return EntityUtils.toString( response.getEntity() );
	}
}
