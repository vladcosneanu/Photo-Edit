package com.patrau.roxana.photoedit.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.fragments.CanvasFragment;
import com.patrau.roxana.photoedit.fragments.CollectionFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

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

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (context == null) {
            return title;
        }

        switch (position) {
            case 0:
                return context.getString(R.string.collection);
            case 1:
                return context.getString(R.string.canvas);
        }

        return title;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
