package com.patrau.roxana.photoedit.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.patrau.roxana.photoedit.helper.Helper;
import com.patrau.roxana.photoedit.interfaces.CanvasSaver;

public class SaveCanvasTask extends AsyncTask<String, Float, Boolean> {

    private Bitmap bitmap;
    private CanvasSaver canvasSaver;

    public SaveCanvasTask(Bitmap bitmap, CanvasSaver canvasSaver) {
        this.bitmap = bitmap;
        this.canvasSaver = canvasSaver;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String canvasFileName = Helper.getNewCanvasFileName();

        boolean canvasSaved = Helper.saveBitmapToCollection(bitmap, canvasFileName);
        boolean thumbSaved = Helper.saveThumbBitmapToCollection(bitmap, canvasFileName);

        return (canvasSaved && thumbSaved);
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean canvasSaved) {
        super.onPostExecute(canvasSaved);

        if (canvasSaved) {
            canvasSaver.onCanvasSaveSuccess();
        } else {
            canvasSaver.onCanvasSaveFailed();
        }
    }
}