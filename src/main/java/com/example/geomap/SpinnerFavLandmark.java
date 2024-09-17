package com.example.geomap;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SpinnerFavLandmark extends Home{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        Spinner s = findViewById(R.id.spinFavLandmark);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,

                android.R.layout.simple_spinner_item, myClass.favId);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s.setAdapter(adapter);
    }
}
