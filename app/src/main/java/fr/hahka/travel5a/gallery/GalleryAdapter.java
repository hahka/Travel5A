package fr.hahka.travel5a.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import fr.hahka.travel5a.ImageLocalDownloaderTask;
import fr.hahka.travel5a.R;

/**
 * Created by thibautvirolle on 22/11/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {


    private static final String TAG = GalleryAdapter.class.getSimpleName();

    private ArrayList listData;
    private Context mContext;

    /**
     * "Adapter" pour afficher une gallerie des images (géolocalisées) déjà présentes sur l'appareil
     * @param context : Context dans lequel l'adapter a été créé
     * @param listData : liste de donnée à afficher grâce à l'adapteur
     */
    public GalleryAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        mContext = context;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.activity_gallery_item, parent, false);

        //itemView.setLayoutParams(new GridView.LayoutParams(5, 100));

        return new GalleryViewHolder(itemView, parent.getContext());


    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {

        File file = (File) listData.get(position);

        new ImageLocalDownloaderTask(holder.galleryImageView, mContext).execute(file.getPath());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    /**
     * ViewHolder servant à garder une référence sur la vue associé à un objet
     */
    public static class GalleryViewHolder extends RecyclerView.ViewHolder {

        protected ImageView galleryImageView;

        /**
         * Constructeur pour le ViewHolder
         * @param v : vue associée au viewholder
         * @param c : context dans lequel le viewholder est créé
         */
        public GalleryViewHolder(View v, final Context c) {
            super(v);

            galleryImageView = (ImageView) v.findViewById(R.id.galleryItemView);

            /*Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotationEcran = display.getRotation();
            // Et positionner ainsi le nombre de degrés de rotation
            if (rotationEcran == Surface.ROTATION_90 || rotationEcran == Surface.ROTATION_270) {

            }*/

        }
    }

}