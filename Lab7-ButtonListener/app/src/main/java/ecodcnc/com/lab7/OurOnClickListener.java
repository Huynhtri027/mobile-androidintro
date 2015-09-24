package ecodcnc.com.lab7;

import android.view.View;
import android.view.View.OnClickListener;

public class OurOnClickListener implements OnClickListener {

    MainActivity caller;
    private int count;

    public OurOnClickListener(MainActivity activity) {
        this.caller = activity;
        this.count = 0;
    }

    @Override
    public void onClick(View v) {
        count++;
        String countstr = Integer.toString(count);
        caller.textView.setText("Clicked " + countstr + " times");

    }

}

