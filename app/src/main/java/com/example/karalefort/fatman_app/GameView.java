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
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.hardware.SensorManager;
import android.hardware.SensorListener;

public class GameView extends View
{
    private Bitmap fatman;
    private Bitmap pdonut;
    private Bitmap bdonut;
    private Bitmap chocdonut;

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
    private final static int GAME_LANDSCAPE = 4;
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
    private final static int Game_LANDSCAPE_MODE = 10;

    private int fontTextPadding = 10;
    private int gameHudTextY = 440;

    private static String gameStrings[];
    private boolean gameWarning = false;
    private int gameCanvasWidth = 0;
    private int gameCanvasHeight = 0;
    private int gameCanvasHalfWidth = 0;
    private int gameCanvasHalfHeight = 0;
    private boolean orientationPortrait;
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

        gameActivity = activity;
        pdonut = BitmapFactory.decodeResource(getResources(), R.drawable.pinkdonut);

        bdonut = BitmapFactory.decodeResource(getResources(), R.drawable.bluedonut);
        chocdonut = BitmapFactory.decodeResource(getResources(), R.drawable.chocolatedonut);
        fatman = BitmapFactory.decodeResource(getResources(), R.drawable.fatman);
        gameSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        gameSensorManager.registerListener(gameSensorAccelerometer, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        gameOffice = new Office(gameActivity);
        gameFatman = new Fatman(this);
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
        if (gameCanvasHeight > gameCanvasWidth)
            orientationPortrait = true;
        else {
            orientationPortrait = false;
            changeState(GAME_LANDSCAPE);
        }
    }


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
            gameWarning = true;
            gamelevel++;
            gameOffice.load(gameActivity, gamelevel);
            gameFatman.init();
        }

        else {
            changeState(GAME_COMPLETE);
        }
    }



    public void updateFatmanPosition() {
        if (gameAccelX > gameSensorBuffer || gameAccelX < -gameSensorBuffer)
            gameFatman.updatePositionX(gameAccelX);
        if (gameAccelY > gameSensorBuffer || gameAccelY < -gameSensorBuffer)
            gameFatman.updatePositionY(gameAccelY);
        if (gameOffice.getCellType(gameFatman.getX(), gameFatman.getY()) == gameOffice.VOID_TILE) {
            if (gameFatman.getLives() > 0) {
                gameFatman.marbleDies();
                gameFatman.init();
                gameWarning = true;
            } else {
                gameEndTime = System.currentTimeMillis();
                gameTotalTime += gameEndTime - levelStartTime;
                changeState(GAME_OVER);
            }

        } else if (gameOffice.getCellType(gameFatman.getX(), gameFatman.getY()) == gameOffice.EXIT_TILE) {
            gameEndTime = System.currentTimeMillis();
            gameTotalTime += gameEndTime - levelStartTime;
            startLevel();
        }
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
                //gameFatman.draw(gameCanvas);
                //canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(fatman, Fatman.x, Fatman.y, null);
                drawMesseges();
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


