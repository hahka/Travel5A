package fr.hahka.travel5a.poi;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thibautvirolle on 25/11/15.
 */
public class PointOfInterestCustomClicker
        implements RecyclerView.OnClickListener, RecyclerView.OnLongClickListener {

    private static final String TAG = PointOfInterestCustomClicker.class.getSimpleName();

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    /**
     * Interface pour gérer les clicks
     */
    public interface OnItemClickListener {
        /**
         * Click sur une view
         * @param view : la vue en question
         */
        public void onItemClick(View view);
    }

    /**
     * Interface pour gérer les longs clicks
     */
    public interface OnItemLongClickListener {
        /**
         * Lon click sur une view
         * @param view : la vue en question
         */
        public void onItemLongClick(View view);
    }


}