package com.telitel.tiwari.mflix.Screens.MainScreens;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telitel.tiwari.mflix.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToolsPage extends Fragment {


    public ToolsPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tools_page, container, false);

        return v;

    }

}
