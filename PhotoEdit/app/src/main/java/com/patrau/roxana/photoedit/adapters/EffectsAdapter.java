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
import com.patrau.roxana.photoedit.objects.Effect;

import java.io.File;
import java.util.List;

public class EffectsAdapter extends BaseAdapter {

    private Context context;
    // list of thumbnail file names
    private List<Effect> effects;

    /**
     * Default constructor for the adapter
     */
    public EffectsAdapter(Context context, List<Effect> effects) {
        this.context = context;
        this.effects = effects;
    }

    public int getCount() {
        return effects.size();
    }

    public Object getItem(int position) {
        return effects.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // get the effect for the specified position
        final Effect effect = (Effect) getItem(position);
        View rowView = convertView;
        if (rowView == null) {
            // inflate the effect icon layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.effect_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            // initialize the effect's views
            viewHolder.effectName = (TextView) rowView.findViewById(R.id.effect_name);
            viewHolder.effectIcon = (ImageView) rowView.findViewById(R.id.effect_icon);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        // set the effect name and icon to the specific views
        viewHolder.effectName.setText(effect.getEffectName());
        viewHolder.effectIcon.setImageResource(effect.getEffectImage());

        return rowView;
    }

    static class ViewHolder {
        private TextView effectName;
        private ImageView effectIcon;
    }
}