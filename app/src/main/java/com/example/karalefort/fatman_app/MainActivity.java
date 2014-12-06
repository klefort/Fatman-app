package com.example.karalefort.fatman_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View.MeasureSpec;


import android.graphics.Typeface;
import android.widget.GridView;
import android.widget.TextView;

import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;




/*class AndroidExternalFontsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    // text view label
        TextView fatmantext = (TextView) findViewById(R.id.fatmantext_id);

        // Font path
        String fontPath = "fonts/FascinateInline-Regular";

        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        // Applying font
        fatmantext.setTypeface(tf);

        setContentView(R.layout.activity_main);
    }
}*/

public class MainActivity extends Activity {



    private GameView View;
    //public TextView txtfatman = (TextView) findViewById(R.id.fatmantext_id);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        protected void onLayout (boolean changed, int left, int top, int right, int bottom)
        //View.measure(UNSPECIFIED, UNSPECIFIED);
//        final int width = View.getMeasuredWidth();
//        final int height = View.getMeasuredHeight();
        final int width = 400;
        final int height = 400;


        final Button exitButton = (Button) findViewById(R.id.exit_id);
        exitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        final Button playButton = (Button) findViewById(R.id.play_id);
        playButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
               // requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                View = new GameView(getApplicationContext(), MainActivity.this, width, height);
                View.setFocusable(true);
                setContentView(View);


            }


        });

        final Button howtoplayButton = (Button) findViewById(R.id.howtoplay_id);
        howtoplayButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setContentView(new howtoplayView(getApplicationContext(), MainActivity.this));

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
