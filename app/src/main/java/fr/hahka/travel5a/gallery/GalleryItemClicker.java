package fr.hahka.travel5a.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by thibautvirolle on 24/11/15.
 * Classe pour gérer les clicks sur la gridView de la galerie
 */
public class GalleryItemClicker implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public GalleryItemClicker(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

}