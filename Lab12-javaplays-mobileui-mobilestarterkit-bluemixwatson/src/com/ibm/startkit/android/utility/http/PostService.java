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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class PostService {
	
	private HttpClient _httpClient;
	
	public PostService( HttpParams params ){
		
		if( params == null ){
			
			_httpClient = new DefaultHttpClient();
		}
		else{
			
			_httpClient = new DefaultHttpClient( params );
		}
	}
	
	public String performPost( String url, String data ) throws URISyntaxException, ClientProtocolException, IOException{
		
		URI uri     = new URI( url ).normalize();
		HttpPost httpPost = new HttpPost( uri );
		
		
		if( data != null ){
			StringEntity se = new StringEntity( data.toString(), HTTP.UTF_8  );
			se.setContentType( new BasicHeader( HTTP.CONTENT_TYPE, "application/json" ) );
			
			httpPost.setEntity( se  );
		}
		
		HttpResponse response = _httpClient.execute( httpPost );
		
		return EntityUtils.toString( response.getEntity() );
	}
	
	public String performPost( String url, String data, String authString ) throws URISyntaxException, ClientProtocolException, IOException{
		
		URI uri     = new URI( url ).normalize();
		HttpPost httpPost = new HttpPost( uri );
		
		if( authString != null ){
			httpPost.setHeader( "Authorization", authString );
		}
		
		if( data != null ){
			StringEntity se = new StringEntity( data.toString(), HTTP.UTF_8  );
			se.setContentType( new BasicHeader( HTTP.CONTENT_TYPE, "application/json" ) );
			
			httpPost.setEntity( se  );
		}
		
		HttpResponse response = _httpClient.execute( httpPost );
		
		return EntityUtils.toString( response.getEntity() );
	}

}
