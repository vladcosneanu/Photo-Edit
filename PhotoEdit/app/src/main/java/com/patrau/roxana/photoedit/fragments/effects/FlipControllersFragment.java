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
import com.patrau.roxana.photoedit.transformations.FlipTask;
import com.patrau.roxana.photoedit.transformations.GrayScaleTask;

public class FlipControllersFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private ImageButton backButton;
    private View flipHorizontal;
    private View flipVertical;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_flip, container, false);

        // initialize the view objects
        flipHorizontal = mView.findViewById(R.id.flip_horizontal);
        flipHorizontal.setOnClickListener(this);
        flipVertical = mView.findViewById(R.id.flip_vertical);
        flipVertical.setOnClickListener(this);

        backButton = (ImageButton) mView.findViewById(R.id.back_button);
        backButton.setOnClickListener((MainActivity) getActivity());

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flip_horizontal:
                ImageProcessor.doFlip(MainActivity.originalBitmap, (MainActivity) getActivity(), FlipTask.FLIP_HORIZONTAL);

                break;
            case R.id.flip_vertical:
                ImageProcessor.doFlip(MainActivity.originalBitmap, (MainActivity) getActivity(), FlipTask.FLIP_VERTICAL);

                break;
        }
    }
}
