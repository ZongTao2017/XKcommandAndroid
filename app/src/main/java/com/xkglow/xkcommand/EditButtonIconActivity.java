package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.PhotoData;
import com.xkglow.xkcommand.Helper.RecyclerViewAdapter;

import java.util.ArrayList;

public class EditButtonIconActivity extends Activity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ButtonData buttonData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_button_icon);

        buttonData = (ButtonData) getIntent().getSerializableExtra("button");

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        ArrayList<PhotoData> photos = new ArrayList<>();
        photos.add(new PhotoData(R.drawable.button_icon1));
        photos.add(new PhotoData(R.drawable.button_icon2));
        photos.add(new PhotoData(R.drawable.button_icon3));
        photos.add(new PhotoData(R.drawable.button_icon4));
        photos.add(new PhotoData(R.drawable.button_icon5));
        photos.add(new PhotoData(R.drawable.button_icon6));
        photos.add(new PhotoData(R.drawable.button_icon7));
        photos.add(new PhotoData(R.drawable.button_icon8));
        boolean horizontal = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int number = 4;
        if (horizontal) {
            number = 8;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(EditButtonIconActivity.this, number, RecyclerView.VERTICAL, false));
        adapter = new RecyclerViewAdapter(EditButtonIconActivity.this, 0, photos, number);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickLister(new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCameraClick() {

            }
        });
    }


}
