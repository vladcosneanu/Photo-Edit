package com.patrau.roxana.photoedit.interfaces;

import android.graphics.Bitmap;

/**
 * This interface defines a canvas saver
 */
public interface CanvasSaver {

    public void onCanvasSaveFailed();

    public void onCanvasSaveSuccess(String canvasFileName);
}
