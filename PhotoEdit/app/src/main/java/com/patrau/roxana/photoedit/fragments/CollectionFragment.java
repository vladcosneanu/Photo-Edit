package com.patrau.roxana.photoedit.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.adapters.CollectionAdapter;
import com.patrau.roxana.photoedit.helper.Helper;

import java.io.File;
import java.util.List;

public class CollectionFragment extends Fragment {

    private View mView;
    private TextView noItemsText;
    private GridView collectionGridView;
    private CollectionAdapter collectionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_collection, container, false);

        // initialize the view objects
        collectionGridView = (GridView) mView.findViewById(R.id.collection_gridview);
        collectionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (collectionAdapter != null) {
                    ((MainActivity) getActivity()).displayProgressDialog();
                    final String thumbnail = (String)collectionAdapter.getItem(position);
                    File thumbnailImage = new File(Helper.getCanvasStorageDirectory(), thumbnail);

                    ((MainActivity) getActivity()).fillCanvas(thumbnailImage.getPath());
                }
            }
        });

        noItemsText = (TextView) mView.findViewById(R.id.no_items_text);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refreshCollection();
    }

    public void refreshCollection() {
        List<String> collection = Helper.getCollection();
        if (collection.size() > 0) {
            // the collection has items
            noItemsText.setVisibility(View.GONE);
            collectionGridView.setVisibility(View.VISIBLE);

            collectionAdapter = new CollectionAdapter(getActivity(), collection);
            collectionGridView.setAdapter(collectionAdapter);
        } else {
            // the collection has no items
            noItemsText.setVisibility(View.VISIBLE);
            collectionGridView.setVisibility(View.GONE);
        }
    }
}
