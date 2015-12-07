package com.patrau.roxana.photoedit.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.patrau.roxana.photoedit.R;

public class CanvasFragment extends Fragment {

    private View mView;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = (ViewGroup) inflater.inflate(
                R.layout.fragment_canvas, container, false);

        imageView = (ImageView) mView.findViewById(R.id.canvas_image);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    public void setImage(Bitmap bitmap) {
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
