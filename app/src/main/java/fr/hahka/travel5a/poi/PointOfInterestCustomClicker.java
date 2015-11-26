package fr.hahka.travel5a.poi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by thibautvirolle on 25/11/15.
 */
public class PointOfInterestCustomClicker
        implements RecyclerView.OnClickListener, RecyclerView.OnLongClickListener {

    private static final String TAG = PointOfInterestCustomClicker.class.getSimpleName();
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public interface OnItemClickListener {
        public void onItemClick(View view);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View view);
    }

    GestureDetector mGestureDetector;

    public PointOfInterestCustomClicker(Context context,
                                        OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public PointOfInterestCustomClicker(Context context,
                                        OnItemClickListener listener,
                                        OnItemLongClickListener longListener) {
        mListener = listener;
        mLongListener = longListener;
    }


    @Override
    public void onClick(View v) {
        if (v != null && mListener != null) {
            mListener.onItemClick(v);
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if (v != null && mLongListener != null) {
            mLongListener.onItemLongClick(v);
            return true;
        }
        return false;
    }

}