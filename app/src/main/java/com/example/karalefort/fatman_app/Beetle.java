package com.example.karalefort.fatman_app;

/**
 * Created by karalefort on 12/6/14.
 */
import android.view.View;
public class Beetle {

    public int x;
    public int y;
    public int beetleRadius;
    private View GameView;

    public Beetle(View view, int x, int y, int mwidth) {
        this.GameView = view;
        this.x = x- mwidth/40;
        this.y = y- mwidth/40;
        beetleRadius = mwidth/40;
//        init();
    }

//    public void init() {
//        x = beetleRadius * 6;
//        y = beetleRadius * 6;
//    }
}
