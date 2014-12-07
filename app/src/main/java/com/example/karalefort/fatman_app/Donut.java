package com.example.karalefort.fatman_app;

/**
 * Created by karalefort on 12/5/14.
 */

import android.view.View;

public class Donut {

    public char color_id;
    public int x;
    public int y;
    public int DonutRadius = 20;
    private View GameView;
    public boolean eaten = false;

    public Donut(View view, int x, int y, char id, int mwidth) {
        this.GameView = view;
        this.x = x- (mwidth/40);
        this.y = y-(mwidth/40);
        this.color_id = id;
        this.DonutRadius = mwidth/40;
//        init();
    }

//    public void init() {
//        x = DonutRadius * 6;
//        y = DonutRadius * 6;
//    }


    public boolean isEaten() {
        return eaten;
    }

}
