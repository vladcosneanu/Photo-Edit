package com.patrau.roxana.photoedit.transformations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.interfaces.TransformationReceiver;

public class BrightnessTask extends AsyncTask<String, Float, Bitmap> {

    private Bitmap inputBitmap;
    private TransformationReceiver transformationReceiver;

    private float value = 0;

    public BrightnessTask(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float value) {
        this.inputBitmap = inputBitmap;
        this.transformationReceiver = transformationReceiver;
        this.value = value;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // create output bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        float[] mat = new float[]{
                (1 + value), 0, 0, 0, 0,
                0, (1 + value), 0, 0, 0,
                0, 0, (1 + value), 0, 0,
                0, 0, 0, 1, 0};
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