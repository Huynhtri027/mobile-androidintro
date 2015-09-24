package com.ibm.android.startkit.searchresponse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Answer
{
	
	public static String _mainObjectIdentifier   = "question";
	public static String _answerObjectIdentifier = "answers" ;
	
	public static String _answerTextIdentifier         = "text";
	public static String _answerConfidenceIdentifier   = "confidence";
	
	private String _answer;
	private double _relevance;
	
	public Answer()
	{
		
	}
	
	public Answer( String answer, double relevance)
	{
		_answer = answer;
		_relevance = relevance;
		
	}

	public String getAnswer() {
		return _answer;
	}

	public void setAnswer(String answer) {
		this._answer = answer;
	}

	public double getRelevance() {
		return _relevance;
	}

	public void setRelevance(double relevance) {
		this._relevance = relevance;
	}
	
	public String toString(){
		
		return " Answer text : " + _answer + " Relevance " + _relevance;
	}
	
	//method to convert the returned string of the service
	//into a list of answer objects
	public static ArrayList< Answer > formatAnswers( String answer ) throws JSONException{

		ArrayList< Answer > answers = new ArrayList< Answer >();
		
		JSONObject serviceResponseObject = ( ( JSONObject ) ( ( new JSONArray( answer ) ).get( 0 ) ) ).getJSONObject( _mainObjectIdentifier );
		
		JSONArray answerList = ( JSONArray ) serviceResponseObject.getJSONArray( _answerObjectIdentifier );
		
		int totalAnswerReceived = answerList.length();
		
		for( int i = 0 ; i < totalAnswerReceived; i++ ){
			
			JSONObject ansObject = ( JSONObject ) answerList.get( i );
			Answer answerObject = new Answer( ansObject.getString( Answer._answerTextIdentifier       ),
					                                      ansObject.getDouble( Answer._answerConfidenceIdentifier ) );			
			answers.add( answerObject );
			
		}
		
		return answers;
	}
	
	

}

