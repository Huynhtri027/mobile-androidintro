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
import com.ibm.android.startkit.searchresponse.SearchResponse;
import com.ibm.android.startkit.searchresponse.QuestionAnswer;
import com.ibm.startkit.android.adapters.TipsExpandableListBaseAdapter;
import com.ibm.startkit.android.app.R;
import com.ibm.startkit.android.app.BlueMobileApp;

/*

UI to display the tips as an expandable list
 */


public class OutputUI extends BaseView   {
	
	SearchResponse 				  _searchResponse;
	
	TipsExpandableListBaseAdapter _listAdapterTips;
    ExpandableListView            _expListViewTips;
    List<String>                  _listDataHeaderTips;
    HashMap<String, List<String>> _listDataChildTips;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

        //set the content of outputui.xml as the UI
        setContentView( R.layout.outputui );

        /*
        The SearchUI passed some additional parameter to this UI
        Time to retreive those
         */
		Intent intent = getIntent();
		String searchResult = intent.getStringExtra( BlueMobileApp._SEARCH_RESULT_MSG );
		String seeds        = intent.getStringExtra( BlueMobileApp._KEYWORD_MSG       );
		
		try{
            //Format the JSON response into friendly objects
			_searchResponse = SearchResponse.formatResponse(searchResult, seeds);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

        //find the expandable list from the UI and make it visible
		_expListViewTips = ( ExpandableListView ) findViewById( R.id.lv_Tips );
		_expListViewTips.setVisibility(View.VISIBLE);

        //populate the list
		showTips();
		
		
	}

    /*
        populate the list with questions and answers

     */
	public void showTips(){
		
		try {			
	
			ArrayList<QuestionAnswer> results = GetTipsResults();

            //there should be at least one question
			if (results.size()==0)
			{
				
			}
			else
			{
                //prepare the list
                //Each question is a header
                //Each answer to that
				prepareTipsListData(results);
				_listAdapterTips = new TipsExpandableListBaseAdapter(this, _listDataHeaderTips, _listDataChildTips);
				_expListViewTips.setAdapter(_listAdapterTips);
				
				_expListViewTips
	            .setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(
                            ExpandableListView parent, View v,
                            int groupPosition, int childPosition,
                            long id) {
                        String answer = _listDataChildTips.get(_listDataHeaderTips.get(groupPosition)).get(childPosition).toString();
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
	     
		 ArrayList<QuestionAnswer> results = _searchResponse.getQAList();
	     return results;
	} 
	
	private void prepareTipsListData(ArrayList<QuestionAnswer> results) {
		_listDataHeaderTips = new ArrayList<String>();
		_listDataChildTips = new HashMap<String, List<String>>();
 
        int count = 0;
        for (QuestionAnswer result:results)
        {
        	_listDataHeaderTips.add(result.getQuestion().toString());
        	List<String> answers = new ArrayList<String>();
        	for (Answer answer:result.getAnswerList())
        	{
        		answers.add(answer.getAnswer());
        	}
        	_listDataChildTips.put(_listDataHeaderTips.get(count), answers);
        	count++;
        }
    }
}
