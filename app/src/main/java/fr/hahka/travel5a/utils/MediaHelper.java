package fr.hahka.travel5a.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.hahka.travel5a.Config;

/**
 * Created by thibautvirolle on 09/11/15.
 */
public final class MediaHelper {


    /**
     * LogCat tag
     */
    private static final String TAG = MediaHelper.class.getSimpleName();


    /**
     * Les classes utilitaires ne doivent pas avoir de constructeur public ou par défaut
     */
    private MediaHelper() { }


    /**
     * Création d'une URI pour stocker l'image ou la vidéo
     * @param type : type de fichier à créer (image, vidéo...)
     * @return (URI) L'uri pour le fichier à créer
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     * @param type : type de fichier à créer (image, vidéo...)
     * @return : le fichier créé en fonction de si c'est une photo ou une vidéo
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Config.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == Config.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
