package com.telitel.tiwari.mflix.Screens.Fragments.UnImplemented;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telitel.tiwari.mflix.R;
import com.telitel.tiwari.mflix.SideNaviToggle;

import java.util.Objects;

public class AboutFragment extends Fragment {
    public AboutFragment() {
        // Required empty public constructor
    }


    String TAG = "ABOUT";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_about, container, false);
        ((SideNaviToggle) Objects.requireNonNull(getActivity())).setDrawerEnabled(false);
        Log.i(TAG, "onCreateView: ");
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((SideNaviToggle) Objects.requireNonNull(getActivity())).setDrawerEnabled(true);
    }
}