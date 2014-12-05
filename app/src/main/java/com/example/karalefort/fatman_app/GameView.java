package com.example.karalefort.fatman_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.hardware.SensorManager;

public class GameView extends View {
    private Bitmap fatman;
    private Bitmap pdonut;
    private Bitmap bdonut;
    private Bitmap chocdonut;
    private SensorManager gameSensorManager;
    private float gameAccelX = 0;
    private float gameAccelY = 0;
    private float gameAccelZ = 0;

    public GameView(Context context) {
        super(context);

        pdonut = BitmapFactory.decodeResource(getResources(), R.drawable.pinkdonut);
        fatman = BitmapFactory.decodeResource(getResources(), R.drawable.fatman);
        bdonut = BitmapFactory.decodeResource(getResources(), R.drawable.bluedonut);
        chocdonut = BitmapFactory.decodeResource(getResources(), R.drawable.chocolatedonut);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(fatman, Fatman.x, Fatman.y, null);
        canvas.drawBitmap(pdonut, pinkDonut.x, pinkDonut.y, null);
        canvas.drawBitmap(bdonut, blueDonut.x, blueDonut.y, null);
        canvas.drawBitmap(chocdonut, chocDonut.x, chocDonut.y, null);
    }


}