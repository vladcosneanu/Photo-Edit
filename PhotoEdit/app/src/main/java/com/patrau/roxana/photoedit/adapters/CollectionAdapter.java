package com.patrau.roxana.photoedit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrau.roxana.photoedit.R;
import com.patrau.roxana.photoedit.helper.Helper;

import java.io.File;
import java.util.List;

public class CollectionAdapter extends BaseAdapter {

    private Context context;
    private List<String> thumbnails;

    public CollectionAdapter(Context context, List<String> thumbnails) {
        this.context = context;
        this.thumbnails = thumbnails;
    }

    public int getCount() {
        return thumbnails.size();
    }

    public Object getItem(int position) {
        return thumbnails.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final String thumbnail = (String) getItem(position);
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.collection_thumb, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.thumbIcon = (ImageView) rowView.findViewById(R.id.thumb_image);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        File thumbnailImage = new File(Helper.getThumbsStorageDirectory(), thumbnail);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(thumbnailImage.getAbsolutePath(), bmOptions);
        viewHolder.thumbIcon.setImageBitmap(bitmap);

        return rowView;
    }

    static class ViewHolder {
        private ImageView thumbIcon;
    }
}