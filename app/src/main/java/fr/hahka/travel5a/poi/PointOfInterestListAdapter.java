package fr.hahka.travel5a.poi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.utils.GeocoderUtils;

/**
 * Created by thibautvirolle on 17/11/15.
 * Classe servant à afficher les points d'intérêt dans la ListView "principale"
 */
public class PointOfInterestListAdapter extends BaseAdapter {

    private static final String TAG = PointOfInterestListAdapter.class.getSimpleName();

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private boolean local;
    private Context mContext;

    public PointOfInterestListAdapter(Context context, ArrayList listData, boolean local) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
        this.local = local;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.moments_list_row, null);
            holder = new ViewHolder();
            holder.descriptionView = (TextView) convertView.findViewById(R.id.publicationDescription);
            holder.secondaryTextView = (TextView) convertView.findViewById(R.id.secondaryText);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PointOfInterest poi = (PointOfInterest) listData.get(position);


        ArrayList<String> myAddress = GeocoderUtils.getAddressFromLocation(mContext, poi.getLatitude(), poi.getLongitude());


        holder.descriptionView.setText(poi.getDescription());

        holder.secondaryTextView.setText(
                GeocoderUtils.getFormatedAddressFromLocation(
                        mContext,
                        poi.getLatitude(),
                        poi.getLongitude(),
                        "Ci, Co"));

        if (holder.imageView != null) {
            new ImageDownloaderTask(holder.imageView).execute(poi.getImagePath());
        }

        Log.d(TAG, "ID : " + poi.getId());

        return convertView;
    }

    static class ViewHolder {
        TextView descriptionView;
        TextView secondaryTextView;
        ImageView imageView;
    }


    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        public Bitmap doInBackground(String... params) {
            if (local) {
                return BitmapFactory.decodeFile(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + File.separator + Config.IMAGE_DIRECTORY_NAME
                        + File.separator + params[0]);
            } else {
                return downloadBitmap(params[0]);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder =
                                imageView.getContext().getResources()
                                        .getDrawable(R.drawable.placeholder);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            } else {
                Log.w("ImageDownloader", "Error downloading image from " + url);
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }


    public void refreshData(ArrayList<PointOfInterest> data) {
        this.listData.clear();
        this.listData = data;
        notifyDataSetChanged();
    }
}
