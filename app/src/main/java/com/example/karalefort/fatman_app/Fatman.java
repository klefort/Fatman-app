package com.example.karalefort.fatman_app;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.view.View;
import android.graphics.Bitmap;


/**
 * Created by karalefort on 12/4/14.
 */


public class Fatman
{
    private int fatmanColor = Color.YELLOW; //delete later
    public static int x = 10;
    public static int y = 10;
    private int fatmanRadius = 10;
    private View GameView;
    public int gameLives = 5;
    public Fatman (View view)
    {
        this.GameView = view;
        init();

    }


    public void init()
    {
        x = fatmanRadius * 6;
        y = fatmanRadius * 6;
    }


    public void updatePositionX(float newX)
    {
        x += newX;
        if (x + fatmanRadius >= GameView.getWidth())
            fatmanRadius = GameView.getWidth() - fatmanRadius;
        else if (x - fatmanRadius< 0)
            x = fatmanRadius;
    }

    public void updatePositionY(float newY)
    {
        y -= newY;
        if (y + fatmanRadius >= GameView.getWidth())
            fatmanRadius = GameView.getWidth() - fatmanRadius;
        else if (y - fatmanRadius< 0)
            y = fatmanRadius;
    }

    public void marbleDies()
    {
        gameLives--;
    }


    public void setLives(int val)
    {
        gameLives = val;
    }

    public int getLives()
    {
        return gameLives;
    }


    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }



   /* public void draw (Canvas canvas){
        Bitmap fatman = BitmapFactory.decodeResource(getResources(), R.drawable.fatman);
    }*/


   /* public void draw(Canvas canvas, Paint paint)
    {
        paint.setColor(fatmanColor);
        canvas.drawCircle(x, y, fatmanRadius, paint);
    }*/

}
