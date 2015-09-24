package com.ecodcnc.lab9;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

// textview
import android.widget.EditText;
import android.widget.TextView;

// new for content provider

//added for intent


public class MainActivity extends Activity {

    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void MyClick(View view) {

        Log.d("MyClick", "Yes working");

        BackGroundTask task = new BackGroundTask();
        task.execute(new Integer[]{10, 100, 1000});



    }

    private class BackGroundTask extends AsyncTask<Integer, Integer, String> {
        Integer progress = new Integer(0);

        @Override
        protected String doInBackground(Integer... args) {

            int result =0;
            for (int i=0;i<args[0];i++) { result=+i;}
            for (int i=0;i<args[1];i++) {result=+i;}
            for (int i=0;i<args[2];i++) {result=+i;}

            //simulate a long task by delaying for 5 seconds:
            long startTime = System.currentTimeMillis();
            long delay = 50000;
            long elapsedTime;
            while(System.currentTimeMillis() < startTime + delay) {
                elapsedTime = System.currentTimeMillis() - startTime;
                if( elapsedTime % 50 == 0) {
                    //update the progress bar...
                    progress = (int) elapsedTime / 50;
                    publishProgress(progress);
                }
            }
            //return a string representation of the square root:
            return String.valueOf(result);
        }

        @Override
        protected void onProgressUpdate(Integer... units) {
            Log.i("HERE",Integer.toString(units[0]));
            textView = (TextView) findViewById(R.id.textview2);
            textView.setVisibility(View.VISIBLE);
            textView.setText(Integer.toString(units[0]));
        }

        @Override
        protected void onPostExecute(String result) {
            editText = (EditText) findViewById(R.id.editText);
            textView = (TextView) findViewById(R.id.textview2);
            textView.setVisibility(View.GONE);
            editText.setText(result);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}