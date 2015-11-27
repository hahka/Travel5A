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

    /**
     * Constructeur de l'item clicker
     * @param context : context dans lequel la fonction st appelée
     * @param listener : listener pour récupérer les clicks
     */
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


    /**
     * Interface pour gérer les clicks
     */
    public interface OnItemClickListener {
        /**
         * Fonction surchérgée dans l'activité opur définir l'action
         * @param view : vue cliquée
         * @param position : position de la vue cliquée
         */
        public void onItemClick(View view, int position);
    }

}