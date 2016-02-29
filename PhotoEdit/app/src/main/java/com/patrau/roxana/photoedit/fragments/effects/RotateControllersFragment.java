package com.patrau.roxana.photoedit.fragments.effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.helper.ImageProcessor;

public class RotateControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar degreeSeekbar;
    private int degreeValue = 180;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_rotate, container, false);

        degreeSeekbar = (SeekBar) mView.findViewById(R.id.degree_seekbar);
        degreeSeekbar.setProgress(degreeValue);
        degreeSeekbar.setOnSeekBarChangeListener(this);

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        MainActivity activity = (MainActivity) getActivity();

        switch (seekBar.getId()) {
            case R.id.degree_seekbar:
                degreeValue = seekBar.getProgress() - 180;
                break;
        }

        ImageProcessor.doRotate(MainActivity.originalBitmap, activity, degreeValue);
    }
}
