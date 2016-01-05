package com.patrau.roxana.photoedit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;

public class EffectsFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private View grayscaleCoontainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_effects, container, false);
        grayscaleCoontainer = mView.findViewById(R.id.grayscale_container);
        grayscaleCoontainer.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grayscale_container:
                ((MainActivity) getActivity()).attachGrayScaleController();

                break;
        }
    }
}
