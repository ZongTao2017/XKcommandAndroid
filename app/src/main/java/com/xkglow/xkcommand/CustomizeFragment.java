package com.xkglow.xkcommand;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomizeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customize, container, false);
        FrameLayout controller = view.findViewById(R.id.controller);
        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectButtonActivity.class));
            }
        });
        FrameLayout sensor = view.findViewById(R.id.sensor);
        sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectSensorActivity.class));
            }
        });
        FrameLayout channel = view.findViewById(R.id.channel);
        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectChannelActivity.class));
            }
        });
        FrameLayout system = view.findViewById(R.id.system);
        system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditSystemActivity.class));
            }
        });
        return view;
    }
}
