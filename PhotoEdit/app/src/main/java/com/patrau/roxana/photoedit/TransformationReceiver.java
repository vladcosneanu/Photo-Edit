package com.patrau.roxana.photoedit;

import android.graphics.Bitmap;

public interface TransformationReceiver {

    public void onTransformationProgress(float progress);

    public void onTransformationComplete(Bitmap bitmap);
}
