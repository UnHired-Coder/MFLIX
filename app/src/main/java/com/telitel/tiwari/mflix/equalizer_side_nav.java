package com.telitel.tiwari.mflix;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * A simple {@link Fragment} subclass.
 */
public class equalizer_side_nav extends Fragment {


    public equalizer_side_nav() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_equalizer_side_nav, container, false);
        ((side_nav_toggle) getActivity()).setDrawerEnabled(false);

        GraphView graph = (GraphView) v.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setThickness(10);
        series.setDrawAsPath(true);
        series.setColor(Color.GREEN);
        series.setDrawBackground(false);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(20);
        series.setBackgroundColor(Color.rgb(4,181,158));
        graph.getViewport().setBackgroundColor(Color.BLACK);
        graph.getViewport().isYAxisBoundsManual();
        graph.getViewport().computeScroll();
        graph.setTitleColor(Color.YELLOW);

        graph.getViewport().setOnXAxisBoundsChangedListener(new Viewport.OnXAxisBoundsChangedListener() {
            @Override
            public void onXAxisBoundsChanged(double minX, double maxX, Reason reason) {
                Log.i("ytytyty","tytytyt");
            }
        });
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                return "";
            }
        });

        graph.setTitleColor(Color.GREEN);
        graph.addSeries(series);
        return v;
    }




    @Override
    public void onDestroyView() {
        ((side_nav_toggle) getActivity()).setDrawerEnabled(true);
        super.onDestroyView();
    }


}
