package fr.hahka.travel5a.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by thibautvirolle on 24/11/15.
 */
public class GalleryRelativeLayout extends RelativeLayout {

    private float mScale = 1f;

    /**
     * Constructeur par défaut de l'objet
     * @param context : Context dans lequel l'objet est créé
     * @param attrs : Attributs pour la création de l'objet
     * @param defStyleAttr : Style pour la création de l'objet
     */
    public GalleryRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Constructeur par défaut de l'objet
     * @param context : Context dans lequel l'objet est créé
     * @param attrs : Attributs pour la création de l'objet
     */
    public GalleryRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructeur par défaut de l'objet
     * @param context : Context dans lequel l'objet est créé
     */
    public GalleryRelativeLayout(Context context) {
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

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            child.invalidate();
            child.setBottom(0);
            child.invalidate();
        }
    }
}