package fr.hahka.travel5a.poi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.travel5a.R;
import fr.hahka.travel5a.utils.GeocoderUtils;
import fr.hahka.travel5a.utils.download.ImageLocalDownloaderTask;

/**
 * Created by thibautvirolle on 17/11/15.
 * Classe servant à afficher les points d'intérêt dans la ListView "principale"
 */
public class PointOfInterestListAdapter extends RecyclerView.Adapter<PointOfInterestListAdapter.ViewHolder> {

    private static final String TAG = PointOfInterestListAdapter.class.getSimpleName();

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private boolean local;
    private Context mContext;

    private PointOfInterestFragment mFragment;

    /**
     * Constructeur de l'adapter
     * @param context : context dans lequel il est appelé
     * @param fragment : fragment ou est utilisé l'adapter
     * @param listData : les données à adapter
     * @param local : les données sont-elles locales? TODO (Oui pour l'instant)
     */
    public PointOfInterestListAdapter(Context context,
                                      PointOfInterestFragment fragment,
                                      ArrayList listData,
                                      boolean local) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
        this.local = local;
        this.mContext = context;
        this.mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.moments_list_row, parent, false);

        return new ViewHolder(itemView, parent.getContext());


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        PointOfInterest poi = (PointOfInterest) listData.get(position);

        Log.d(TAG, poi.getImagePath());
        new ImageLocalDownloaderTask(holder.thumbImage, mContext).execute(poi.getImagePath());

        holder.descriptionView.setText(poi.getDescription());

        holder.secondaryTextView.setText(
                GeocoderUtils.getFormatedAddressFromLocation(
                        mContext,
                        poi.getLatitude(),
                        poi.getLongitude(),
                        "Ci, Co"));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.onItemClick(v);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mFragment.onItemLongClick(v);
                return true;
            }
        });

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
     * Sert à actualiser la liste après une modification
     * @param data : liste des POIs
     */
    public void refreshData(ArrayList<PointOfInterest> data) {
        this.listData.clear();
        this.listData = data;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder servant à garder une référence sur la vue associé à un objet
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thumbImage;
        protected TextView descriptionView;
        protected TextView secondaryTextView;
        protected Context mContext;
        protected View view;

        /**
         * Constructeur pour le ViewHolder
         * @param v : vue associée au viewholder
         * @param c : context dans lequel le viewholder est créé
         */
        public ViewHolder(View v, final Context c) {
            super(v);

            view = v;

            mContext = c;
            thumbImage = (ImageView) v.findViewById(R.id.thumbImage);
            descriptionView = (TextView) v.findViewById(R.id.publicationDescription);
            secondaryTextView = (TextView) v.findViewById(R.id.secondaryText);


        }
    }

}
