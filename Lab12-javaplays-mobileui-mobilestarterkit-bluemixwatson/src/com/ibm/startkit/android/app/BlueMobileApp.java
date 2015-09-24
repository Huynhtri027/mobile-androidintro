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

package com.ibm.startkit.android.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;



public final class BlueMobileApp extends Application {

	//keys in the property file to read various URIs

	//to read the base uri
	public static String _BASEURI_PROP   = "baseuri";
	//to read the login uri
	public static String _LOGINURI_PROP  = "loginuri";

	//to read the search uri
	public static String _SEARCHURI_PROP = "searchuri";

	//to read the ethnicities list uri
	public static String _ETHNICITIES_PROP = "ethnicitielistsuri";

	//to read the fitness list uri
	public static String _FITNESS_PROP = "fitnesslisturi";

	//to read the register uri
	public static String _REGISTER_PROP = "registeruri";

	//to read the user profile uri
	public static String _USR_PROFILE_PROP = "userprofileuri";

	//to read the update user profile uri
	public static String _USR_PROFILE_UPDATE_PROP = "updateuserprofileuri";


	//static variables to store the URIs
	public static String _bluemixURL         = null;
	public static String _loginURL           = null;
	public static String _searchURL          = null;
	public static String _registerURL        = null;
	public static String _usrProfileURL      = null;
	public static String _updateProfileURL   = null;
	public static String _ethnicitiesListURL = null;
	public static String _fitnessListURL     = null;


	public static String _SEARCH_RESULT_MSG = "com.ibm.au.cognitivecare.searchresult";
	public static String _KEYWORD_MSG       = "com.ibm.au.cognitivecare.originalkeyword";



	public static final String PROPS_FILE = "starterkit.properties";

	private static final String CLASS_NAME = BlueMobileApp.class.getSimpleName();

	private static String _userID = null;



	public BlueMobileApp() {
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity,
										  Bundle savedInstanceState) {
				Log.d(CLASS_NAME,
						"Activity created: " + activity.getLocalClassName());
			}

			@Override
			public void onActivityStarted(Activity activity) {
				Log.d(CLASS_NAME,
						"Activity started: " + activity.getLocalClassName());
			}

			@Override
			public void onActivityResumed(Activity activity) {
				Log.d(CLASS_NAME,
						"Activity resumed: " + activity.getLocalClassName());
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity,
													Bundle outState) {
				Log.d(CLASS_NAME,
						"Activity saved instance state: "
								+ activity.getLocalClassName());
			}

			@Override
			public void onActivityPaused(Activity activity) {
				Log.d(CLASS_NAME,
						"Activity paused: " + activity.getLocalClassName());
			}

			@Override
			public void onActivityStopped(Activity activity) {
				Log.d(CLASS_NAME,
						"Activity stopped: " + activity.getLocalClassName());
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				Log.d(CLASS_NAME,
						"Activity destroyed: " + activity.getLocalClassName());
			}
		});
	}

	@Override
	public void onCreate() {
		super.onCreate();


	}

	//simple logout function
	//set the userID to null
	public static void logout()
	{
		setUserID(null);
	}

	//setting the userID
	public static void setUserID( String userID ){

		_userID = userID;
	}

	//getting the userID
	public static String getUserID(){

		return _userID;
	}

	//check if any user is logged in
	public static boolean isLoggedIn(){

		if( _userID == null ){
			return false;
		}
		return true;

	}


	//retrieve various server side URLs from the properties file
	public static void readServiceURL(Context context) {


		Properties props = new java.util.Properties();
		try {

			//get the asset manager of the application to access the
			//properties file from the asset
			AssetManager assetManager = context.getAssets();
			props.load(assetManager.open(PROPS_FILE));

			//some helpful message
			System.out.println("Inside Properties File value for the URL is" + props.getProperty("baseuri"));
			Log.i(CLASS_NAME, "Found configuration file: " + PROPS_FILE);

			//read the base URL of the server side
			//this is the java app URL on Bluemix
			BlueMobileApp._bluemixURL = props.getProperty(BlueMobileApp._BASEURI_PROP);

			//read the REST API location for login task
			BlueMobileApp._loginURL = BlueMobileApp._bluemixURL.concat(props.getProperty(BlueMobileApp._LOGINURI_PROP));


			//read the REST API location for the search task
			BlueMobileApp._searchURL = BlueMobileApp._bluemixURL.concat( props.getProperty( BlueMobileApp._SEARCHURI_PROP ) );


			//read the REST API location for the register task
			BlueMobileApp._registerURL = BlueMobileApp._bluemixURL.concat( props.getProperty( BlueMobileApp._REGISTER_PROP ) );

			//read the REST API location for the update profile task
			BlueMobileApp._usrProfileURL = BlueMobileApp._bluemixURL.concat( props.getProperty( BlueMobileApp._USR_PROFILE_PROP ) );


			//read the REST API location for the update profile task
			BlueMobileApp._updateProfileURL = BlueMobileApp._bluemixURL.concat( props.getProperty( BlueMobileApp._USR_PROFILE_UPDATE_PROP ) );

			//read the REST API location for the ethnicities list
			BlueMobileApp._ethnicitiesListURL = BlueMobileApp._bluemixURL.concat( props.getProperty( BlueMobileApp._ETHNICITIES_PROP ) );

			//read the REST API location for the fitness list
			BlueMobileApp._fitnessListURL = BlueMobileApp._bluemixURL.concat( props.getProperty( BlueMobileApp._FITNESS_PROP ) );

		}
		catch (FileNotFoundException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file was not found.", e);
		}
		catch (IOException e) {
			Log.e(CLASS_NAME, "The starterkit.properties file could not be read properly.", e);
		}
	}

	public static String getLoginURI( Context context ){

		if( BlueMobileApp._bluemixURL == null || BlueMobileApp._loginURL == null ){

			BlueMobileApp.readServiceURL( context );
		}

		return BlueMobileApp._loginURL;
	}


	public static String getSearchURI( Context context ){

		if( BlueMobileApp._bluemixURL == null || BlueMobileApp._searchURL == null ){

			BlueMobileApp.readServiceURL( context );
		}

		return BlueMobileApp._searchURL;
	}

	public static String getRegisterURI( Context context ){

		if( BlueMobileApp._bluemixURL == null || BlueMobileApp._registerURL == null ){

			BlueMobileApp.readServiceURL( context );
		}

		return BlueMobileApp._registerURL;
	}

	public static String getUserProfileURI( Context context ){

		if( BlueMobileApp._bluemixURL == null || BlueMobileApp._usrProfileURL == null ){

			BlueMobileApp.readServiceURL( context );
		}

		return BlueMobileApp._usrProfileURL;
	}

	public static String getUpdateUserProfileURI( Context context ){

		if( BlueMobileApp._bluemixURL == null || BlueMobileApp._updateProfileURL == null ){

			BlueMobileApp.readServiceURL( context );
		}

		return BlueMobileApp._updateProfileURL;
	}

	public static String getEthnicitiesListURL( Context context ){

		if( BlueMobileApp._bluemixURL == null || BlueMobileApp._ethnicitiesListURL == null ){

			BlueMobileApp.readServiceURL( context );
		}

		return BlueMobileApp._ethnicitiesListURL;
	}

	public static String getFitnessListURL( Context context ){

		if( BlueMobileApp._bluemixURL == null || BlueMobileApp._fitnessListURL == null ){

			BlueMobileApp.readServiceURL( context );
		}

		return BlueMobileApp._fitnessListURL;
	}

}