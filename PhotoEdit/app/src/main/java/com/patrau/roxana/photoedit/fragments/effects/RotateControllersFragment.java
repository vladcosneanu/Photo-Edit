package com.patrau.roxana.photoedit.fragments.effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.helper.ImageProcessor;

public class RotateControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar degreeSeekbar;
    private TextView angleTextView;

    private static final int SEEK_BAR_OFFSET = 180;
    private int degreeValue = SEEK_BAR_OFFSET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_rotate, container, false);

        // initialize the view objects
        degreeSeekbar = (SeekBar) mView.findViewById(R.id.degree_seekbar);
        degreeSeekbar.setProgress(degreeValue);
        degreeSeekbar.setOnSeekBarChangeListener(this);

        // initialize the angle value text views
        angleTextView = (TextView) mView.findViewById(R.id.angle_textview);

        // set the initial values for the RGB text views
        angleTextView.setText(getString(R.string.value_degrees, degreeValue - SEEK_BAR_OFFSET));

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateValueTextView(seekBar);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // seek-bar value changed, apply the effect to the current bitmap
        MainActivity activity = (MainActivity) getActivity();

        updateValueTextView(seekBar);

        ImageProcessor.doRotate(MainActivity.originalBitmap, activity, degreeValue);
    }

    private void updateValueTextView(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.degree_seekbar:
                degreeValue = seekBar.getProgress() - SEEK_BAR_OFFSET;
                angleTextView.setText(getString(R.string.value_degrees, degreeValue));
                break;
        }
    }
}
