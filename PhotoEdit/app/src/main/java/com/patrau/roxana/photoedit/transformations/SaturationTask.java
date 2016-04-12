package com.patrau.roxana.photoedit.transformations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.interfaces.TransformationReceiver;

public class SaturationTask extends AsyncTask<String, Float, Bitmap> {

    private Bitmap inputBitmap;
    private TransformationReceiver transformationReceiver;

    private static final float DEFAULT_RED = 0.3086f;
    private static final float DEFAULT_GREEN = 0.6094f;
    private static final float DEFAULT_BLUE = 0.0820f;

    private float value = 0;

    public SaturationTask(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float value) {
        this.inputBitmap = inputBitmap;
        this.transformationReceiver = transformationReceiver;
        this.value = cleanValue(value, 100);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // create output bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);

        float x = 1 + ((value > 0) ? 3 * value / 100 : value / 100);
        float[] mat = new float[]{
                DEFAULT_RED * (1 - x) + x, DEFAULT_GREEN * (1 - x), DEFAULT_BLUE * (1 - x), 0, 0,
                DEFAULT_RED * (1 - x), DEFAULT_GREEN * (1 - x) + x, DEFAULT_BLUE * (1 - x), 0, 0,
                DEFAULT_RED * (1 - x), DEFAULT_GREEN * (1 - x), DEFAULT_BLUE * (1 - x) + x, 0, 0,
                0, 0, 0, 1, 0,
                0, 0, 0, 0, 1
        };
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

    private static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
}