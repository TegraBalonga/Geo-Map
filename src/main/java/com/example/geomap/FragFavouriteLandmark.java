package com.example.geomap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragFavouriteLandmark extends Fragment {

    private View view;
    public ImageView close;
    public Button btnShowOnMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourite_landmark,container,false);

        close = view.findViewById(R.id.close);

        btnShowOnMap = view.findViewById(R.id.btnShowLandmark);

        btnShowOnMap.setOnClickListener(v ->{


        });

        close.setOnClickListener(v -> container.removeView(view));

        return view;
    }
}
