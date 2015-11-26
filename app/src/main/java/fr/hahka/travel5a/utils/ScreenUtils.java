package fr.hahka.travel5a.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by thibautvirolle on 25/11/15.
 */
public class ScreenUtils {

    private static final String TAG = ScreenUtils.class.getSimpleName();
    private static int sScreenWidthDP = -1;
    private static int sScreenWidth = -1;
    private static int sScreenHeight = -1;


    public static double getScreenWidthInInchWithOrientation(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        double displayWidthInInch;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "1");
            Log.d(TAG, String.valueOf(getScreenWidth(context)));
            Log.d(TAG, String.valueOf(getScreenHeight(context)));
            /*Log.d(TAG, String.valueOf(metrics.ydpi));*/
            displayWidthInInch = getScreenHeight(context) / metrics.ydpi;
            Log.d(TAG, String.valueOf(displayWidthInInch));

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "2");
            Log.d(TAG, String.valueOf(getScreenWidth(context)));
            Log.d(TAG, String.valueOf(getScreenHeight(context)));
            /*Log.d(TAG, String.valueOf(metrics.xdpi));*/
            displayWidthInInch = getScreenWidth(context) / metrics.xdpi;
            Log.d(TAG, String.valueOf(displayWidthInInch));

        } else {
            Log.d(TAG, "3");
            displayWidthInInch = getScreenWidth(context) / metrics.ydpi;
        }
        return displayWidthInInch;
        /*DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        double xDpi = metrics.xdpi;
        double yDpi = metrics.ydpi;
        double screenWidthInch = getScreenWidth(context)/xDpi;
        double screenHeightInch = getScreenHeight(context)/yDpi;
        return Math.sqrt(screenWidthInch*screenWidthInch + screenHeightInch*screenHeightInch);*/
    }


    public static double getActualScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        double xDpi = metrics.xdpi;
        double yDpi = metrics.ydpi;
        double screenWidthInch = getScreenWidth(context)/xDpi;
        double screenHeightInch = getScreenHeight(context)/yDpi;
        return Math.sqrt(screenWidthInch*screenWidthInch + screenHeightInch*screenHeightInch);
    }

    public static int getScreenHeight(Context context) {
        if (sScreenHeight == -1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenHeight = size.y;
        }
        return sScreenHeight;
    }

    public static int getScreenHeightInDp(Context context) {
        if (sScreenHeight == -1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenHeight = size.y;
        }
        return pixelsToDp(context, sScreenHeight);
    }

    public static int getScreenWidthInDp(Context context) {
        if (sScreenWidthDP == -1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenWidthDP = pixelsToDp(context, size.x);
        }
        return sScreenWidthDP;
    }
    public static int getScreenWidth(Context context) {
        if (sScreenWidth == -1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenWidth = size.x;
        }

        return sScreenWidth;
    }
    public static float dpToPixels(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int pixelsToDp(Context context, float pixels) {
        float density = context.getResources().getDisplayMetrics().densityDpi;
        return Math.round(pixels / (density / 160f));
    }


}
