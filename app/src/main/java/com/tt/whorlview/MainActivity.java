package com.tt.whorlview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tt.whorlviewlibrary.WhorlView;

public class MainActivity extends AppCompatActivity {
    private boolean mIsRunning;
    private WhorlView mWhorlView1;
    private WhorlView mWhorlView2;
    private WhorlView mWhorlView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) this.findViewById(R.id.button);
        btn.setText("start");
        mWhorlView1 = (WhorlView) this.findViewById(R.id.whorl);
        mWhorlView2 = (WhorlView) this.findViewById(R.id.whorl2);
        mWhorlView3 = (WhorlView) this.findViewById(R.id.whorl3);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsRunning) {
                    mWhorlView1.stop();
                    mWhorlView2.stop();
                    mWhorlView3.stop();
                } else {
                    mWhorlView1.start();
                    mWhorlView2.start();
                    mWhorlView3.start();
                }
                mIsRunning = !mIsRunning;
                btn.setText(mIsRunning ? "stop" : "start");
            }
        });
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
