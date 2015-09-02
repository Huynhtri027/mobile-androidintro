package example.com.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView; // LAB 2 NEW
import android.util.Log;   // LAB 3 NEW

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);  // LAB 2 COMMENTED OUT

        Log.i(this.getClass().getName(), "Here before LAB 2 code in onCreate");  // LAB 3 CODE

        // LAB 2 THREE NEW LINES ADDED
        TextView textView = new TextView(this);
        textView.setText("Hello New World!");
        setContentView(textView);

        Log.i(this.getClass().getName(), "Here After LAB 2 code in onCreate");  // LAB 3 CODE
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

            // ADD Lab 3 - Challenge !!!

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
