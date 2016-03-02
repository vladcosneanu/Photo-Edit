package com.patrau.roxana.photoedit.helper;

import android.graphics.Bitmap;

import com.patrau.roxana.photoedit.interfaces.TransformationReceiver;
import com.patrau.roxana.photoedit.transformations.BoostColorTask;
import com.patrau.roxana.photoedit.transformations.RotateTask;
import com.patrau.roxana.photoedit.transformations.FlipTask;
import com.patrau.roxana.photoedit.transformations.GrayScaleTask;
import com.patrau.roxana.photoedit.transformations.InvertTask;

/**
 * This class is the main access point for all image effects
 */
public class ImageProcessor {

    public static void doGreyscale(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float red, float green, float blue) {
        GrayScaleTask greyScaleTask = new GrayScaleTask(inputBitmap, transformationReceiver, red, green, blue);
        greyScaleTask.execute(new String[]{});
    }

    public static void doInvert(Bitmap inputBitmap, TransformationReceiver transformationReceiver) {
        InvertTask greyScaleTask = new InvertTask(inputBitmap, transformationReceiver);
        greyScaleTask.execute(new String[]{});
    }

    public static void doFlip(Bitmap inputBitmap, TransformationReceiver transformationReceiver, int type) {
        FlipTask flipTask = new FlipTask(inputBitmap, transformationReceiver, type);
        flipTask.execute(new String[]{});
    }

    public static void doBoostColor(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float redPercent, float greenPercent, float bluePercent) {
        BoostColorTask boostColorTask = new BoostColorTask(inputBitmap, transformationReceiver,
                (float) redPercent / 100, (float) greenPercent / 100, (float) bluePercent / 100);
        boostColorTask.execute(new String[]{});
    }

    public static void doRotate(Bitmap inputBitmap, TransformationReceiver transformationReceiver, float degree) {
        RotateTask rotateTask = new RotateTask(inputBitmap, transformationReceiver, degree);
        rotateTask.execute(new String[]{});
    }
}
