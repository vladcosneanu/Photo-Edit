package com.patrau.roxana.photoedit.transformations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.interfaces.TransformationReceiver;

public class GrayScaleTask extends AsyncTask<String, Float, Bitmap> {

    // constant factors
    public static final float DEFAULT_RED = 0.299f;
    public static final float DEFAULT_GREEN = 0.587f;
    public static final float DEFAULT_BLUE = 0.114f;

    private Bitmap inputBitmap;
    private TransformationReceiver transformationReceiver;

    private float red = DEFAULT_RED;
    private float green = DEFAULT_GREEN;
    private float blue = DEFAULT_BLUE;

    public GrayScaleTask(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float red, float green, float blue) {
        this.inputBitmap = inputBitmap;
        this.transformationReceiver = transformationReceiver;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // create output bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        float[] mat = new float[]{
                red, green, blue, 0, 0,
                red, green, blue, 0, 0,
                red, green, blue, 0, 0,
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