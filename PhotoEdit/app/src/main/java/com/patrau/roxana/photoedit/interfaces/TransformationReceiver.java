package com.patrau.roxana.photoedit.interfaces;

import android.graphics.Bitmap;

/**
 * This interface defines a transformation receiver
 */
public interface TransformationReceiver {

    public void onTransformationProgress(float progress);

    public void onTransformationComplete(Bitmap bitmap);
}
