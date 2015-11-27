package fr.hahka.travel5a.utils.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

import fr.hahka.travel5a.R;

/**
 * A utility that downloads a file from a URL.
 *
 * @author www.codejava.net
 */
public  class FtpDownloadUtils extends AsyncTask<String, Void, Bitmap> {

    private static final int BUFFER_SIZE = 4096;
    private final WeakReference<ImageView> imageViewReference;
    private final Context mContext;


    /**
     * Constructeur par défaut
     * @param imageView : référence à l'imageview à afficher
     * @param context : context d'où est appelé la fonction
     */
    public FtpDownloadUtils(ImageView imageView, Context context) {

        imageViewReference = new WeakReference<ImageView>(imageView);
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            Log.d("test", "ok");
            return downloadFileToBitmap(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("test", "ko");
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {

        Bitmap bmp = bitmap;
        if (isCancelled()) {
            bmp = null;
        }
        Log.d("test", String.valueOf(bmp != null));

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bmp != null) {
                    imageView.setImageBitmap(bmp);
                } else {
                    Drawable placeholder = imageView.getContext()
                            .getResources().getDrawable(R.drawable.placeholder);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }


    /**
     * Downloads a file from a URL
     *
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException si le chemin spécifié n'existe pas
     * @return (String) le chemin du fichier créé
     */
    public static String downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        URLConnection httpConn = url.openConnection();

        String fileName = "";
        String disposition = httpConn.getHeaderField("Content-Disposition");
        String contentType = httpConn.getContentType();
        int contentLength = httpConn.getContentLength();

        if (disposition != null) {
            // extracts file name from header field
            int index = disposition.indexOf("filename=");
            if (index > 0) {
                fileName = disposition.substring(index + 10,
                        disposition.length() - 1);
            }
        } else {
            // extracts file name from URL
            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                    fileURL.length());
        }

        System.out.println("Content-Type = " + contentType);
        System.out.println("Content-Disposition = " + disposition);
        System.out.println("Content-Length = " + contentLength);
        System.out.println("fileName = " + fileName);

        // opens input stream from the HTTP connection
        InputStream inputStream = httpConn.getInputStream();
        String saveFilePath = saveDir + File.separator + fileName;

        // opens an output stream to save into file
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        return saveFilePath;

    }


    /**
     * Downloads a file from a URL
     *
     * @param fileURL HTTP URL of the file to be downloaded
     * @throws IOException si le chemin spécifié n'existe pas
     * @return (String) le chemin du fichier créé
     */
    public static Bitmap downloadFileToBitmap(String fileURL)
            throws IOException {
        URL url = new URL(fileURL);
        URLConnection httpConn = url.openConnection();

        String fileName = "";
        String disposition = httpConn.getHeaderField("Content-Disposition");
        String contentType = httpConn.getContentType();
        int contentLength = httpConn.getContentLength();

        if (disposition != null) {
            // extracts file name from header field
            int index = disposition.indexOf("filename=");
            if (index > 0) {
                fileName = disposition.substring(index + 10,
                        disposition.length() - 1);
            }
        } else {
            // extracts file name from URL
            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                    fileURL.length());
        }

        System.out.println("Content-Type = " + contentType);
        System.out.println("Content-Disposition = " + disposition);
        System.out.println("Content-Length = " + contentLength);
        System.out.println("fileName = " + fileName);

        // opens input stream from the HTTP connection
        InputStream inputStream = httpConn.getInputStream();
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        inputStream.close();

        return bmp;

    }





}
