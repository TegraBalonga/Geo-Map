package com.example.geomap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class FragChangeMap extends Fragment {


    private View view;
    public ImageView close, def, ter,sat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_change_map,container,false);

        close = view.findViewById(R.id.close);

        def = view.findViewById(R.id.ivDefault);

        ter = view.findViewById(R.id.ivTerrain);

        sat = view.findViewById(R.id.ivSatellite);

        MyClass myClass = com.example.geomap.MyClass.getInstance();

        close.setOnClickListener(v -> container.removeView(view));

        def.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Home.class);
            myClass.setMapType("default");
            startActivity(intent);

        });



        ter.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Home.class);
            myClass.setMapType("terrain");
            startActivity(intent);

        });



        sat.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Home.class);
            myClass.setMapType("satellite");
            startActivity(intent);

        });



        return view;
    }



}
