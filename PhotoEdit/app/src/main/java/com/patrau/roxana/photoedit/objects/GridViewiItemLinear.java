package com.patrau.roxana.photoedit.objects;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GridViewiItemLinear extends LinearLayout {

    public GridViewiItemLinear(Context context) {
        super(context);
    }

    public GridViewiItemLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewiItemLinear(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}