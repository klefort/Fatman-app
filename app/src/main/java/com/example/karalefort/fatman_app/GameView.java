package com.example.karalefort.fatman_app;

import android.content.Context;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.hardware.SensorManager;
import android.hardware.SensorListener;
import java.util.ArrayList;
import android.media.MediaPlayer;

public class GameView extends View
{

    //Create a list of donuts and beetles
    private ArrayList<Beetle> beetleList = new ArrayList<Beetle>();
    private ArrayList<Donut> donutList = new ArrayList<Donut>();

    //Noise that Fatman makes when he eats a donut
    MediaPlayer munch = MediaPlayer.create(getContext(), R.raw.eating);

    private Bitmap fatman;
    private Bitmap pdonut;
    private Bitmap bdonut;
    private Bitmap chocdonut;
    private Bitmap beetle;
    private Fatman gameFatman;
    private Office gameOffice;
    private Canvas gameCanvas;
    private Paint canvasPaint;
    private Typeface gameFont = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    private Activity gameActivity;

    private final static int BEFORE_BEGIN_STATE = -1;
    private final static int GAME_START = 0;
    private final static int GAME_RUNNING = 1;
    private final static int GAME_OVER = 2;
    private final static int GAME_COMPLETE = 3;
    private static int gameCurrentState = BEFORE_BEGIN_STATE;

    private final static int Game_LIVES = 0;
    private final static int Game_LEVEL = 1;
    private final static int Game_TIME = 2;
    private final static int Game_TAP_SCREEN = 3; // tap screen to restart
    private final static int Game_GAME_COMPLETE = 4;
    private final static int Game_GAME_OVER = 5;
    private final static int Game_TOTAL_TIME = 6;
    private final static int Game_GAME_OVER_MSG_A = 7;// level, keep trying
    private final static int Game_GAME_OVER_MSG_B = 8;// touch to restart
    private final static int Game_RESTART = 9;

    public int mheight = 0;
    public int mwidth = 0;
    private int fontTextPadding = 10;
    private static String gameStrings[];
    private boolean gameWarning = false;
    public int gameCanvasWidth = 0;
    public int gameCanvasHeight = 0;
    private int gameCanvasHalfWidth = 0;
    private int gameCanvasHalfHeight = 0;
    private int gamelevel = 1;

    private long levelStartTime;
    private long levelRemainTime = 30000;
    private boolean touched = false;

    private SensorManager gameSensorManager;
    private float gameAccelX = 0;
    private float gameAccelY = 0;
    private float gameAccelZ = 0;

    private final SensorListener gameSensorAccelerometer = new SensorListener()
    {
        public void onSensorChanged(int sensor, float[] values)
        {
            gameAccelX = values[0];
            gameAccelY = values[1];
            gameAccelZ = values[2];
        }

        public void onAccuracyChanged(int sensor, int accuracy) {
        }
    };


    public GameView(Context context, Activity activity)
    {

        super(context);
        canvasPaint = new Paint();

        //Makes game full screen
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        mwidth=metrics.widthPixels;
        mheight=metrics.heightPixels;
        gameActivity = activity;
        pdonut = BitmapFactory.decodeResource(getResources(), R.drawable.pinkdonutsmall);
        bdonut = BitmapFactory.decodeResource(getResources(), R.drawable.bluedonutsmall);
        chocdonut = BitmapFactory.decodeResource(getResources(), R.drawable.chocdonutsmall);
        fatman = BitmapFactory.decodeResource(getResources(), R.drawable.fatman);
        beetle = BitmapFactory.decodeResource(getResources(), R.drawable.smallbeetle);
        gameSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        gameSensorManager.registerListener(gameSensorAccelerometer, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        gameOffice = new Office(gameActivity, mwidth, mheight);
        gameFatman = new Fatman(this, mwidth, mheight);
        gameStrings = getResources().getStringArray(R.array.gameStrings);
        changeState(GAME_START);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        gameCanvasWidth = w;
        gameCanvasHeight = h;
        gameCanvasHalfWidth = w / 2;
        gameCanvasHalfHeight = h / 2;
    }


    public void gameOnEachTick()
    {
        switch (gameCurrentState)
        {
            case GAME_START:
                startNewGame();
                changeState(GAME_RUNNING);

            case GAME_RUNNING:
                if (!gameWarning) {
                    updateFatmanPosition();
                    levelRemainTime = 31000 - (System.currentTimeMillis() - levelStartTime);
                }
                break;
        }
        invalidate();
    }


    public void startNewGame()
    {
        gamelevel = 0;
        nextLevel();
    }


    //Reinitializes every level after dying
    public void startLevel()
    {
        touched = false;
        levelRemainTime = 30000;
        gameFatman.fatmanRadius = mwidth/(40);
        beetleList.clear();
        donutList.clear();

        switch (gamelevel) {
            case 1:
                gameFatman.init((mwidth/20)*3, (mheight/36)*3,mwidth);

                donutList.add(new Donut(this, (mwidth/20)*4, (mheight/36)*27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*13,'c', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*8, (mheight/36)*30,'c', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*10, (mheight/36)*6,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*11, (mheight/36)*21,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*15, (mheight/36)*3,'c', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*15,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*17, (mheight/36)*24,'p', mwidth));

                beetleList.add(new Beetle(this,(mwidth/20)*3, (mheight/36)*30, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*4, (mheight/36)*9, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*11, (mheight/36)*27, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*11, (mheight/36)*27, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*18, (mheight/36)*3, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*18, (mheight/36)*30, mwidth));
                break;

            case 2:
                gameFatman.init((mwidth/20)*3,(mheight/36)*29, mwidth);

                donutList.add(new Donut(this,(mwidth/20)*18,(mheight/36)*9,'p',mwidth));

                beetleList.add(new Beetle(this,(mwidth/20)*11,(mheight/36)*4,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*6,(mheight/36)*8,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*8,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*9,(mheight/36)*9,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*12,(mheight/36)*11,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*7,(mheight/36)*12,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*9,(mheight/36)*14,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*12,(mheight/36)*15,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*16,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*11,(mheight/36)*19,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*16,(mheight/29)*5,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*18,(mheight/36)*19,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*17,(mheight/36)*12,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*5,(mheight/36)*16,mwidth));
                break;

            case 3:
                gameFatman.init((mwidth/20*10), (mheight/36)*14,mwidth);

                donutList.add(new Donut(this, (mwidth/20)* 16, (mheight/36)* 5,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));

                beetleList.add(new Beetle(this, (mwidth/20)* 10, (mheight/36)*27, mwidth));
                break;

            case 4:
                gameFatman.init((mwidth/20)*5, (mheight/36)*4,mwidth);

//                donutList.add(new Donut(this, (mwidth/20)* 3, (mheight/36)* 27,'b', mwidth));
//                donutList.add(new Donut(this, (mwidth/20)* 6, (mheight/36)* 23,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 7, (mheight/36)* 10,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 10, (mheight/36)* 2,'p', mwidth));
//                donutList.add(new Donut(this, (mwidth/20)* 14, (mheight/36)* 8, 'b',mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 14, (mheight/36)* 12,'p', mwidth));

                beetleList.add(new Beetle(this, (mwidth/20)* 2, (mheight/36)* 5, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 2, (mheight/36)* 13, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 3, (mheight/36)* 29, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 5, (mheight/36)* 20, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 7, (mheight/36)* 27, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 8, (mheight/36)* 14, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 10, (mheight/36)* 7, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 6, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 15, (mheight/36)* 30, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 16, (mheight/36)* 6, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 17, (mheight/36)* 7, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 17, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 18, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 19, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 20, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 17, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 18, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 19, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 20, mwidth));
                break;

            case 5:
                gameFatman.init((mwidth/20)*5, (mheight/36)*4,mwidth);
                donutList.add(new Donut(this, (mwidth/20)*12, (mheight/36)*4, 'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*4,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*3, (mheight/36)*15, 'p',mwidth));
                donutList.add(new Donut(this, (mwidth/20)*3, (mheight/36)*16, 'p',mwidth));
                donutList.add(new Donut(this, (mwidth/20)*7, (mheight/36)*15, 'p',mwidth));
                donutList.add(new Donut(this, (mwidth/20)*13, (mheight/36)*23,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*18, (mheight/36)*23,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*18, (mheight/36)*30,'p', mwidth));

                beetleList.add(new Beetle(this, (mwidth/20)*2, (mheight/36)*2, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)*8, (mheight/36)*2, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)*19, (mheight/36)*4, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)*19, (mheight/36)*22, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)*2, (mheight/36)*31, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)*16, (mheight/36)*22, mwidth));
                break;

            case 6:
                gameFatman.init((mwidth / 20) * 5, (mheight / 36) * 18, mwidth);

                donutList.add(new Donut(this,(mwidth/20)*2,(mheight/36)*3,'b',mwidth));
                donutList.add(new Donut(this,(mwidth/20)*3,(mheight/36)*7,'p',mwidth));
                donutList.add(new Donut(this,(mwidth/20)*2,(mheight/36)*12,'b',mwidth));
                donutList.add(new Donut(this,(mwidth/20)*11,(mheight/36)*12,'c',mwidth));
                donutList.add(new Donut(this,(mwidth/20)*16,(mheight/36)*5,'b',mwidth));
                donutList.add(new Donut(this,(mwidth/20)*17,(mheight/36)*4,'b',mwidth));
                donutList.add(new Donut(this,(mwidth/20)*5,(mheight/36)*27,'b',mwidth));
                donutList.add(new Donut(this,(mwidth/20)*3,(mheight/36)*30,'b',mwidth));

                beetleList.add(new Beetle(this,(mwidth/20)*3,(mheight/36)*16,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*3,(mheight/36)*20,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*7,(mheight/36)*16,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*7,(mheight/36)*20,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*14,(mheight/36)*8,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*8,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*19,(mheight/36)*2,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*8,(mheight/36)*26,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*31,mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*18,(mheight/36)*26,mwidth));
                break;

            case 7:
                gameFatman.init((mwidth/20)*5, (mheight/36)*4,mwidth);
                donutList.add(new Donut(this, (mwidth/20)* 13, (mheight/36)* 19,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 14, (mheight/36)* 30,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 2, (mheight/36)* 13,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)* 2, (mheight/36)* 27,'p', mwidth));

                beetleList.add(new Beetle(this, (mwidth/20)* 6, (mheight/36)* 2, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 9, (mheight/36)* 2, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 14, (mheight/36)* 2, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 14, (mheight/36)* 7, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 5, (mheight/36)* 13, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 2, (mheight/36)* 19, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 7, (mheight/36)* 22, mwidth));
                beetleList.add(new Beetle(this, (mwidth/20)* 4, (mheight/36)* 29, mwidth));
                break;

            case 8:
                gameFatman.init((mwidth/20)*10, (mheight/36)*22,mwidth);

                donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*7,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*25,'c', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*29,'c', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*8,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*10, (mheight/36)*18,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*10, (mheight/36)*19,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*11, (mheight/36)*18,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*11, (mheight/36)*19,'b', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*14, (mheight/36)*24,'p', mwidth));
                donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*24,'p', mwidth));

                beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*15, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*16, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*17, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*18, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*19, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*15, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*16, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*17, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*18, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*19, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*8, (mheight/36)*14, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*9, (mheight/36)*13, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*10, (mheight/36)*12, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*11, (mheight/36)*12, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*12, (mheight/36)*13, mwidth));
                beetleList.add(new Beetle(this,(mwidth/20)*13, (mheight/36)*14, mwidth));
                break;
        }
    }

    //Initializes new levels
    public void nextLevel()
    {
        if (gamelevel < gameOffice.MAX_LEVELS)
        {
            touched = false;
            levelRemainTime = 30000;
            gameFatman.fatmanRadius = mwidth/(40);
            gameWarning = true;
            gamelevel++;
            gameOffice.load(gameActivity, gamelevel);
            beetleList.clear();
            donutList.clear();

            switch (gamelevel) {
                case 1:
                    gameFatman.init((mwidth/20)*3, (mheight/36)*3,mwidth);

                    donutList.add(new Donut(this, (mwidth/20)*4, (mheight/36)*27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*13,'c', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*8, (mheight/36)*30,'c', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*10, (mheight/36)*6,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*11, (mheight/36)*21,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*15, (mheight/36)*3,'c', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*15,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*17, (mheight/36)*24,'p', mwidth));

                    beetleList.add(new Beetle(this,(mwidth/20)*3, (mheight/36)*30, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*4, (mheight/36)*9, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*11, (mheight/36)*27, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*11, (mheight/36)*27, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*18, (mheight/36)*3, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*18, (mheight/36)*30, mwidth));
                    break;

                case 2:
                    gameFatman.init((mwidth/20)*3,(mheight/36)*29, mwidth);

                    donutList.add(new Donut(this,(mwidth/20)*18,(mheight/36)*9,'p',mwidth));

                    beetleList.add(new Beetle(this,(mwidth/20)*11,(mheight/36)*4,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*6,(mheight/36)*8,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*8,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*9,(mheight/36)*9,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*12,(mheight/36)*11,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*7,(mheight/36)*12,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*9,(mheight/36)*14,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*12,(mheight/36)*15,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*16,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*11,(mheight/36)*19,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*16,(mheight/29)*5,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*18,(mheight/36)*19,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*17,(mheight/36)*12,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*5,(mheight/36)*16,mwidth));
                    break;

                case 3:
                    gameFatman.init((mwidth/20*10), (mheight/36)*14,mwidth);

                    donutList.add(new Donut(this, (mwidth/20)* 16, (mheight/36)* 5,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 4, (mheight/36)* 27,'p', mwidth));

                    beetleList.add(new Beetle(this, (mwidth/20)* 10, (mheight/36)*27, mwidth));
                    break;

                case 4:
                    gameFatman.init((mwidth/20)*5, (mheight/36)*4,mwidth);

//                    donutList.add(new Donut(this, (mwidth/20)* 3, (mheight/36)* 27,'b', mwidth));
//                    donutList.add(new Donut(this, (mwidth/20)* 6, (mheight/36)* 23,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 7, (mheight/36)* 10,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 10, (mheight/36)* 2,'p', mwidth));
//                    donutList.add(new Donut(this, (mwidth/20)* 14, (mheight/36)* 8, 'b',mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 14, (mheight/36)* 12,'p', mwidth));

                    beetleList.add(new Beetle(this, (mwidth/20)* 2, (mheight/36)* 5, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 2, (mheight/36)* 13, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 3, (mheight/36)* 29, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 5, (mheight/36)* 20, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 7, (mheight/36)* 27, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 8, (mheight/36)* 14, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 10, (mheight/36)* 7, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 6, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 15, (mheight/36)* 30, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 16, (mheight/36)* 6, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 17, (mheight/36)* 7, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 17, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 18, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 19, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 12, (mheight/36)* 20, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 17, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 18, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 19, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 13, (mheight/36)* 20, mwidth));
                    break;

                case 5:
                    gameFatman.init((mwidth/20)*5, (mheight/36)*4,mwidth);
                    donutList.add(new Donut(this, (mwidth/20)*12, (mheight/36)*4, 'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*4,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*3, (mheight/36)*15, 'p',mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*3, (mheight/36)*16, 'p',mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*7, (mheight/36)*15, 'p',mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*13, (mheight/36)*23,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*18, (mheight/36)*23,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*18, (mheight/36)*30,'p', mwidth));

                    beetleList.add(new Beetle(this, (mwidth/20)*2, (mheight/36)*2, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)*8, (mheight/36)*2, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)*19, (mheight/36)*4, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)*19, (mheight/36)*22, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)*2, (mheight/36)*31, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)*16, (mheight/36)*22, mwidth));
                    break;

                case 6:
                    gameFatman.init((mwidth / 20) * 5, (mheight / 36) * 18, mwidth);

                    donutList.add(new Donut(this,(mwidth/20)*2,(mheight/36)*3,'b',mwidth));
                    donutList.add(new Donut(this,(mwidth/20)*3,(mheight/36)*7,'p',mwidth));
                    donutList.add(new Donut(this,(mwidth/20)*2,(mheight/36)*12,'b',mwidth));
                    donutList.add(new Donut(this,(mwidth/20)*11,(mheight/36)*12,'c',mwidth));
                    donutList.add(new Donut(this,(mwidth/20)*16,(mheight/36)*5,'b',mwidth));
                    donutList.add(new Donut(this,(mwidth/20)*17,(mheight/36)*4,'b',mwidth));
                    donutList.add(new Donut(this,(mwidth/20)*5,(mheight/36)*27,'b',mwidth));
                    donutList.add(new Donut(this,(mwidth/20)*3,(mheight/36)*30,'b',mwidth));

                    beetleList.add(new Beetle(this,(mwidth/20)*3,(mheight/36)*16,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*3,(mheight/36)*20,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*7,(mheight/36)*16,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*7,(mheight/36)*20,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*14,(mheight/36)*8,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*8,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*19,(mheight/36)*2,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*8,(mheight/36)*26,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*15,(mheight/36)*31,mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*18,(mheight/36)*26,mwidth));
                    break;

                case 7:
                    gameFatman.init((mwidth/20)*5, (mheight/36)*4,mwidth);
                    donutList.add(new Donut(this, (mwidth/20)* 13, (mheight/36)* 19,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 14, (mheight/36)* 30,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 2, (mheight/36)* 13,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)* 2, (mheight/36)* 27,'p', mwidth));

                    beetleList.add(new Beetle(this, (mwidth/20)* 6, (mheight/36)* 2, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 9, (mheight/36)* 2, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 14, (mheight/36)* 2, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 14, (mheight/36)* 7, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 5, (mheight/36)* 13, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 2, (mheight/36)* 19, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 7, (mheight/36)* 22, mwidth));
                    beetleList.add(new Beetle(this, (mwidth/20)* 4, (mheight/36)* 29, mwidth));
                    break;

                case 8:
                    gameFatman.init((mwidth/20)*10, (mheight/36)*22,mwidth);

                    donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*7,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*25,'c', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*5, (mheight/36)*29,'c', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*8,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*10, (mheight/36)*18,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*10, (mheight/36)*19,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*11, (mheight/36)*18,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*11, (mheight/36)*19,'b', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*14, (mheight/36)*24,'p', mwidth));
                    donutList.add(new Donut(this, (mwidth/20)*16, (mheight/36)*24,'p', mwidth));

                    beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*15, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*16, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*17, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*18, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*7, (mheight/36)*19, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*15, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*16, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*17, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*18, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*14, (mheight/36)*19, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*8, (mheight/36)*14, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*9, (mheight/36)*13, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*10, (mheight/36)*12, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*11, (mheight/36)*12, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*12, (mheight/36)*13, mwidth));
                    beetleList.add(new Beetle(this,(mwidth/20)*13, (mheight/36)*14, mwidth));
                    break;
            }
        }
        else {
            changeState(GAME_COMPLETE);
        }
    }



    //Updates Fatmans position throughout the game
    public void updateFatmanPosition() {

        //Dictates how Fatman can navigate through the levels
        if (!(gameOffice.getCellType(gameFatman.getX() + gameFatman.fatmanRadius, gameFatman.getY()) == gameOffice.VOID_TILE && gameAccelX > 0) &&
                !(gameOffice.getCellType(gameFatman.getX() - gameFatman.fatmanRadius, gameFatman.getY()) == gameOffice.VOID_TILE && gameAccelX < 0) &&
                !((gameOffice.getCellType(gameFatman.getX() + gameFatman.fatmanRadius, gameFatman.getY() + (int) (gameFatman.fatmanRadius * .8)) == gameOffice.VOID_TILE && gameAccelX > 0) ||
                        (gameOffice.getCellType(gameFatman.getX() + gameFatman.fatmanRadius, gameFatman.getY() - (int) (gameFatman.fatmanRadius * .8)) == gameOffice.VOID_TILE && gameAccelX > 0)) &&
                !((gameOffice.getCellType(gameFatman.getX() - gameFatman.fatmanRadius, gameFatman.getY() - (int) (gameFatman.fatmanRadius * .8)) == gameOffice.VOID_TILE && gameAccelX < 0) ||
                        (gameOffice.getCellType(gameFatman.getX() - gameFatman.fatmanRadius, gameFatman.getY() + (int) (gameFatman.fatmanRadius * .8)) == gameOffice.VOID_TILE && gameAccelX < 0))) {
            gameFatman.updatePositionX(gameAccelX * 2);
        }

        if (!(gameOffice.getCellType(gameFatman.getX(), gameFatman.getY() + gameFatman.fatmanRadius) == gameOffice.VOID_TILE && gameAccelY < 0) &&
                !(gameOffice.getCellType(gameFatman.getX(), gameFatman.getY() - gameFatman.fatmanRadius) == gameOffice.VOID_TILE && gameAccelY > 0)
                &&
                !((gameOffice.getCellType(gameFatman.getX() + (int) (gameFatman.fatmanRadius * 0.8), gameFatman.getY() + gameFatman.fatmanRadius) == gameOffice.VOID_TILE && gameAccelY < 0) ||
                        (gameOffice.getCellType(gameFatman.getX() - (int) (gameFatman.fatmanRadius * .8), gameFatman.getY() + gameFatman.fatmanRadius) == gameOffice.VOID_TILE && gameAccelY < 0)) &&
                !((gameOffice.getCellType(gameFatman.getX() + (int) (gameFatman.fatmanRadius * .8), gameFatman.getY() - gameFatman.fatmanRadius) == gameOffice.VOID_TILE && gameAccelY > 0) ||
                        (gameOffice.getCellType(gameFatman.getX() - (int) (gameFatman.fatmanRadius * .8), gameFatman.getY() - gameFatman.fatmanRadius) == gameOffice.VOID_TILE && gameAccelY > 0))) {

            gameFatman.updatePositionY(gameAccelY * 2);
        }

        //Dictates Fatman's behavior upon eating a beetle
        for (int i = 0; i < beetleList.size(); i++) {
            if ((gameFatman.getX() + gameFatman.fatmanRadius > beetleList.get(i).x + beetleList.get(i).beetleRadius &&
                    gameFatman.getX() - gameFatman.fatmanRadius < beetleList.get(i).x + beetleList.get(i).beetleRadius) ||
                    (gameFatman.getX() + gameFatman.fatmanRadius > beetleList.get(i).x - beetleList.get(i).beetleRadius &&
                            gameFatman.getX() - gameFatman.fatmanRadius < beetleList.get(i).x - beetleList.get(i).beetleRadius)) {
                if ((gameFatman.getY() + gameFatman.fatmanRadius < beetleList.get(i).y + beetleList.get(i).beetleRadius &&
                        gameFatman.getY() + gameFatman.fatmanRadius > beetleList.get(i).y - beetleList.get(i).beetleRadius) ||
                        (gameFatman.getY() + gameFatman.fatmanRadius > beetleList.get(i).y + beetleList.get(i).beetleRadius &&
                                gameFatman.getY() - gameFatman.fatmanRadius < beetleList.get(i).y + beetleList.get(i).beetleRadius)) {

                    if (gameFatman.getLives() > 0) {
                        gameFatman.FatmanDies();
                        startLevel();

                        gameWarning = true;
                    }
                    else {
                        changeState(GAME_OVER);
                    }
                }
            }
        }

        //Dictates Fatman's behavior upon eating a donut
        for (int i = 0; i < donutList.size(); i++) {

            if ((gameFatman.getX() + gameFatman.fatmanRadius > donutList.get(i).x + donutList.get(i).DonutRadius &&
                    gameFatman.getX() - gameFatman.fatmanRadius < donutList.get(i).x + donutList.get(i).DonutRadius) ||
                    (gameFatman.getX() + gameFatman.fatmanRadius > donutList.get(i).x - donutList.get(i).DonutRadius &&
                            gameFatman.getX() - gameFatman.fatmanRadius < donutList.get(i).x - donutList.get(i).DonutRadius)) {
                if ((gameFatman.getY() + gameFatman.fatmanRadius < donutList.get(i).y + donutList.get(i).DonutRadius &&
                        gameFatman.getY() + gameFatman.fatmanRadius > donutList.get(i).y - donutList.get(i).DonutRadius) ||
                        (gameFatman.getY() + gameFatman.fatmanRadius > donutList.get(i).y + donutList.get(i).DonutRadius &&
                                gameFatman.getY() - gameFatman.fatmanRadius < donutList.get(i).y + donutList.get(i).DonutRadius)) {

                    gameFatman.fatmanRadius += mwidth/80;
                    donutList.remove(i);
                    munch.start();

                    if (donutList.isEmpty()) {
                        nextLevel();

                    }
                    break;
                }
            }

            //Dictates what happens when user runs out of time
            if (levelRemainTime <= 0)
            {
                touched = false;
                if (gameFatman.getLives() > 0) {
                    gameFatman.FatmanDies();
                    startLevel();
                    gameWarning = true;
                }
                else {
                    changeState(GAME_OVER);
                }
            }
        }
    }

    //Dictates what happens when the user touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (gameCurrentState == GAME_OVER || gameCurrentState == GAME_COMPLETE)
                gameCurrentState = GAME_START;

            else if (gameCurrentState == GAME_RUNNING && !touched) {
                gameWarning = false;
                touched = true;
                levelStartTime = System.currentTimeMillis();
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            cleanUp();

        return true;
    }



    //Draws each element on the screen
    @Override
    protected void onDraw(Canvas canvas) {

        int newwidth = 2 * gameFatman.fatmanRadius;
        int newheight =  2 * gameFatman.fatmanRadius;

        Bitmap resizedfatman = Bitmap.createScaledBitmap(fatman, newwidth, newheight, false);

        gameCanvas = canvas;
        canvasPaint.setColor(Color.BLACK);
        gameCanvas.drawRect(0, 0, gameCanvasWidth, gameCanvasHeight, canvasPaint);
        switch (gameCurrentState) {
            case GAME_RUNNING:
                gameOffice.draw(gameCanvas, canvasPaint);

                canvas.drawBitmap(resizedfatman, gameFatman.getX()- resizedfatman.getWidth()/2 , gameFatman.getY()- resizedfatman.getHeight()/2, null);

                for (int i = 0; i < beetleList.size();i++) {
                    canvas.drawBitmap(beetle, beetleList.get(i).x - beetle.getWidth()/2, beetleList.get(i).y - beetle.getWidth()/2, null);
                }
                for (int i = 0; i < donutList.size();i++) {
                    if (donutList.get(i).color_id == 'p')
                        canvas.drawBitmap(pdonut, donutList.get(i).x - pdonut.getWidth()/2, donutList.get(i).y - pdonut.getHeight()/2, null);
                    if (donutList.get(i).color_id == 'c')
                        canvas.drawBitmap(chocdonut, donutList.get(i).x - chocdonut.getWidth()/2, donutList.get(i).y - chocdonut.getHeight()/2, null);
                    if (donutList.get(i).color_id == 'b')
                        canvas.drawBitmap(bdonut, donutList.get(i).x - bdonut.getWidth()/2, donutList.get(i).y - bdonut.getHeight()/2, null);
                }
                drawMesseges();
                break;

            case GAME_OVER:
                drawGameOver();
                gameFatman.gameLives = 5;
                break;

            case GAME_COMPLETE:
                drawGameComplete();
                break;
        }
        gameOnEachTick();
    }


    //Draws the time, lives, and level on the bottom of the play screen
    public void drawMesseges() {
        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setTextAlign(Paint.Align.LEFT);

        gameCanvas.drawText(gameStrings[Game_TIME] + ": " + (levelRemainTime / 1000), fontTextPadding, mheight / 36 * (float)34.75,
                canvasPaint);

        canvasPaint.setTextAlign(Paint.Align.CENTER);
        gameCanvas.drawText(gameStrings[Game_LEVEL] + ": " + gamelevel, mwidth/2, mheight/36*(float)34.75, canvasPaint);
        canvasPaint.setTextAlign(Paint.Align.RIGHT);
        gameCanvas.drawText(gameStrings[Game_LIVES] + ": " + gameFatman.getLives(), mwidth - fontTextPadding,
                mheight/36*(float)34.75, canvasPaint);
        if (gameWarning) {
            canvasPaint.setColor(Color.BLACK);

            gameCanvas.drawRect((float) mwidth / 2, (float) mheight / 2, (float) mwidth / 2, (float) mheight / 2,
                    canvasPaint);

            canvasPaint.setColor(Color.DKGRAY);
            canvasPaint.setTextSize(40);

            canvasPaint.setTextAlign(Paint.Align.CENTER);
            gameCanvas.drawText(gameStrings[Game_TAP_SCREEN], gameCanvasHalfWidth, gameCanvasHalfHeight, canvasPaint);
        }
    }

    //Displays message when the game is over
    public void drawGameOver() {
        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setTextAlign(Paint.Align.CENTER);
        canvasPaint.setTextSize(20);
        gameCanvas.drawText(gameStrings[Game_GAME_OVER], gameCanvasHalfWidth, gameCanvasHalfHeight, canvasPaint);
        gameCanvas.drawText(gameStrings[Game_GAME_OVER_MSG_A] + " " + (gamelevel - 1) + " "
                + gameStrings[Game_GAME_OVER_MSG_B], gameCanvasHalfWidth, gameCanvasHalfHeight
                + (canvasPaint.getFontSpacing() * 2), canvasPaint);

        gameCanvas.drawText(gameStrings[Game_RESTART], gameCanvasHalfWidth, gameCanvasHeight
                - (canvasPaint.getFontSpacing() * 3), canvasPaint);
        gameCanvas.translate(0, 50);
    }

    //Displays the message when the game is completed
    public void drawGameComplete() {
        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setTextAlign(Paint.Align.CENTER);
        canvasPaint.setTextSize(40);
        gameCanvas.drawText("CONGRATULATIONS!", gameCanvasHalfWidth, gameCanvasHalfHeight-140, canvasPaint);
        gameCanvas.drawText("YOU ARE A", gameCanvasHalfWidth, gameCanvasHalfHeight-90, canvasPaint);
        gameCanvas.drawText("HAPPY FATMAN!!", gameCanvasHalfWidth, gameCanvasHalfHeight-40, canvasPaint);

        canvasPaint.setTextSize(20);

        gameCanvas.drawText(gameStrings[Game_RESTART], gameCanvasHalfWidth, gameCanvasHalfHeight+20, canvasPaint);
    }

    //Changes the state of the game
    public void changeState(int newState) {
        gameCurrentState = newState;
    }

    public void registerListener() {
        gameSensorManager.registerListener(gameSensorAccelerometer,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregisterListener() {
        gameSensorManager.unregisterListener(gameSensorAccelerometer);
    }

    //Acts as a deconstructor
    public void cleanUp() {
        gameFatman = null;
        gameOffice = null;
        gameStrings = null;
        unregisterListener();
        gameActivity.finish();
    }
}


