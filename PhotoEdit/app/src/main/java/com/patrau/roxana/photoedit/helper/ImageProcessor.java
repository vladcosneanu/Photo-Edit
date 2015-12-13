package com.patrau.roxana.photoedit.helper;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.patrau.roxana.photoedit.TransformationReceiver;
import com.patrau.roxana.photoedit.transformations.GreyScaleTask;

public class ImageProcessor {

    public static void doGreyscale(Bitmap inputBitmap, TransformationReceiver transformationReceiver) {
        GreyScaleTask greyScaleTask = new GreyScaleTask(inputBitmap, transformationReceiver);
        greyScaleTask.execute(new String[]{});
    }
}
