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
import com.patrau.roxana.photoedit.transformations.GrayScaleTask;

public class SepiaControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private TextView redTextView;
    private TextView greenTextView;
    private TextView blueTextView;

    private float redValue = -0.05f;
    private float greenValue = 0.0f;
    private float blueValue = -0.18f;

    private static final int SEEK_BAR_OFFSET = 50;
    private static final int SEEK_BAR_OFFSET_MULTIPLIER = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_sepia, container, false);

        // initialize the RGB values text views
        redTextView = (TextView) mView.findViewById(R.id.red_textview);
        greenTextView = (TextView) mView.findViewById(R.id.green_textview);
        blueTextView = (TextView) mView.findViewById(R.id.blue_textview);

        // all seek-bars have an offset multiplier of 100
        redSeekBar = (SeekBar) mView.findViewById(R.id.sepia_red_seekbar);
        redSeekBar.setProgress((int) (redValue * SEEK_BAR_OFFSET_MULTIPLIER) + SEEK_BAR_OFFSET);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = (SeekBar) mView.findViewById(R.id.sepia_green_seekbar);
        greenSeekBar.setProgress((int) (greenValue * SEEK_BAR_OFFSET_MULTIPLIER) + SEEK_BAR_OFFSET);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = (SeekBar) mView.findViewById(R.id.sepia_blue_seekbar);
        blueSeekBar.setProgress((int) (blueValue * SEEK_BAR_OFFSET_MULTIPLIER) + SEEK_BAR_OFFSET);
        blueSeekBar.setOnSeekBarChangeListener(this);

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // fragment attached, apply the effect to the current bitmap
        MainActivity activity = (MainActivity) getActivity();
        ImageProcessor.doSepia(MainActivity.originalBitmap, activity, redValue, greenValue, blueValue);

        // set the initial values for the RGB text views
        redTextView.setText(String.valueOf(redValue));
        greenTextView.setText(String.valueOf(greenValue));
        blueTextView.setText(String.valueOf(blueValue));
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
            case R.id.sepia_red_seekbar:
                redValue = (float) (seekBar.getProgress() - SEEK_BAR_OFFSET) / SEEK_BAR_OFFSET_MULTIPLIER;
                redTextView.setText(String.valueOf(redValue));
                break;
            case R.id.sepia_green_seekbar:
                greenValue = (float) (seekBar.getProgress() - SEEK_BAR_OFFSET) / SEEK_BAR_OFFSET_MULTIPLIER;
                greenTextView.setText(String.valueOf(greenValue));
                break;
            case R.id.sepia_blue_seekbar:
                blueValue = (float) (seekBar.getProgress() - SEEK_BAR_OFFSET) / SEEK_BAR_OFFSET_MULTIPLIER;
                blueTextView.setText(String.valueOf(blueValue));
                break;
        }

        ImageProcessor.doSepia(MainActivity.originalBitmap, activity, redValue, greenValue, blueValue);
    }
}
