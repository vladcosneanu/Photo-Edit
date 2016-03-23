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

public class BoostColorControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private TextView redTextView;
    private TextView greenTextView;
    private TextView blueTextView;

    private int redValue = 0;
    private int greenValue = 0;
    private int blueValue = 0;

    private static final int SEEK_BAR_OFFSET = 75;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_boost_color, container, false);

        // initialize the RGB values text views
        redTextView = (TextView) mView.findViewById(R.id.red_textview);
        greenTextView = (TextView) mView.findViewById(R.id.green_textview);
        blueTextView = (TextView) mView.findViewById(R.id.blue_textview);

        // all seek-bars have an offset of 75 in order to start at the middle
        redSeekBar = (SeekBar) mView.findViewById(R.id.boost_color_red_seekbar);
        redSeekBar.setProgress(redValue + SEEK_BAR_OFFSET);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = (SeekBar) mView.findViewById(R.id.boost_color_green_seekbar);
        greenSeekBar.setProgress(greenValue + SEEK_BAR_OFFSET);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = (SeekBar) mView.findViewById(R.id.boost_color_blue_seekbar);
        blueSeekBar.setProgress(blueValue + SEEK_BAR_OFFSET);
        blueSeekBar.setOnSeekBarChangeListener(this);

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // fragment attached, apply the effect to the current bitmap
        MainActivity activity = (MainActivity) getActivity();
        ImageProcessor.doBoostColor(MainActivity.originalBitmap, activity, redValue, greenValue, blueValue);

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
            case R.id.boost_color_red_seekbar:
                redValue = seekBar.getProgress() - SEEK_BAR_OFFSET;
                redTextView.setText(String.valueOf(redValue));
                break;
            case R.id.boost_color_green_seekbar:
                greenValue = seekBar.getProgress() - SEEK_BAR_OFFSET;
                greenTextView.setText(String.valueOf(greenValue));
                break;
            case R.id.boost_color_blue_seekbar:
                blueValue = seekBar.getProgress() - SEEK_BAR_OFFSET;
                blueTextView.setText(String.valueOf(blueValue));
                break;
        }

        ImageProcessor.doBoostColor(MainActivity.originalBitmap, activity, redValue, greenValue, blueValue);
    }
}
