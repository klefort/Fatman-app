package com.example.karalefort.fatman_app;

import android.content.Context;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.hardware.SensorManager;
import android.hardware.SensorListener;
import android.widget.GridView;
import java.util.ArrayList;


public class GameView extends View
{

    private ArrayList<Beetle> beetleList = new ArrayList<Beetle>();
    private ArrayList<Donut> donutList = new ArrayList<Donut>();

    private Bitmap fatman;
    //    private Bitmap fatman = Bitmap.createScaledBitmap(rfatman, 25, 25, true);
    private Bitmap pdonut;
    private Bitmap bdonut;
    private Bitmap chocdonut;
    private Bitmap beetle;
    private Fatman gameFatman;
    private Office gameOffice;
    private Canvas gameCanvas;
    private Paint canvasPaint;
    private Donut gameDonut;
    private Typeface gameFont = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    private Activity gameActivity;
    private int radiusFatman;

    private final static int BEFORE_BEGIN_STATE = -1;
    private final static int GAME_START = 0;
    private final static int GAME_RUNNING = 1;
    private final static int GAME_OVER = 2;
    private final static int GAME_COMPLETE = 3;
    //    private final static int GAME_LANDSCAPE = 4;
    private static int gameCurrentState = BEFORE_BEGIN_STATE;

    private final static int Game_LIVES = 0;
    private final static int Game_LEVEL = 1;
    private final static int Game_TIME = 2;
    private final static int Game_TAP_SCREEN = 3;
    private final static int Game_GAME_COMPLETE = 4;
    private final static int Game_GAME_OVER = 5;
    private final static int Game_TOTAL_TIME = 6;
    private final static int Game_GAME_OVER_MSG_A = 7;
    private final static int Game_GAME_OVER_MSG_B = 8;
    private final static int Game_RESTART = 9;
//    private final static int Game_LANDSCAPE_MODE = 10;

    private int fontTextPadding = 10;
    private int gameHudTextY = 440;

    private static String gameStrings[];
    private boolean gameWarning = false;
    public int gameCanvasWidth = 0;
    public int gameCanvasHeight = 0;
    private int gameCanvasHalfWidth = 0;
    private int gameCanvasHalfHeight = 0;
    //    private boolean orientationPortrait = true;
    private int gamelevel = 1;
    private long gameTotalTime = 0;
    private long levelStartTime = 0;
    private long gameEndTime = 0;

    private SensorManager gameSensorManager;
    private float gameAccelX = 0;
    private float gameAccelY = 0;
    private float gameAccelZ = 0;
    private float gameSensorBuffer = 0;



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
        //canvasPaint = setAntiAlias(true);
       /* DisplayMetrics dm = new DisplayMetrics();
        createDisplayContext(Display);
        context.getSystemService(Context.WINDOW_SERVICE);
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mwidth=dm.widthPixels;
        int mheight=dm.heightPixels;*/

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mwidth=metrics.widthPixels;
        int mheight=metrics.heightPixels;

        gameActivity = activity;
        pdonut = BitmapFactory.decodeResource(getResources(), R.drawable.pinkdonutsmall);
        bdonut = BitmapFactory.decodeResource(getResources(), R.drawable.bluedonutsmall);
        chocdonut = BitmapFactory.decodeResource(getResources(), R.drawable.chocdonutsmall);
        fatman = BitmapFactory.decodeResource(getResources(), R.drawable.fatmantinyopenmouth);
        beetle = BitmapFactory.decodeResource(getResources(), R.drawable.smallbeetle);
        gameSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        gameSensorManager.registerListener(gameSensorAccelerometer, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        gameOffice = new Office(gameActivity, mwidth, mheight);
        gameFatman = new Fatman(this);
//        gameDonut = new Donut(this, 100, 100, 'c');
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
//    public int get_CanvasWidth()
//    {
//        return gameCanvasWidth;
//    }

    public void gameOnEachTick()
    {
        switch (gameCurrentState)
        {
            case GAME_START:
                startNewGame();
                changeState(GAME_RUNNING);

            case GAME_RUNNING:
                if (!gameWarning)
                    updateFatmanPosition();
                break;
        }
        invalidate();
    }


    public void startNewGame()
    {
        gameTotalTime = 0;
        gamelevel = 0;
        startLevel();
    }

    public void startLevel()
    {
        if (gamelevel < gameOffice.MAX_LEVELS)
        {
            radiusFatman = 15;
            gameWarning = true;
            gamelevel++;
            gameOffice.load(gameActivity, gamelevel);
            gameFatman.init();
            beetleList.clear();
            donutList.clear();
            switch (gamelevel) {
                case 1:
                    beetleList.add(new Beetle(this,400,200));
                    beetleList.add(new Beetle(this,400,300));
                    donutList.add(new Donut(this,100,500,'b'));
                    donutList.add(new Donut(this,200,600,'c'));
                    donutList.add(new Donut(this,200,400,'p'));
                    break;

                case 2:
                    beetleList.add(new Beetle(this,200,100));
                    beetleList.add(new Beetle(this,400,200));
                    donutList.add(new Donut(this,500,300,'p'));
                    donutList.add(new Donut(this,200,300,'c'));
                    break;
            }
        }
        else {
            changeState(GAME_COMPLETE);
        }
    }




    public void updateFatmanPosition() {
        if (!(gameOffice.getCellType(gameFatman.getX() + radiusFatman, gameFatman.getY()) == gameOffice.VOID_TILE && gameAccelX > 0) &&
                !(gameOffice.getCellType(gameFatman.getX() - radiusFatman, gameFatman.getY()) == gameOffice.VOID_TILE && gameAccelX < 0)) {
//                if (gameAccelX > gameSensorBuffer || gameAccelX < -gameSensorBuffer)
            gameFatman.updatePositionX(gameAccelX*2);
        }

        if (!(gameOffice.getCellType(gameFatman.getX(), gameFatman.getY() + radiusFatman) == gameOffice.VOID_TILE && gameAccelY < 0) &&
                !(gameOffice.getCellType(gameFatman.getX(), gameFatman.getY() - radiusFatman) == gameOffice.VOID_TILE && gameAccelY > 0)) {
//            if (gameAccelY > gameSensorBuffer || gameAccelY < -gameSensorBuffer)
            gameFatman.updatePositionY(gameAccelY*2);
        }
//        if (gameOffice.getCellType(gameFatman.getX(), gameFatman.getY()) == gameOffice.BEETLE_TILE) {
//            if (gameAccelX > gameSensorBuffer || gameAccelX < -gameSensorBuffer)
//                if (gameFatman.getLives() > 0) {
//                    gameFatman.FatmanDies();
//                    gameFatman.init();
//                    gameWarning = true;

//                }
//        }

        for (int i = 0; i < beetleList.size();i++) {
            if (gameFatman.getX() + radiusFatman > beetleList.get(i).x - beetleList.get(i).beetleRadius &&
                    gameFatman.getX() - radiusFatman < beetleList.get(i).x + beetleList.get(i).beetleRadius)
            {
                if (gameFatman.getY() + radiusFatman < beetleList.get(i).y + beetleList.get(i).beetleRadius &&
                        gameFatman.getY() - radiusFatman > beetleList.get(i).y - beetleList.get(i).beetleRadius)
                {
                    if (gameFatman.getLives() > 0) {
                        gameFatman.FatmanDies();
                        gameFatman.init();
                        gameWarning = true;
                    }
                    else {
                        gameEndTime = System.currentTimeMillis();
                        gameTotalTime += gameEndTime - levelStartTime;
                        changeState(GAME_OVER);
                    }
                }
            }
        }

//        boolean eaten = false;

        for (int i = 0; i < donutList.size(); i++) {

                if (gameFatman.getX() + radiusFatman > donutList.get(i).x - donutList.get(i).DonutRadius &&
                        gameFatman.getX() - radiusFatman < donutList.get(i).x + donutList.get(i).DonutRadius) {
                    if (gameFatman.getY() + radiusFatman < donutList.get(i).y + donutList.get(i).DonutRadius &&
                            gameFatman.getY() - radiusFatman > donutList.get(i).y - donutList.get(i).DonutRadius) {

                        radiusFatman += 15;
                        donutList.remove(i);


                        if (donutList.isEmpty()) {
                            startLevel();
                        }
                        break;
//                        size--;
//                        eaten = true;
                    }
                }

        }
//            else {
//                gameEndTime = System.currentTimeMillis();
//                gameTotalTime += gameEndTime - levelStartTime;
//                changeState(GAME_OVER);
//            }

//        } else if (gameOffice.getCellType(gameFatman.getX(), gameFatman.getY()) == gameOffice.EXIT_TILE) {
//            gameEndTime = System.currentTimeMillis();
//            gameTotalTime += gameEndTime - levelStartTime;
//            startLevel();
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (gameCurrentState == GAME_OVER || gameCurrentState == GAME_COMPLETE)
                gameCurrentState = GAME_START;
            else if (gameCurrentState == GAME_RUNNING) {
                gameWarning = false;
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

    //    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(fatman, Fatman.x, Fatman.y, null);
//        canvas.drawBitmap(pdonut, pinkDonut.x, pinkDonut.y, null);
//        canvas.drawBitmap(bdonut, blueDonut.x, blueDonut.y, null);
//        canvas.drawBitmap(chocdonut, chocDonut.x, chocDonut.y, null);
//
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        gameCanvas = canvas;
        canvasPaint.setColor(Color.BLACK);
        gameCanvas.drawRect(0, 0, gameCanvasWidth, gameCanvasHeight, canvasPaint);
        switch (gameCurrentState) {
            case GAME_RUNNING:
                gameOffice.draw(gameCanvas, canvasPaint);
                //gameFatman.draw(gameCanvas, canvasPaint);
                //canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(fatman, Fatman.x - gameFatman.fatmanRadius, Fatman.y - gameFatman.fatmanRadius, null);
//                canvas.drawBitmap(pdonut, gameDonut.x, gameDonut.y, null);
//                canvas.drawBitmap(beetle, gameOffice.beetlex, gameOffice.beetley, null);
                for (int i = 0; i < beetleList.size();i++) {
                    canvas.drawBitmap(beetle, beetleList.get(i).x - beetleList.get(i).beetleRadius, beetleList.get(i).y - beetleList.get(i).beetleRadius, null);
                }
                for (int i = 0; i < donutList.size();i++) {
                    if (donutList.get(i).color_id == 'p')
                    canvas.drawBitmap(pdonut, donutList.get(i).x - donutList.get(i).DonutRadius, donutList.get(i).y - donutList.get(i).DonutRadius, null);
                    if (donutList.get(i).color_id == 'c')
                        canvas.drawBitmap(chocdonut, donutList.get(i).x - donutList.get(i).DonutRadius, donutList.get(i).y - donutList.get(i).DonutRadius, null);
                    if (donutList.get(i).color_id == 'b')
                        canvas.drawBitmap(bdonut, donutList.get(i).x - donutList.get(i).DonutRadius, donutList.get(i).y - donutList.get(i).DonutRadius, null);
                }
                drawMesseges();/**/
                break;

            case GAME_OVER:
                drawGameOver();
                break;

            case GAME_COMPLETE:
                drawGameComplete();
                break;

            /*case GAME_LANDSCAPE:
                drawLandscapeMode();
                break;*/
        }
        gameOnEachTick();
    }

    public void drawMesseges() {
        canvasPaint.setColor(Color.GREEN);
        canvasPaint.setTextAlign(Paint.Align.LEFT);
        gameCanvas.drawText(gameStrings[Game_TIME] + ": " + (gameTotalTime / 1000), fontTextPadding, gameHudTextY,
                canvasPaint);
        canvasPaint.setTextAlign(Paint.Align.CENTER);
        gameCanvas.drawText(gameStrings[Game_LEVEL] + ": " + gamelevel, gameCanvasHalfWidth, gameHudTextY, canvasPaint);
        canvasPaint.setTextAlign(Paint.Align.RIGHT);
        gameCanvas.drawText(gameStrings[Game_LIVES] + ": " + gameFatman.getLives(), gameCanvasWidth - fontTextPadding,
                gameHudTextY, canvasPaint);
        if (gameWarning) {
            canvasPaint.setColor(Color.BLUE);
            gameCanvas
                    .drawRect(0, gameCanvasHalfHeight - 15, gameCanvasWidth, gameCanvasHalfHeight + 5,
                            canvasPaint);
            canvasPaint.setColor(Color.WHITE);
            canvasPaint.setTextAlign(Paint.Align.CENTER);
            gameCanvas.drawText(gameStrings[Game_TAP_SCREEN], gameCanvasHalfWidth, gameCanvasHalfHeight, canvasPaint);
        }
    }
    public void drawGameOver() {
        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setTextAlign(Paint.Align.CENTER);

        gameCanvas.drawText(gameStrings[Game_GAME_OVER], gameCanvasHalfWidth, gameCanvasHalfHeight, canvasPaint);
        gameCanvas.drawText(gameStrings[Game_TOTAL_TIME] + ": " + (gameTotalTime / 1000) + "s",
                gameCanvasHalfWidth, gameCanvasHalfHeight + canvasPaint.getFontSpacing(), canvasPaint);
        gameCanvas.drawText(gameStrings[Game_GAME_OVER_MSG_A] + " " + (gamelevel - 1) + " "
                + gameStrings[Game_GAME_OVER_MSG_B], gameCanvasHalfWidth, gameCanvasHalfHeight
                + (canvasPaint.getFontSpacing() * 2), canvasPaint);

        gameCanvas.drawText(gameStrings[Game_RESTART], gameCanvasHalfWidth, gameCanvasHeight
                - (canvasPaint.getFontSpacing() * 3), canvasPaint);
        gameCanvas.translate(0, 50);
    }


    public void drawGameComplete() {
        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setTextAlign(Paint.Align.CENTER);
        gameCanvas.drawText(gameStrings[GAME_COMPLETE], gameCanvasHalfWidth, gameCanvasHalfHeight, canvasPaint);
        gameCanvas.drawText(gameStrings[Game_TOTAL_TIME] + ": " + (gameTotalTime / 100) + "s",
                gameCanvasHalfWidth, gameCanvasHalfHeight + canvasPaint.getFontSpacing(), canvasPaint);
        gameCanvas.drawText(gameStrings[Game_RESTART], gameCanvasHalfWidth, gameCanvasHeight
                - (canvasPaint.getFontSpacing() * 3), canvasPaint);
    }


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

    public void cleanUp() {
        gameFatman = null;
        gameOffice = null;
        gameStrings = null;
        unregisterListener();
        gameActivity.finish();
    }
}


