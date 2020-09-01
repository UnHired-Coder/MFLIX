package com.telitel.tiwari.mflix;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.telitel.tiwari.mflix.Database.StorageUtil;
import com.telitel.tiwari.mflix.RecyclerViewAdapters.SongsRecyclerViewAdapter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

public class PlayerView extends BottomSheetDialogFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Toast.makeText(getContext(), "Now Playing PlayerView" + StorageUtil.getInstance(getContext()).loadAudioIndex(), Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_player_view, container, false);
        Toast.makeText(getContext(), "PlayerView" + StorageUtil.getInstance(getContext()).loadAudioIndex(), Toast.LENGTH_LONG).show();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getContext() != null)
            StorageUtil.registerPres(getContext(), this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getContext() != null)
            StorageUtil.unRegisterPres(getContext(), this);
    }
}
