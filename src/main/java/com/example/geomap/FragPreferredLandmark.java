package com.example.geomap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragPreferredLandmark extends Fragment {

    private View view;
    private ImageView close;
    private Button btnHis, btnMod, btnPop;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preferred_landmark, container, false);

        close = view.findViewById(R.id.close);

        close.setOnClickListener(v -> container.removeView(view));

        MyClass myClass = com.example.geomap.MyClass.getInstance();

        btnHis = view.findViewById(R.id.btnHistorical);

        btnHis.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Home.class);
            myClass.setLandmarkType("historical");
            startActivity(intent);

            container.removeView(view);

        });


        btnMod = view.findViewById(R.id.btnModern);

        btnMod.setOnClickListener(v ->
        {

            Intent intent = new Intent(getActivity(), Home.class);
            myClass.setLandmarkType("modern");
            startActivity(intent);

            container.removeView(view);

        });


        btnPop = view.findViewById(R.id.btnPopular);

        btnPop.setOnClickListener(v ->
        {

            Intent intent = new Intent(getActivity(), Home.class);
            myClass.setLandmarkType("popular");
            startActivity(intent);

            container.removeView(view);

        });


        return view;
    }
}
