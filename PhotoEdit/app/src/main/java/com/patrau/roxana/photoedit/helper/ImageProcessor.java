package com.patrau.roxana.photoedit.helper;

import android.graphics.Bitmap;

import com.patrau.roxana.photoedit.TransformationReceiver;
import com.patrau.roxana.photoedit.transformations.GrayScaleTask;

public class ImageProcessor {

    public static void doGreyscale(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float red, float green, float blue) {
        GrayScaleTask greyScaleTask = new GrayScaleTask(inputBitmap, transformationReceiver, red, green, blue);
        greyScaleTask.execute(new String[]{});
    }
}
