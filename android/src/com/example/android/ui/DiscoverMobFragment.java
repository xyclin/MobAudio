package com.example.android.ui;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dolby.dap.DolbyAudioProcessing;
import com.example.android.MyActivity;
import com.example.android.R;

import android.widget.ListView;
import com.example.android.model.Mob;
import com.example.android.network.NetworkAPI;
import com.example.android.service.MobLoader;

import android.widget.ArrayAdapter;

public class DiscoverMobFragment extends Fragment implements LocationListener {
    private DolbyAudioProcessing mDolbyAudioProcessing;
	ListView mListView;
	Thread mThread;
	Object mThreadLock = new Object();
    private MyActivity activity;

    public DiscoverMobFragment(MyActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //mPlayer = ClientManager.getInstance().getSong();
		mListView = (ListView) inflater.inflate(R.layout.song_play_view, container, false);
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        refreshList(null);
        return mListView;
    }

    public void refreshList(Location loc){
        double lat = 37.8;
        double lon = 122.4;
        double radius = 999999.;
        if (loc != null){
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            radius = 10000;
        }
        MobLoader loader = new MobLoader(){
            @Override
            public void onPostExecute(Mob[] mobs){
                mListView.setAdapter(new MobAdapter(getActivity(), R.layout.song_element, R.id.text_name, mobs, activity));
            }
        };
        loader.execute(radius, lat, lon);
    }


    @Override
    public void onLocationChanged(Location loc) {
		refreshList(loc);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
