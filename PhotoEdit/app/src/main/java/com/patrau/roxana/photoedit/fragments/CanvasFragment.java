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
import com.patrau.roxana.photoedit.fragments.effects.FlipControllersFragment;
import com.patrau.roxana.photoedit.fragments.effects.GrayScaleControllersFragment;

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
        mView = (ViewGroup) inflater.inflate(
                R.layout.fragment_canvas, container, false);

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
            canvasContainer.setBackgroundResource(0);
            imageView.setImageBitmap(bitmap);
            addImageText.setVisibility(View.GONE);
            controllersFrameContainer.setVisibility(View.VISIBLE);
        }
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
        MainActivity.currentBitmap = BitmapFactory.decodeFile(originalFilePath);
        setCanvasImage(MainActivity.currentBitmap);
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
}
