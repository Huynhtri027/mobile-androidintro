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
package com.ibm.startkit.android.view.apptasks;

import com.ibm.startkit.android.app.R;
import com.ibm.startkit.android.app.BlueMobileApp;
import com.ibm.startkit.android.view.LoginUI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/*

Base view class for all the activities within the application
Only a valid user (successful login and/or registration) will
let user into these activities. These activities have a menu
options


 */

public class BaseView extends Activity{

	ProgressDialog pDialog = null;
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

		//uses the applications menu from res->menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionmenu, menu);
		return true;
    }

	@Override
	public boolean onOptionsItemSelected( MenuItem item ){

		//take necessary steps when a menu item is clicked

		switch( item.getItemId() ){
		
			case R.id.action_updateprofile:
				
				navigateToUpdate();
				return true;
				
			case R.id.action_logout:
				
				BlueMobileApp.logout();
				navigateToLogin();
				return true;
			
			case R.id.action_search:
				
				searchTips();
				return true;
				
			
			default:
				return super.onOptionsItemSelected( item );
			
		}
	}

	
	private void searchTips(){
		
		Intent newIntent = new Intent( getApplicationContext(), SearchUI.class );
		startActivity( newIntent );
		
	}
	
	private void navigateToUpdate(){
		
		Intent newIntent = new Intent( getApplicationContext(), UpdateProfileUI.class );
		startActivity( newIntent );
		
	}
	
	private void navigateToLogin(){
		
		if( !BlueMobileApp.isLoggedIn() ){
			Intent intent = new Intent( getApplicationContext(),LoginUI.class );
			intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
	        startActivity(intent);
			finish();
		}
	}
	
}
