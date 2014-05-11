package com.example.android.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.model.Mob;

import java.util.List;

/**
 * Created by gabriel on 5/11/14.
 */
public class MobAdapter extends ArrayAdapter<Mob> {

    public MobAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MobAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public MobAdapter(Context context, int resource, Mob[] objects) {
        super(context, resource, objects);
    }

    public MobAdapter(Context context, int resource, int textViewResourceId, Mob[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public MobAdapter(Context context, int resource, List<Mob> objects) {
        super(context, resource, objects);
    }

    public MobAdapter(Context context, int resource, int textViewResourceId, List<Mob> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        TextView name = (TextView)convertView.findViewById(R.id.text_name);
        TextView label = (TextView)convertView.findViewById(R.id.text_label);
        final Mob mob = getItem(position);
        name.setText(mob.getName());
        label.setText(mob.getTime());
        return convertView;
    }
}
