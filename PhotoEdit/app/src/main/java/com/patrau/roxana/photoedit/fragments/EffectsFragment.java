package com.patrau.roxana.photoedit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.helper.ImageProcessor;

public class EffectsFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private View grayscaleCoontainer;
    private View invertCoontainer;
    private View flipCoontainer;
    private View boostColorCoontainer;
    private View rotateCoontainer;
    private View sepiaCoontainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_effects, container, false);

        // initialize the view objects
        grayscaleCoontainer = mView.findViewById(R.id.grayscale_container);
        grayscaleCoontainer.setOnClickListener(this);
        invertCoontainer = mView.findViewById(R.id.invert_container);
        invertCoontainer.setOnClickListener(this);
        flipCoontainer = mView.findViewById(R.id.flip_container);
        flipCoontainer.setOnClickListener(this);
        boostColorCoontainer = mView.findViewById(R.id.boost_color_container);
        boostColorCoontainer.setOnClickListener(this);
        rotateCoontainer = mView.findViewById(R.id.rotate_container);
        rotateCoontainer.setOnClickListener(this);
        sepiaCoontainer = mView.findViewById(R.id.sepia_container);
        sepiaCoontainer.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grayscale_container:
                ((MainActivity) getActivity()).attachGrayScaleController();

                break;
            case R.id.invert_container:
                ImageProcessor.doInvert(MainActivity.originalBitmap, (MainActivity) getActivity());

                break;
            case R.id.flip_container:
                ((MainActivity) getActivity()).attachFlipController();

                break;
            case R.id.boost_color_container:
                ((MainActivity) getActivity()).attachBoostColorController();

                break;
            case R.id.rotate_container:
                ((MainActivity) getActivity()).attachRotateController();

                break;
            case R.id.sepia_container:
                ((MainActivity) getActivity()).attachSepiaController();

                break;
        }
    }
}
