package com.example.karalefort.fatman_app;


import android.view.View;

public class Fatman
{
    public static int x = 100;
    public static int y = 100;
    public int fatmanRadius = 15;
    private View GameView;
    public int gameLives = 5;

    //Fatman constructor
    public Fatman (View view, int mwidth, int mheight)
    {
        this.GameView = view;
        init(x, y,mwidth);

    }

    //Initializes Fatman's position and size
    public void init(int x, int y, int mwidth)
    {
        this.x = x;
        this.y = y;
        fatmanRadius = mwidth/40;
    }

    //Updates Fatman's x-position
    public void updatePositionX(float newX)
    {
        x += newX;
    }

    //Updates Fatman's y-position
    public void updatePositionY(float newY)
    {
        y -= newY;
    }

    public void FatmanDies()
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
}
