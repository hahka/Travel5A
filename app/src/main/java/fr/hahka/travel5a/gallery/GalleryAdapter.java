package fr.hahka.travel5a.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import fr.hahka.travel5a.R;
import fr.hahka.travel5a.utils.download.ImageLocalDownloaderTask;

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

        return new GalleryViewHolder(itemView, parent.getContext());


    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {

        holder.galleryImageView.setImageDrawable(null);

        File file = (File) listData.get(position);

        holder.task.cancel(true);
        holder.task = new ImageLocalDownloaderTask(holder.galleryImageView, mContext);
        holder.task.execute(file.getPath());

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

        protected GalleryImageView galleryImageView;

        protected ImageLocalDownloaderTask task;

        /**
         * Constructeur pour le ViewHolder
         * @param v : vue associée au viewholder
         * @param c : context dans lequel le viewholder est créé
         */
        public GalleryViewHolder(View v, final Context c) {
            super(v);

            galleryImageView = (GalleryImageView) v.findViewById(R.id.galleryItemView);
            task = new ImageLocalDownloaderTask(galleryImageView, c);
        }
    }

}