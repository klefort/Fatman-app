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

    public Donut(View view, int x, int y, char id) {
        this.GameView = view;
        this.x = x;
        this.y = y;
        this.color_id = id;
        init();
    }

    public void init() {
        x = DonutRadius * 6;
        y = DonutRadius * 6;
    }


    public boolean isEaten() {
        return eaten;
    }

}
