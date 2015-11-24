package fr.hahka.travel5a.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.hahka.travel5a.Config;

/**
 * Created by thibautvirolle on 13/11/15.
 */
public final class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    /**
     * Les classes utilitaires ne doivent pas avoir de constructeur public ou par défaut
     */
    private ImageUtils() { }

    /**
     * Fonction retournant un bitmap ré-orienté et redimentionné,
     * les images uploadées ayant parfois une mauvaise orientation.
     * Ce problème est réglable grâce aux propriétés EXIF de l'image
     * @param image : fichier à orienter et redimentionner
     * @param screenWidth : largeur de l'écran du device Android
     * @return (Bitmap) L'image traitée
     */
    public static Bitmap getOrientedAndScaledBitmap(File image, int screenWidth) {

        Bitmap bitmap = null;
        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(image.getAbsolutePath());
            switch(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1))
            {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = -90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;

                default:
                    rotation = 0;
                    break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

        int height = (screenWidth * bitmap.getHeight()) / bitmap.getWidth();
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, height, true);

        int scaledWidth = bitmap.getWidth();
        int scaledHeight = bitmap.getHeight();
        int xStart = 0;
        int yStart = 0;

        if (scaledHeight > scaledWidth)
        {
            yStart = (scaledHeight - scaledWidth) / 2;
            scaledHeight = scaledWidth;
        } else
        {
            xStart = (scaledWidth - scaledHeight) / 2;
            scaledWidth = scaledHeight;
        }

        Matrix matrix = new Matrix();

        matrix.postRotate(rotation);

        return Bitmap.createBitmap(bitmap, xStart, yStart, scaledWidth, scaledHeight, matrix, true);
    }


    /**
     * Enregistre une image bitmap dans la mémoire interne de l'appareil
     * @param bitmapImage L'image à enregistrer
     * @param fileName Le nom à donner à l'image
     * @return Le chemin du répertoire dans lequel l'image a été enregistrée
     */
    public static String saveToInternalSorage(Bitmap bitmapImage, String fileName) {

        File directory = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Creation du repertoire s'il n'existe pas
        if (!FileUtils.createFolderIfNotExists(directory))
            return null;

        // Creation de l'image
        File mypath = new File(directory.getPath() + File.separator + fileName);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
}
