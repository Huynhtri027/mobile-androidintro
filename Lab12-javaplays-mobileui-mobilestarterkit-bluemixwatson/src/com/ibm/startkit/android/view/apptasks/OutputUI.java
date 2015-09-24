package com.ibm.startkit.android.view.apptasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.ibm.android.startkit.searchresponse.Answer;
import com.ibm.android.startkit.searchresponse.MatchResponse;
import com.ibm.android.startkit.searchresponse.QuestionAnswer;
import com.ibm.startkit.android.adapters.TipsExpandableListBaseAdapter;
import com.ibm.startkit.android.app.R;
import com.ibm.startkit.android.app.BlueMobileApp;

/*

Displays the Question and the relevant answers here

 */

public class OutputUI extends BaseView   {
	
	MatchResponse matchResponse;
	
	TipsExpandableListBaseAdapter listAdapterTips;
    ExpandableListView expListViewTips;
    List<String> listDataHeaderTips;
    HashMap<String, List<String>> listDataChildTips;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView( R.layout.outputui );

		//2 parameters were passed to this activity, retrieve those here
		Intent intent = getIntent();
		String searchResult = intent.getStringExtra( BlueMobileApp._SEARCH_RESULT_MSG );
		String seeds        = intent.getStringExtra( BlueMobileApp._KEYWORD_MSG       );
		
		Log.i("Response in output",searchResult);

		try{
			matchResponse = MatchResponse.formatResponse( searchResult,seeds );
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		expListViewTips = ( ExpandableListView ) findViewById( R.id.lv_Tips );

				
		expListViewTips.setVisibility(View.VISIBLE);
		showTips();
		
		
	}
		 
	public void showTips(){
		
		try {			
	
			ArrayList<QuestionAnswer> results = GetTipsResults();
			
			if (results.size()==0)
			{
				
			}
			else
			{
				prepareTipsListData(results);
				listAdapterTips = new TipsExpandableListBaseAdapter(this, listDataHeaderTips, listDataChildTips);
				expListViewTips.setAdapter(listAdapterTips);
				
				expListViewTips
	            .setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
	
	                @Override
	                public boolean onChildClick(
	                        ExpandableListView parent, View v,
	                        int groupPosition, int childPosition,
	                        long id) {
	                	String answer = listDataChildTips.get(listDataHeaderTips.get(groupPosition)).get(childPosition).toString();
	                	Toast.makeText(OutputUI.this, answer, Toast.LENGTH_LONG).show();	
	                    return true;
	                }
	            });
			}
		} catch (Exception e) {
			Toast.makeText(OutputUI.this, "Try searching again", Toast.LENGTH_LONG).show();
		}

	}
	
	private ArrayList<QuestionAnswer> GetTipsResults(){
	     
		 ArrayList<QuestionAnswer> results = matchResponse.getQAList();
	     return results;
	} 
	
	private void prepareTipsListData(ArrayList<QuestionAnswer> results) {
		listDataHeaderTips = new ArrayList<String>();
		listDataChildTips = new HashMap<String, List<String>>();
 
        int count = 0;
        for (QuestionAnswer result:results)
        {
        	listDataHeaderTips.add(result.getQuestion().toString());
        	List<String> answers = new ArrayList<String>();
        	for (Answer answer:result.getAnswerList())
        	{
        		answers.add(answer.getAnswer());
        	}
        	listDataChildTips.put(listDataHeaderTips.get(count), answers); 
        	count++;
        }
    }
}
