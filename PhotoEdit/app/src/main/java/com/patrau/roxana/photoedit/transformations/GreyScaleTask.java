package com.patrau.roxana.photoedit.transformations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.TransformationReceiver;

public class GreyScaleTask extends AsyncTask<String, Float, Bitmap> {

    private Bitmap inputBitmap;
    private TransformationReceiver transformationReceiver;

    public GreyScaleTask(Bitmap inputBitmap, TransformationReceiver transformationReceiver) {
        this.inputBitmap = inputBitmap;
        this.transformationReceiver = transformationReceiver;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // constant factors
        final float GS_RED = 0.299f;
        final float GS_GREEN = 0.587f;
        final float GS_BLUE = 0.114f;

        // create output bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        float[] mat = new float[]{
                GS_RED, GS_GREEN, GS_BLUE, 0, 0,
                GS_RED, GS_GREEN, GS_BLUE, 0, 0,
                GS_RED, GS_GREEN, GS_BLUE, 0, 0,
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