package com.example.karalefort.fatman_app;

/**
 * Created by karalefort on 12/6/14.
 */
import android.view.View;
public class Beetle {

    public int x;
    public int y;
    public int beetleRadius = 20;
    private View GameView;

    public Beetle(View view, int x, int y) {
        this.GameView = view;
        this.x = x;
        this.y = y;
//        init();
    }

//    public void init() {
//        x = beetleRadius * 6;
//        y = beetleRadius * 6;
//    }
}
