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

public class HueControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar valueSeekbar;
    private TextView valueTextView;

    private static final int SEEK_BAR_OFFSET = 180;
    private int value = SEEK_BAR_OFFSET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_hue, container, false);

        // initialize the view objects
        valueSeekbar = (SeekBar) mView.findViewById(R.id.value_seekbar);
        valueSeekbar.setProgress(value);
        valueSeekbar.setOnSeekBarChangeListener(this);

        // initialize the value text views
        valueTextView = (TextView) mView.findViewById(R.id.value_textview);

        // set the initial values for the RGB text views
        valueTextView.setText(String.valueOf(value - SEEK_BAR_OFFSET));

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
        // seek-bar value changed, apply the effect to the current bitmap
        MainActivity activity = (MainActivity) getActivity();

        switch (seekBar.getId()) {
            case R.id.value_seekbar:
                value = seekBar.getProgress() - SEEK_BAR_OFFSET;
                valueTextView.setText(String.valueOf(value));
                break;
        }

        ImageProcessor.doHue(MainActivity.originalBitmap, activity, value);
    }
}
