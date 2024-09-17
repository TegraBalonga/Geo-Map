package com.example.geomap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FragDelay extends Fragment {

    private View view;
    public ImageView close;

    public Spinner spinDelay;

    public EditText edtTime;

    public Button btnDelaySet;

    MyClass myClass = com.example.geomap.MyClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_delay, container, false);

        close = view.findViewById(R.id.close);

        edtTime = view.findViewById(R.id.edtTime);

        btnDelaySet = view.findViewById(R.id.btnDelaySet);

        close.setOnClickListener(v -> {

            myClass.arrDelaysMakerOp.remove(myClass.arrDelaysMakerOp.size());

            container.removeView(view);

        });

        addListenerOnSpinnerItemSelection();


        btnDelaySet.setOnClickListener(v -> {

            ArrayList<String> arrTime = new ArrayList<>();
            arrTime.add(edtTime.getText().toString());
            myClass.setArrDelaysTime(arrTime);

            myClass.arrDelaysMakerOp.set(myClass.arrDelaysMakerOp.size() - 1, new MarkerOptions()
                    .position(myClass.getArrDelaysMakerOp().get(myClass.arrDelaysMakerOp.size() - 1).getPosition())
                    .title(spinDelay.getSelectedItem().toString() + " at " + myClass.getArrDelaysTime().get(myClass.arrDelaysTime.size() -1)));

           // myClass.arrDelaysType.add(spinDelay.getSelectedItem().toString());


            container.removeView(view);

        });


        return view;
    }


    public void addListenerOnSpinnerItemSelection() {

        spinDelay = view.findViewById(R.id.spinDelay);
        spinDelay.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


}

