package com.example.karalefort.fatman_app;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.lang.Object.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;
import java.util.ArrayList;



public class Office
{

    public int TILE_SIZEX = 20;
    public int TILE_SIZEY = 20;
    public final static int OFFICE_COLUMNS = 20;
    public final static int OFFICE_ROWS = 32;
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
//    public ArrayList<Integer> beetlex = new ArrayList<Integer>();
//    public ArrayList<Integer> beetley = new ArrayList<Integer>();

//    public int beetlex;
//    public int beetley;

    Office(Activity activity, int mwidth, int mheight) {
//        final double tile;
//        tile = (mwidth / OFFICE_COLUMNS * mheight / OFFICE_ROWS);
//
//     int TILE_SIZE = (int)tile;
        TILE_SIZEX =(int)(mwidth / OFFICE_COLUMNS);
        TILE_SIZEY =(int)(mheight / (OFFICE_ROWS + 4));

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
//                is.read();
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
            officeScreenX = officeColumn * TILE_SIZEX;
            officeScreenY = officeRow * TILE_SIZEY;
            paint.setColor(Color.CYAN);
            if (OfficeArray[i] == PATH_TILE)
            {
                canvas.drawRect(officeScreenX, officeScreenY, officeScreenX + TILE_SIZEX, officeScreenY + TILE_SIZEY, paint);
            }
            else if (OfficeArray[i] == EXIT_TILE)
            {
                paint.setColor(Color.RED);
                canvas.drawRect(officeScreenX, officeScreenY, officeScreenX + TILE_SIZEX, officeScreenY + TILE_SIZEY, paint);
                //paint.setColor(Color.GREEN);
            }
            else if (OfficeArray[i] == VOID_TILE)
            {
                officeRectangle.left = officeScreenX;
                officeRectangle.top = officeScreenY;
                officeRectangle.right = officeScreenX + TILE_SIZEX;
                officeRectangle.bottom = officeScreenY + TILE_SIZEY;

                paint.setColor(VOID_COLOR);
                canvas.drawRect(officeRectangle, paint);
            }
            else if (OfficeArray[i] == BEETLE_TILE)
            {
//                beetlex[i] = officeScreenX;
//                beetley[i] = officeScreenY;
                officeRectangle.left = officeScreenX;
                officeRectangle.top = officeScreenY;
                officeRectangle.right = officeScreenX + TILE_SIZEX;
                officeRectangle.bottom = officeScreenY + TILE_SIZEY;

                paint.setColor(BEETLE_COLOR);
                canvas.drawRect(officeRectangle, paint);
            }
        }
    }

    public int getCellType(int x, int y)
    {
        int mCellCol = x / TILE_SIZEX;
        int mCellRow = y / TILE_SIZEY;
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
