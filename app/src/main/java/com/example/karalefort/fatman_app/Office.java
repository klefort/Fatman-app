package com.example.karalefort.fatman_app;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;


public class Office
{

    public final static int TILE_SIZE = 25;
    public final static int OFFICE_COLUMNS = 20;
    public final static int OFFICE_ROWS = 26;
    public final static int PATH_TILE = 0;
    public final static int VOID_TILE = 1;
    public final static int EXIT_TILE = 2;
    public final static int BEETLE_TILE = 3;
    private final static int VOID_COLOR = Color.LTGRAY;
    private final static int BEETLE_COLOR = Color.GREEN;
    private Rect officeRectangle = new Rect();
    private static int[] OfficeArray;
    public final static int MAX_LEVELS = 10;
    private int officeRow;
    private int officeColumn;
    private int officeScreenX;
    private int officeScreenY;

    Office(Activity activity) {
    }

    void load(Activity activity, int newLevel)
    {
        String olevel = "level" + newLevel + ".txt";
        AssetManager am = activity.getAssets();
        InputStream is = null;
        try {

            OfficeArray = new int[OFFICE_ROWS * OFFICE_COLUMNS];

            is = am.open(olevel);
            for (int i = 0; i < OfficeArray.length; i++) {
                OfficeArray[i] = Character.getNumericValue(is.read());
                is.read();
                is.read();
            }
        } catch (Exception e) {

        } finally {
            closeStream(is);
        }
    }

    public void draw(Canvas canvas, Paint paint)
    {
        for (int i = 0; i < OfficeArray.length; i++)
        {
            officeRow = i / OFFICE_COLUMNS;
            officeColumn = i % OFFICE_COLUMNS;
            officeScreenX = officeColumn * TILE_SIZE;
            officeScreenY = officeRow * TILE_SIZE;
            paint.setColor(Color.CYAN);
            if (OfficeArray[i] == PATH_TILE)
            {
                canvas.drawRect(officeScreenX, officeScreenY, officeScreenX + TILE_SIZE, officeScreenY + TILE_SIZE, paint);
            }
            else if (OfficeArray[i] == EXIT_TILE)
            {
                paint.setColor(Color.RED);
                canvas.drawRect(officeScreenX, officeScreenY, officeScreenX + TILE_SIZE, officeScreenY + TILE_SIZE, paint);
                //paint.setColor(Color.GREEN);
            }
            else if (OfficeArray[i] == VOID_TILE)
            {
                officeRectangle.left = officeScreenX;
                officeRectangle.top = officeScreenY;
                officeRectangle.right = officeScreenX + TILE_SIZE;
                officeRectangle.bottom = officeScreenY + TILE_SIZE;

                paint.setColor(VOID_COLOR);
                canvas.drawRect(officeRectangle, paint);
            }
            else if (OfficeArray[i] == BEETLE_TILE)
            {
                officeRectangle.left = officeScreenX;
                officeRectangle.top = officeScreenY;
                officeRectangle.right = officeScreenX + TILE_SIZE;
                officeRectangle.bottom = officeScreenY + TILE_SIZE;

                paint.setColor(BEETLE_COLOR);
                canvas.drawRect(officeRectangle, paint);
            }
        }
    }

    public int getCellType(int x, int y)
    {
        int mCellCol = x / TILE_SIZE;
        int mCellRow = y / TILE_SIZE;
        int mLocation = 0;
        if (mCellRow > 0)
        {
            mLocation = mCellRow * OFFICE_COLUMNS;
        }
        mLocation += mCellCol;
        return OfficeArray[mLocation];
    }


    private static void closeStream(Closeable stream)
    {
        if (stream != null)
        {
            try {
                stream.close();

            } catch (IOException e) {
            }
        }
    }
    public static int x = 50;
    public static int y = 100;
}
