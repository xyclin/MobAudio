package com.example.android.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.MyActivity;
import com.example.android.R;
import com.example.android.model.Mob;

import java.util.List;

/**
 * Created by gabriel on 5/11/14.
 */
public class MobAdapter extends ArrayAdapter<Mob> {

    private MyActivity activity;

    public MobAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MobAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public MobAdapter(Context context, int resource, Mob[] objects) {
        super(context, resource, objects);
    }

    public MobAdapter(Context context, int resource, int textViewResourceId, Mob[] objects, MyActivity activity) {
        super(context, resource, textViewResourceId, objects);
        this.activity = activity;
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
        convertView.setTag(mob.getMobId());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.playSong(mob.getUrl());
                FragmentManager manager = activity.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.fragment_container, new SongPlayingFragment());
                transaction.commit();
            }
        });
        return convertView;
    }
}
