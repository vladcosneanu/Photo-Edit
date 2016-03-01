package com.patrau.roxana.photoedit.interfaces;

import android.graphics.Bitmap;

public interface CanvasSaver {

    public void onCanvasSaveFailed();

    public void onCanvasSaveSuccess(String canvasFileName);
}
