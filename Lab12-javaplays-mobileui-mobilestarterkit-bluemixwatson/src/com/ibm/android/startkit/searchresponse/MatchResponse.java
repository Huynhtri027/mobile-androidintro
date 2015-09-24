package com.ibm.android.startkit.searchresponse;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class MatchResponse {
	
	
	public static String _questionIdentifier       = "question";
	public static String _answerTextIdentifier     = "text";
	public static String _answerConfIdentifier     = "confidence";
	public static String _answerListIdentifier     = "answers";
	public static String _answerIdentifier         = "answer";
	
	
	public static String _quesansListIdenntifier   = "qa";
	
	
	ArrayList<QuestionAnswer> questionAnswerList;
	
	public MatchResponse() 
	{
		questionAnswerList = new ArrayList< QuestionAnswer >();
	}
	
	//ToDo: Delete this function later
	public static MatchResponse getDummyQA(){
		ArrayList< QuestionAnswer > qa = new ArrayList< QuestionAnswer > ();
		
		QuestionAnswer qa1 = new QuestionAnswer();
		qa1.setQuestion( "What is this crazyness?");
		qa1.addAnswer( new Answer("He he", 0.9) );
		
		QuestionAnswer qa2 = new QuestionAnswer();
		qa2.setQuestion( "Why do I have to be crazy?");
		qa2.addAnswer( new Answer("Good one", 0.8) );
		
		qa.add( qa1 );
		qa.add( qa2 );
		
		MatchResponse ma = new MatchResponse();
		ma.addQA( qa1 );
		ma.addQA( qa2 );
		
		return ma;
	}
	
	public ArrayList< QuestionAnswer > getQAList(){
		return questionAnswerList;
	}
	
	
	
	public void addQA( QuestionAnswer qa ){
		questionAnswerList.add( qa );
	}
	
	
	public String toString(){
		
		String convertedString = "Response Found\n";
		       convertedString += " Doctors in the list\n";
		       
		convertedString += " Tips " + "\n";
		for( QuestionAnswer qa : questionAnswerList ){
			
			convertedString += "  " + qa.getQuestion() + "\n";
			
			for( Answer ans : qa.getAnswerList() ){
				
				convertedString += "   " + ans.getRelevance() + "\n";
				convertedString += "   " + ans.getAnswer() + "\n";
				
			}
		}
		
		return convertedString;
	}
	
	public static MatchResponse formatResponse( String responseJSON, String keyword ) throws JSONException{
		
		MatchResponse mr = new MatchResponse();
		
		JSONObject responseJSONObj = new JSONObject( responseJSON );
		
		JSONArray qaArray     = responseJSONObj.getJSONArray( _quesansListIdenntifier );
		
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
				answer.setRelevance( answerObject.getDouble( _answerConfIdentifier ) );
				
				queans.addAnswer( answer );
			}
			mr.addQA( queans );
		}
		return mr;
	}	
}