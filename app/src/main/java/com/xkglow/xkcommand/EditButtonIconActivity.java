package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.PhotoData;
import com.xkglow.xkcommand.Helper.RecyclerViewAdapter;

import java.util.ArrayList;

public class EditButtonIconActivity extends Activity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ButtonData buttonData;
    int resourceId;

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

        FrameLayout done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.type = 2;
                buttonData.iconResourceId = resourceId;
                AppGlobal.setButton(buttonData);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        ArrayList<PhotoData> photos = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            photos.add(new PhotoData(AppGlobal.getIconResourceId(i)));
        }
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
                resourceId = AppGlobal.getIconResourceId(position);
            }

            @Override
            public void onCameraClick() {

            }
        });
    }


}
