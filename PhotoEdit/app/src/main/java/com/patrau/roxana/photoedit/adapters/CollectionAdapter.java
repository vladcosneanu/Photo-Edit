package com.patrau.roxana.photoedit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.helper.Helper;
import com.patrau.roxana.photoedit.objects.CanvasThumb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends BaseAdapter {

    private Context context;
    // list of thumbnail file names
    private List<CanvasThumb> canvasThumbs;

    /**
     * Default constructor for the adapter
     */
    public CollectionAdapter(Context context, List<CanvasThumb> canvasThumbs) {
        this.context = context;
        this.canvasThumbs = canvasThumbs;
    }

    public int getCount() {
        return canvasThumbs.size();
    }

    public Object getItem(int position) {
        return canvasThumbs.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // get the thumbnail for the specified position
        final CanvasThumb canvasThumb = (CanvasThumb) getItem(position);
        View rowView = convertView;
        if (rowView == null) {
            // inflate the collection thumb layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.collection_thumb, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            // initialize the thumb icon views
            viewHolder.thumbIcon = (ImageView) rowView.findViewById(R.id.thumb_image);
            viewHolder.itemSelected = (ImageView) rowView.findViewById(R.id.item_selected);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        // get the file object for the specified thumbnail
        File thumbnailImage = new File(Helper.getThumbsStorageDirectory(), canvasThumb.getThumbnail());

        // decode the thumbnail file
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(thumbnailImage.getAbsolutePath(), bmOptions);

        // attach the obtained bitmap to the image view
        viewHolder.thumbIcon.setImageBitmap(bitmap);
        viewHolder.itemSelected.setVisibility((canvasThumb.isChecked() ? View.VISIBLE : View.GONE));

        return rowView;
    }

    public void setCheckedItem(int position, boolean checked) {
        ((CanvasThumb) getItem(position)).setChecked(checked);
        notifyDataSetChanged();
    }

    public void clearCheckedItems() {
        for (int i = 0; i < canvasThumbs.size(); i++) {
            canvasThumbs.get(i).setChecked(false);
        }
    }

    public List<CanvasThumb> getCheckedItems() {
        List<CanvasThumb> checkedCanvases = new ArrayList<CanvasThumb>();
        for (int i = 0; i < canvasThumbs.size(); i++) {
            if (canvasThumbs.get(i).isChecked()) {
                checkedCanvases.add(canvasThumbs.get(i));
            }
        }

        return checkedCanvases;
    }

    static class ViewHolder {
        private ImageView thumbIcon;
        private ImageView itemSelected;
    }
}