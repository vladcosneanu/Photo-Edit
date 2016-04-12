package com.patrau.roxana.photoedit.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.patrau.roxana.photoedit.MainActivity;
import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.adapters.CollectionAdapter;
import com.patrau.roxana.photoedit.helper.Helper;
import com.patrau.roxana.photoedit.objects.CanvasThumb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {

    private View mView;
    private TextView noItemsText;
    private GridView collectionGridView;
    private CollectionAdapter collectionAdapter;
    private ActionMode actionMode = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment's layout
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_collection, container, false);

        // initialize the view objects and attach the item click listener
        collectionGridView = (GridView) mView.findViewById(R.id.collection_gridview);
        collectionGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        collectionGridView.setMultiChoiceModeListener(new MultiChoiceModeListener());
        collectionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (collectionAdapter != null) {
                    ((MainActivity) getActivity()).displayProgressDialog();
                    final CanvasThumb canvasThumb = (CanvasThumb) collectionAdapter.getItem(position);
                    File thumbnailImage = new File(Helper.getCanvasStorageDirectory(), canvasThumb.getThumbnail());

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (actionMode != null) {
            actionMode.finish();
        }
    }

    public void refreshCollection() {
        List<String> collection = Helper.getCollection();
        if (collection.size() > 0) {
            // the collection has items
            noItemsText.setVisibility(View.GONE);
            collectionGridView.setVisibility(View.VISIBLE);

            List<CanvasThumb> canvasThumbs = new ArrayList<CanvasThumb>();
            for (int i = 0; i < collection.size(); i++) {
                canvasThumbs.add(new CanvasThumb(collection.get(i)));
            }

            collectionAdapter = new CollectionAdapter(getActivity(), canvasThumbs);
            collectionGridView.setAdapter(collectionAdapter);
        } else {
            // the collection has no items
            noItemsText.setVisibility(View.VISIBLE);
            collectionGridView.setVisibility(View.GONE);
        }
    }

    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            actionMode = mode;
            mode.setTitle(getString(R.string.select_items));
            mode.setSubtitle(R.string.one_item_selected);

            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multichoice, menu);

            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_share:
                    ArrayList<Uri> files = new ArrayList<Uri>();
                    List<CanvasThumb> checkedCanvases = collectionAdapter.getCheckedItems();
                    for (int i = 0; i < checkedCanvases.size(); i++) {
                        File originalImage = new File(Helper.getCanvasStorageDirectory(), checkedCanvases.get(i).getThumbnail());
                        Uri uri = Uri.fromFile(originalImage);
                        files.add(uri);
                    }

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_title));
                    intent.setType("image/*");

                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                    startActivity(intent);

                    mode.finish();

                    return true;
                case R.id.action_delete:
                    displayDeleteDialog(mode);

                    return true;
            }

            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            collectionAdapter.clearCheckedItems();
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
            collectionAdapter.setCheckedItem(position, checked);
            int selectCount = collectionGridView.getCheckedItemCount();
            switch (selectCount) {
                case 1:
                    mode.setSubtitle(R.string.one_item_selected);
                    break;
                default:
                    mode.setSubtitle(getString(R.string.x_items_selected, selectCount));
                    break;
            }
        }
    }

    private void displayDeleteDialog(final ActionMode mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_dialog_title);
        builder.setMessage(R.string.delete_dialog_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final List<CanvasThumb> checkedCanvases = collectionAdapter.getCheckedItems();
                mode.finish();
                final MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.displayProgressDialog();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < checkedCanvases.size(); i++) {
                            File thumbnailImage = new File(Helper.getThumbsStorageDirectory(), checkedCanvases.get(i).getThumbnail());
                            thumbnailImage.delete();
                            File originalImage = new File(Helper.getCanvasStorageDirectory(), checkedCanvases.get(i).getThumbnail());
                            originalImage.delete();
                        }

                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.dismissProgressDialog();
                                Toast.makeText(mainActivity, R.string.deleted_canvases, Toast.LENGTH_SHORT).show();

                                refreshCollection();
                            }
                        });
                    }
                });
                thread.start();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

    public void closeMultiChoiceMode() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }
}
