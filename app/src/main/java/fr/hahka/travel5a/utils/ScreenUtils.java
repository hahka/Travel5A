package fr.hahka.travel5a.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by thibautvirolle on 25/11/15.
 */
public final class ScreenUtils {

    private static final String TAG = ScreenUtils.class.getSimpleName();
    private static int sScreenWidth = -1;
    private static int sScreenHeight = -1;

    /**
     * Les classes utilitaires ne doivent pas avoir de constructeur public par défaut
     */
    private ScreenUtils() { }

    /**
     * Récupère la largeur en pouces de l'écran suivant son orientation
     * @param context : context blablabla...
     * @return : la largeur d'affichage dispo
     */
    public static double getScreenWidthInInchWithOrientation(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        double displayWidthInInch;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "1");
            Log.d(TAG, String.valueOf(getScreenWidth(context)));
            Log.d(TAG, String.valueOf(getScreenHeight(context)));
            displayWidthInInch = getScreenHeight(context) / metrics.ydpi;
            Log.d(TAG, String.valueOf(displayWidthInInch));

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "2");
            Log.d(TAG, String.valueOf(getScreenWidth(context)));
            Log.d(TAG, String.valueOf(getScreenHeight(context)));
            displayWidthInInch = getScreenWidth(context) / metrics.xdpi;
            Log.d(TAG, String.valueOf(displayWidthInInch));

        } else {
            Log.d(TAG, "3");
            displayWidthInInch = getScreenWidth(context) / metrics.ydpi;
        }
        return displayWidthInInch;
    }

    /**
     * Récupère la hauteur de l'écran
     * @param context : context pour obtenir les infos sur l'écran
     * @return : hauteur de l'écran
     */
    public static int getScreenHeight(Context context) {
        if (sScreenHeight == -1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenHeight = size.y;
        }
        return sScreenHeight;
    }


    /**
     * Récupère la largeur de l'écran
     * @param context : context pour obtenir les infos sur l'écran
     * @return : largeur de l'écran
     */
    public static int getScreenWidth(Context context) {
        if (sScreenWidth == -1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenWidth = size.x;
        }

        return sScreenWidth;
    }

}
