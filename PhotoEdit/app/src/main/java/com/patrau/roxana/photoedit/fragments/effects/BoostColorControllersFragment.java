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

public class BoostColorControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    private int redValue = 0;
    private int greenValue = 0;
    private int blueValue = 0;
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_boost_color, container, false);

        redSeekBar = (SeekBar) mView.findViewById(R.id.boost_color_red_seekbar);
        redSeekBar.setProgress(redValue + 75);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = (SeekBar) mView.findViewById(R.id.boost_color_green_seekbar);
        greenSeekBar.setProgress(greenValue + 75);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = (SeekBar) mView.findViewById(R.id.boost_color_blue_seekbar);
        blueSeekBar.setProgress(blueValue + 75);
        blueSeekBar.setOnSeekBarChangeListener(this);

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        ImageProcessor.doBoostColor(MainActivity.currentBitmap, activity, redValue, greenValue, blueValue);
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
            case R.id.boost_color_red_seekbar:
                redValue = seekBar.getProgress() - 75;
                break;
            case R.id.boost_color_green_seekbar:
                greenValue = seekBar.getProgress() - 75;
                break;
            case R.id.boost_color_blue_seekbar:
                blueValue = seekBar.getProgress() - 75;
                break;
        }

        ImageProcessor.doBoostColor(MainActivity.currentBitmap, activity, redValue, greenValue, blueValue);
    }
}
