package com.example.android.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.android.MyActivity;
import com.example.android.R;
import com.example.android.model.Mob;
import com.example.android.service.CreateMobService;

/**
 * Created by gabriel on 5/10/14.
 */
@SuppressLint("NewApi")
public class CreateMobFragment extends Fragment implements View.OnClickListener {

    private EditText nameText;
    private EditText urlText;
    private EditText timeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_mob, null);
        nameText = (EditText) view.findViewById(R.id.name_field);
        urlText = (EditText) view.findViewById(R.id.url_field);
        timeText = (EditText) view.findViewById(R.id.time_field);
        view.findViewById(R.id.create_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_button:
                String name = nameText.getText().toString();
                String url = urlText.getText().toString();
                String time = timeText.getText().toString();
                Mob mob = new Mob();
                mob.setName(name);
                mob.setUrl(url);
                mob.setTime(time);
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(new Criteria(), false);
                Location loc = provider == null ? null : locationManager.getLastKnownLocation(provider);
                mob.setLatitude(loc == null ? 0 : loc.getLatitude());
                mob.setLongitude(loc == null ? 0 : loc.getLongitude());
                Intent intent = new Intent();
                intent.putExtra(CreateMobService.MOB_KEY, mob);
                getActivity().startService(intent);
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }
}
