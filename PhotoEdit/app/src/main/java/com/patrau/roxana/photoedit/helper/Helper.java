package com.patrau.roxana.photoedit.helper;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Helper {

    private static int MAX_THUMB_SIZE = 175;

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    public static boolean saveBitmapToCollection(Bitmap bitmap, String fileName) {
        return saveBitmapToLocation(bitmap, getCanvasStorageDirectory(), fileName);
    }

    public static boolean saveThumbBitmapToCollection(Bitmap bitmap, String fileName) {
        Bitmap thumbBitmap = scaleDown(bitmap, MAX_THUMB_SIZE, true);

        return saveBitmapToLocation(thumbBitmap, getThumbsStorageDirectory(), fileName);
    }

    private static boolean saveBitmapToLocation(Bitmap bitmap, String directory, String fileName) {
        File storageDirectory = new File(directory);
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }

        FileOutputStream outputStream = null;
        boolean fileSaved = false;
        try {
            File file = new File(storageDirectory, fileName);
            outputStream = new FileOutputStream(file);

            fileSaved = bitmap.compress(Bitmap.CompressFormat.PNG, 85, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileSaved;
    }

    public static Bitmap scaleDown(Bitmap bitmap, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / bitmap.getWidth(),
                (float) maxImageSize / bitmap.getHeight());
        int width = Math.round((float) ratio * bitmap.getWidth());
        int height = Math.round((float) ratio * bitmap.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width,
                height, filter);

        return newBitmap;
    }

    public static String getNewCanvasFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd, H:mm:ss");
        String fileName = simpleDateFormat.format(Calendar.getInstance().getTime());
        fileName += ".png";

        return fileName;
    }

    public static String getCanvasStorageDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotoEdit";
    }

    public static String getThumbsStorageDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotoEdit/Thumbs";
    }

    public static List<String> getCollection() {
        List<String> collection = new ArrayList<String>();
        File thumbsStorageDirectory = new File(getThumbsStorageDirectory());

        if (thumbsStorageDirectory.exists()) {
            File files[] = thumbsStorageDirectory.listFiles();
            for (File file: files) {
                collection.add(file.getName());
            }
        }

        return collection;
    }
}
