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

import org.w3c.dom.Text;

public class GrayScaleControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private TextView redTextView;
    private TextView greenTextView;
    private TextView blueTextView;

    private float redValue = GrayScaleTask.DEFAULT_RED;
    private float greenValue = GrayScaleTask.DEFAULT_GREEN;
    private float blueValue = GrayScaleTask.DEFAULT_BLUE;

    private static final int SEEK_BAR_OFFSET_MULTIPLIER = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_grayscale, container, false);

        // initialize the RGB values text views
        redTextView = (TextView) mView.findViewById(R.id.red_textview);
        greenTextView = (TextView) mView.findViewById(R.id.green_textview);
        blueTextView = (TextView) mView.findViewById(R.id.blue_textview);

        // all seek-bars have an offset multiplier of 1000
        redSeekBar = (SeekBar) mView.findViewById(R.id.grayscale_red_seekbar);
        redSeekBar.setProgress((int) (redValue * SEEK_BAR_OFFSET_MULTIPLIER));
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = (SeekBar) mView.findViewById(R.id.grayscale_green_seekbar);
        greenSeekBar.setProgress((int) (greenValue * SEEK_BAR_OFFSET_MULTIPLIER));
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = (SeekBar) mView.findViewById(R.id.grayscale_blue_seekbar);
        blueSeekBar.setProgress((int) (blueValue * SEEK_BAR_OFFSET_MULTIPLIER));
        blueSeekBar.setOnSeekBarChangeListener(this);

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // fragment attached, apply the effect to the current bitmap
        MainActivity activity = (MainActivity) getActivity();
        ImageProcessor.doGreyscale(MainActivity.originalBitmap, activity, redValue, greenValue, blueValue);

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
            case R.id.grayscale_red_seekbar:
                redValue = (float) seekBar.getProgress() / SEEK_BAR_OFFSET_MULTIPLIER;
                redTextView.setText(String.valueOf(redValue));
                break;
            case R.id.grayscale_green_seekbar:
                greenValue = (float) seekBar.getProgress() / SEEK_BAR_OFFSET_MULTIPLIER;
                greenTextView.setText(String.valueOf(greenValue));
                break;
            case R.id.grayscale_blue_seekbar:
                blueValue = (float) seekBar.getProgress() / SEEK_BAR_OFFSET_MULTIPLIER;
                blueTextView.setText(String.valueOf(blueValue));
                break;
        }

        ImageProcessor.doGreyscale(MainActivity.originalBitmap, activity, redValue, greenValue, blueValue);
    }
}
