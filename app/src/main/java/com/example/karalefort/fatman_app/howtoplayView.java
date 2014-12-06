package com.example.karalefort.fatman_app;

import android.content.Context;
import android.view.View;


/**
 * Created by karalefort on 12/6/14.
 */

public class howtoplayView extends View {
    private static String howtoplayStrings[];
    public howtoplayView(Context applicationContext, MainActivity mainActivity) {
        super(applicationContext);

        howtoplayStrings = getResources().getStringArray(R.array.howtoplayStrings);

        /*canvasPaint.setTextAlign(Paint.Align.CENTER);
        applicationContext.drawText(howtoplayStrings[Game_LEVEL] + ": " + gamelevel, gameCanvasHalfWidth, gameHudTextY, canvasPaint);
        canvasPaint.setTextAlign(Paint.Align.RIGHT);*/


    }
}