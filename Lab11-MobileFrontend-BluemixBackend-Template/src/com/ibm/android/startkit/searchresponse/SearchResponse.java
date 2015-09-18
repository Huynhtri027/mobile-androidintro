package com.ibm.android.startkit.searchresponse;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class SearchResponse {
	
	
	public static String _questionIdentifier       = "question";
	public static String _answerTextIdentifier     = "text";
	public static String _answerConfIdentifier     = "confidence";
	public static String _answerListIdentifier     = "answers";

	
	
	public static String _quesansListIdentifier = "qa";
	
	
	ArrayList<QuestionAnswer> questionAnswerList;
	
	public SearchResponse()
	{
		questionAnswerList = new ArrayList< QuestionAnswer >();
	}
	
	public ArrayList< QuestionAnswer > getQAList(){
		return questionAnswerList;
	}
	
	
	
	public void addQA( QuestionAnswer qa ){
		questionAnswerList.add( qa );
	}
	
	

	public static SearchResponse formatResponse( String responseJSON, String keyword ) throws JSONException{
		
		SearchResponse mr = new SearchResponse();
		
		JSONObject responseJSONObj = new JSONObject( responseJSON );
		
		JSONArray qaArray     = responseJSONObj.getJSONArray(_quesansListIdentifier);
		
		int qaFound = qaArray.length();
		Log.i( "QAFound ", String.valueOf( qaFound ) );
		
		
		for( int qa = 0 ; qa < qaFound; qa++ ){
			
			QuestionAnswer queans = new QuestionAnswer();
			
			JSONObject qaObject = qaArray.getJSONObject( qa );
			
			String questionString = qaObject.getString( _questionIdentifier );
			
			
			if( questionString.indexOf( "What is" ) >=0  ){
				questionString = "What is \"" + keyword + "\"?";
			}
			
			if(  questionString.indexOf( "What should" ) >=0 ){
				questionString = "What should I know about \"" + keyword + "\"?";
			}
			
			queans.setQuestion( questionString );
			
			JSONArray answerListObject = qaObject.getJSONArray( _answerListIdentifier );
			
			int totalAnswerFound = answerListObject.length();
			
			for( int ans = 0 ; ans < totalAnswerFound ; ans ++  ){
				
				JSONObject answerObject = answerListObject.getJSONObject( ans );
				Answer answer = new Answer();
				
				answer.setAnswer( answerObject.getString   ( _answerTextIdentifier ) );
				queans.addAnswer( answer );
			}
			mr.addQA( queans );
		}
		return mr;
	}	
}