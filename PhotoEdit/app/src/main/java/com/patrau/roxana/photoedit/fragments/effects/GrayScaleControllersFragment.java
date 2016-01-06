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
import com.patrau.roxana.photoedit.transformations.GrayScaleTask;

public class GrayScaleControllersFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View mView;
    private ImageButton backButton;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    private float redValue = GrayScaleTask.DEFAULT_RED;
    private float greenValue = GrayScaleTask.DEFAULT_GREEN;
    ;
    private float blueValue = GrayScaleTask.DEFAULT_BLUE;
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_grayscale, container, false);

        redSeekBar = (SeekBar) mView.findViewById(R.id.grayscale_red_seekbar);
        redSeekBar.setProgress((int) (redValue * 1000));
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = (SeekBar) mView.findViewById(R.id.grayscale_green_seekbar);
        greenSeekBar.setProgress((int) (greenValue * 1000));
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = (SeekBar) mView.findViewById(R.id.grayscale_blue_seekbar);
        blueSeekBar.setProgress((int) (blueValue * 1000));
        blueSeekBar.setOnSeekBarChangeListener(this);

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        ImageProcessor.doGreyscale(MainActivity.currentBitmap, activity, redValue, greenValue, blueValue);
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
            case R.id.grayscale_red_seekbar:
                redValue = (float) seekBar.getProgress() / 1000;
                break;
            case R.id.grayscale_green_seekbar:
                greenValue = (float) seekBar.getProgress() / 1000;
                break;
            case R.id.grayscale_blue_seekbar:
                blueValue = (float) seekBar.getProgress() / 1000;
                break;
        }

        ImageProcessor.doGreyscale(MainActivity.currentBitmap, activity, redValue, greenValue, blueValue);
    }
}
