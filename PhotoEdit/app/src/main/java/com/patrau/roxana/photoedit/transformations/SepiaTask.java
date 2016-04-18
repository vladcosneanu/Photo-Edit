package com.patrau.roxana.photoedit.transformations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.interfaces.TransformationReceiver;

public class SepiaTask extends AsyncTask<String, Float, Bitmap> {

    // constant factors
    public static final float DEFAULT_RED = 0.299f;
    public static final float DEFAULT_GREEN = 0.587f;
    public static final float DEFAULT_BLUE = 0.114f;

    private Bitmap inputBitmap;
    private TransformationReceiver transformationReceiver;

    private float redDepth = 0;
    private float greenDepth = 0;
    private float blueDepth = 0;

    public SepiaTask(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float redDepth, float greenDepth, float blueDepth) {
        this.inputBitmap = inputBitmap;
        this.transformationReceiver = transformationReceiver;
        this.redDepth = redDepth;
        this.greenDepth = greenDepth;
        this.blueDepth = blueDepth;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // create output bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        float[] mat = new float[]{
                DEFAULT_RED + redDepth, DEFAULT_GREEN, DEFAULT_BLUE, 0, -1,
                DEFAULT_RED, DEFAULT_GREEN + greenDepth, DEFAULT_BLUE, 0, 0,
                DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE + blueDepth, 0, 0,
                0, 0, 0, 1, 0,};
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(mat);
        paint.setColorFilter(filter);
        canvas.drawBitmap(inputBitmap, 0, 0, paint);

        // return final image
        return outputBitmap;
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);

        transformationReceiver.onTransformationProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap outputBitmap) {
        super.onPostExecute(outputBitmap);

        transformationReceiver.onTransformationComplete(outputBitmap);
    }
}