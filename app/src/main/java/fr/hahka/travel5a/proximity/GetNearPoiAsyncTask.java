package fr.hahka.travel5a.proximity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import fr.hahka.travel5a.R;

/**
 * Created by thibautvirolle on 27/11/15.
 */
public class GetNearPoiAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    private final Context mContext;

    /**
     * Constructeur de l'asynctask pour récupérer une image locale
     * @param imageView : référence de l'imageView que nous voulons modifier
     * @param context : context dans lequel l'AsyncTask est créé
     */
    public GetNearPoiAsyncTask(ImageView imageView, Context context) {

        imageViewReference = new WeakReference<ImageView>(imageView);
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return null;
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
