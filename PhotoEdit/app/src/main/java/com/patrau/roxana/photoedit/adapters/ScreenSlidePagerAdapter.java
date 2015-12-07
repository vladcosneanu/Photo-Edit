package com.patrau.roxana.photoedit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.patrau.roxana.photoedit.fragments.CanvasFragment;
import com.patrau.roxana.photoedit.fragments.CollectionFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private CollectionFragment collectionFragment;
    private CanvasFragment canvasFragment;

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (collectionFragment == null) {
                    collectionFragment = new CollectionFragment();
                }
                return collectionFragment;
            case 1:
                if (canvasFragment == null) {
                    canvasFragment = new CanvasFragment();
                }
                return canvasFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
