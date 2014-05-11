package com.example.android.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.android.R;

/**
 * Created by gabriel on 5/10/14.
 */
@SuppressLint("NewApi")
public class CreateMobFragment extends Fragment implements View.OnClickListener {

    private EditText nameText;
    private EditText urlText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_mob, null);
        nameText = (EditText) view.findViewById(R.id.name_field);
        urlText = (EditText) view.findViewById(R.id.url_field);
        view.findViewById(R.id.create_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_button:
                String name = nameText.getText().toString();
                String url = urlText.getText().toString();
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }
}
