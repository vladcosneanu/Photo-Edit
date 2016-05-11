package com.patrau.roxana.photoedit.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.fragments.effects.BoostColorControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.BrightnessControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.FlipControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.GrayScaleControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.HueControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.RotateControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.SaturationControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.SepiaControllersFragment;

public class CanvasFragment extends Fragment {

    private View mView;
    private ImageView imageView;
    private TextView addImageText;
    private String originalFilePath;
    private View canvasContainer;
    private View controllersFrameContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(
                R.layout.fragment_canvas, container, false);

        // initialize the view objects
        imageView = (ImageView) mView.findViewById(R.id.canvas_image);
        addImageText = (TextView) mView.findViewById(R.id.add_image_text);
        canvasContainer = mView.findViewById(R.id.canvas_container);
        controllersFrameContainer = mView.findViewById(R.id.controllers_frame_container);
        controllersFrameContainer.setVisibility(View.GONE);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    public void setCanvasImage(Bitmap bitmap) {
        if (imageView != null) {
            MainActivity.currentCanvasBitmap = bitmap;
            canvasContainer.setBackgroundResource(0);
            imageView.setImageBitmap(bitmap);
            addImageText.setVisibility(View.GONE);

            if (MainActivity.inCanvasEditMode) {
                controllersFrameContainer.setVisibility(View.VISIBLE);
            } else {
                controllersFrameContainer.setVisibility(View.GONE);
            }
        }
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
        // start a new thread in order to improve user experience while decoding bitmap file
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.originalBitmap = BitmapFactory.decodeFile(CanvasFragment.this.originalFilePath);
                // synchronize with the UI thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCanvasImage(MainActivity.originalBitmap);
                        ((MainActivity) getActivity()).dismissProgressDialog();
                    }
                });
            }
        });
        thread.start();
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public void hideControllersFrameContainer() {
        controllersFrameContainer.setVisibility(View.GONE);
    }

    private void attachControllersFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.controllers_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public void attachEffectsFragment() {
        EffectsFragment effectsFragment = new EffectsFragment();
        attachControllersFragment(effectsFragment);
    }

    public void attachGrayScaleController() {
        GrayScaleControllersFragment grayScaleControllersFragment = new GrayScaleControllersFragment();
        attachControllersFragment(grayScaleControllersFragment);
    }

    public void attachFlipController() {
        FlipControllersFragment flipControllersFragment = new FlipControllersFragment();
        attachControllersFragment(flipControllersFragment);
    }

    public void attachBoostColorController() {
        BoostColorControllersFragment boostColorControllersFragment = new BoostColorControllersFragment();
        attachControllersFragment(boostColorControllersFragment);
    }

    public void attachRotateController() {
        RotateControllersFragment rotateControllersFragment = new RotateControllersFragment();
        attachControllersFragment(rotateControllersFragment);
    }

    public void attachSepiaController() {
        SepiaControllersFragment sepiaControllersFragment = new SepiaControllersFragment();
        attachControllersFragment(sepiaControllersFragment);
    }

    public void attachBrightnessController() {
        BrightnessControllersFragment brightnessControllersFragment = new BrightnessControllersFragment();
        attachControllersFragment(brightnessControllersFragment);
    }

    public void attachHueController() {
        HueControllersFragment hueControllersFragment = new HueControllersFragment();
        attachControllersFragment(hueControllersFragment);
    }

    public void attachSaturationController() {
        SaturationControllersFragment saturationControllersFragment = new SaturationControllersFragment();
        attachControllersFragment(saturationControllersFragment);
    }

    public Fragment getCurrentControllersFragment() {
        return getChildFragmentManager().findFragmentById(R.id.controllers_frame);
    }
}
