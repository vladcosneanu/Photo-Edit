package com.patrau.roxana.photoedit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.adapters.EffectsAdapter;
import com.patrau.roxana.photoedit.helper.Helper;
import com.patrau.roxana.photoedit.helper.ImageProcessor;
import com.patrau.roxana.photoedit.objects.Effect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EffectsFragment extends Fragment {

    private View mView;
    private EffectsAdapter effectsAdapter;
    private GridView effectsGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_effects, container, false);

        // initialize the effects list
        List<Effect> effects = new ArrayList<Effect>();
        effects.add(new Effect(getString(R.string.grayscale), R.drawable.ic_brightness_medium_black));
        effects.add(new Effect(getString(R.string.invert_image), R.drawable.ic_invert_colors_black));
        effects.add(new Effect(getString(R.string.flip_image), R.drawable.ic_flip_black));
        effects.add(new Effect(getString(R.string.boost_color), R.drawable.ic_palette_black));
        effects.add(new Effect(getString(R.string.rotate), R.drawable.ic_rotate_right_black));
        effects.add(new Effect(getString(R.string.sepia), R.drawable.ic_filter_vintage_black));
        effects.add(new Effect(getString(R.string.brightness), R.drawable.ic_brightness));
        effects.add(new Effect(getString(R.string.hue), R.drawable.ic_tonality_black));

        // initialize the effecrs adapter
        effectsAdapter = new EffectsAdapter(getActivity(), effects);

        // initialize the view object and attach the item click listener
        effectsGridView = (GridView) mView.findViewById(R.id.effects_gridview);
        effectsGridView.setAdapter(effectsAdapter);
        effectsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (effectsAdapter != null) {
                    switch (position) {
                        case 0:
                            ((MainActivity) getActivity()).attachGrayScaleController();

                            break;
                        case 1:
                            ImageProcessor.doInvert(MainActivity.originalBitmap, (MainActivity) getActivity());

                            break;
                        case 2:
                            ((MainActivity) getActivity()).attachFlipController();

                            break;
                        case 3:
                            ((MainActivity) getActivity()).attachBoostColorController();

                            break;
                        case 4:
                            ((MainActivity) getActivity()).attachRotateController();

                            break;
                        case 5:
                            ((MainActivity) getActivity()).attachSepiaController();

                            break;
                        case 6:
                            ((MainActivity) getActivity()).attachBrightnessController();

                            break;
                        case 7:
                            ((MainActivity) getActivity()).attachHueController();

                            break;
                    }
                }
            }
        });

        return mView;
    }
}
