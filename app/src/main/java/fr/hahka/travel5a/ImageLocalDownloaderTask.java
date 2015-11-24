package fr.hahka.travel5a;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

import fr.hahka.travel5a.utils.ImageUtils;

/**
 * Created by thibautvirolle on 23/11/15.
 * AsyncTask servant à récupérer puis affichée une image locale dans une ImageView
 */
public class ImageLocalDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private final Context mContext;

    /**
     * Constructeur de l'asynctask pour récupérer une image locale
     * @param imageView : référence de l'imageView que nous voulons modifier
     * @param context : context dans lequel l'AsyncTask est créé
     */
    public ImageLocalDownloaderTask(ImageView imageView, Context context) {

        imageViewReference = new WeakReference<ImageView>(imageView);
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        return ImageUtils.getOrientedAndScaledBitmap(
                new File(params[0]),
                mContext.getResources().getDisplayMetrics().widthPixels
        );
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        Bitmap bmp = bitmap;
        if (isCancelled()) {
            bmp = null;
        }

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
}
