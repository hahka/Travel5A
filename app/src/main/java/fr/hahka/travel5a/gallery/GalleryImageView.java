package fr.hahka.travel5a.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by thibautvirolle on 24/11/15.
 */
public class GalleryImageView extends ImageView {

    private float mScale = 1f;

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryImageView(Context context) {
        super(context);
    }

    public void setScale(int scale) {
        mScale = scale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width * mScale));
    }
}