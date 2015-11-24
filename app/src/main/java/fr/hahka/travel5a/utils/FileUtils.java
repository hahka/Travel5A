package fr.hahka.travel5a.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thibautvirolle on 09/11/15.
 */
public final class FileUtils {

    /**
     * Les classes utilitaires ne doivent pas avoir de constructeur public ou par défaut
     */
    private FileUtils() { }

    /**
     * Permet de générer une string qui est l'encodage en base64 d'un fichier
     * @param file Le fichier à encoder
     * @return L'image encodée en base64 (String)
     * @throws FileNotFoundException quand le fichier n'est pas trouvé
     */
    public static String getEncodedFile(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file.getPath());
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Encode un bitmap en string base64
     * @param bitmap le bitmap à encoder
     * @return une string correspondant au bitmap encodé en base64
     */
    public static String getEncodedBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    /**
     * Crée un dossier si celui-ci n'existe pas
     * @param file : le dossier en question
     * @return (Boolean) True si le dossier existait déjà ou si il a été créé, False sinon
     */
    public static boolean createFolderIfNotExists(File file) {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }
        return true;
    }
}
