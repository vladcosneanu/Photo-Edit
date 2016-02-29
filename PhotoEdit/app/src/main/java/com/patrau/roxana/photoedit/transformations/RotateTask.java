package com.patrau.roxana.photoedit.transformations;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.interfaces.TransformationReceiver;

public class RotateTask extends AsyncTask<String, Float, Bitmap> {

    private Bitmap inputBitmap;
    private TransformationReceiver transformationReceiver;
    private float degree;

    public RotateTask(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float degree) {
        this.inputBitmap = inputBitmap;
        this.transformationReceiver = transformationReceiver;
        this.degree = degree;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // create output bitmap
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);

        // create new bitmap rotated using matrix
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap, 0, 0, inputBitmap.getWidth(), inputBitmap.getHeight(), matrix, true);

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