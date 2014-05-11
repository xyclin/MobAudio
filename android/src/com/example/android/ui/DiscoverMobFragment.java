package com.example.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dolby.dap.DolbyAudioProcessing;
import com.example.android.R;

import android.widget.ListView;
import com.example.android.service.NetworkAPI;
import android.widget.ArrayAdapter;

public class DiscoverMobFragment extends Fragment implements LocationListener {
    private DolbyAudioProcessing mDolbyAudioProcessing;
	ListView mListView;
	Thread mThread;
	Object mThreadLock = new Object();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //mPlayer = ClientManager.getInstance().getSong();
		mListView = inflater.inflate(R.layout.song_play_view, container, false);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        return mListView;
    }

    @Override
    public void onLocationChanged(Location loc) {
		final double lat = loc.getLatitude();
		final double lon = loc.getLongitude();

		synchronized(mThreadLock) {
			if (mThread != null)
				return;
			mThread = new Thread(){
				@Override
				public void run() {
					final Mob[] mobs = NetworkAPI.getInstance().getMobs(5000, lat, lon).toArray();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lv.setAdapter(new ArrayAdapter<Mob>(this, R.layout.song_element, mobs) {
								@Override
								public View getView(int position, View convertView, ViewGroup parent) {
									convertView = super.getView(position, convertView, parent);
									TextView name = (TextView)convertView.findViewById(R.id.text_name);
									TextView label = (TextView)convertView.findViewById(R.id.text_label);
									final Mob mob = mobs[position];
									name.setText(mob.getName());
									label.setText(mob.getLabel());
								}
							});
						}
					});
					synchronized(mThreadLock) {
						mThread = null;
					}
				}
			}.start();
		}
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
