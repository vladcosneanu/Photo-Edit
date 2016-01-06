package com.patrau.roxana.photoedit.transformations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.TransformationReceiver;

public class BoostColorTask extends AsyncTask<String, Float, Bitmap> {

    private Bitmap inputBitmap;
    private TransformationReceiver transformationReceiver;

    private float redPercent = 0f;
    private float greenPercent = 0f;
    private float bluePercent = 0f;

    public BoostColorTask(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float redPercent, float greenPercent, float bluePercent) {
        this.inputBitmap = inputBitmap;
        this.transformationReceiver = transformationReceiver;
        this.redPercent = redPercent;
        this.greenPercent = greenPercent;
        this.bluePercent = bluePercent;
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
                (1 + redPercent), 0, 0, 0, 0,
                0, (1 + greenPercent), 0, 0, 0,
                0, 0, (1 + bluePercent), 0, 0,
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