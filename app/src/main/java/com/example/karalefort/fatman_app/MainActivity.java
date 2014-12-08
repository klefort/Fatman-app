package com.example.karalefort.fatman_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;


public class MainActivity extends Activity {

    private GameView View;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Makes an exit button
        final Button exitButton = (Button) findViewById(R.id.exit_id);
        exitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //Makes the play button
        final Button playButton = (Button) findViewById(R.id.play_id);
        playButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                View = new GameView(getApplicationContext(), MainActivity.this);
                View.setFocusable(true);

                setContentView(View);

            }


        });

        //Makes the 'How to Play' button
        final Button howtoplayButton = (Button) findViewById(R.id.howtoplay_id);
        howtoplayButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.howtoplay);
                final Button backButton = (Button) findViewById(R.id.back_id);
                backButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }


                });
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
