package com.example.karalefort.fatman_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


import android.graphics.Typeface;
import android.widget.TextView;




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

        //setContentView(R.layout.activity_main);
        // Font path
        //String fontPath = "fonts/DAYPBL__";

        // Loading Font Face
        //Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Fascinate-Regular");
        // Applying font
        ///txtfatman.setTypeface(tf);





            final Button exitButton = (Button) findViewById(R.id.exit_id);
            exitButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        final Button playButton = (Button) findViewById(R.id.play_id);
        playButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                    //requestWindowFeature(Window.FEATURE_NO_TITLE);
                    setContentView(new GameView(getApplicationContext(), MainActivity.this));


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
