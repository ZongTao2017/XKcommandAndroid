package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.xkglow.xkcommand.Helper.AppGlobal;

public class EditSystemActivity extends Activity {
    private View buttonColorView, buttonWarningColorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_system);

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonColorView = findViewById(R.id.button_color);
        buttonWarningColorView = findViewById(R.id.button_warning_color);

        FrameLayout buttonColorLayout = findViewById(R.id.button_color_layout);
        buttonColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSystemActivity.this, EditColorActivity.class);
                intent.putExtra("type", "button_color");
                startActivity(intent);
            }
        });

        FrameLayout buttonWarningColorLayout = findViewById(R.id.button_warning_color_layout);
        buttonWarningColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSystemActivity.this, EditColorActivity.class);
                intent.putExtra("type", "button_warning_color");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        float r=8;
        ShapeDrawable shape = new ShapeDrawable (new RoundRectShape(new float[] { r, r, r, r, r, r, r, r },null,null));
        shape.getPaint().setColor(AppGlobal.getSystemData().buttonColor);
        buttonColorView.setBackground(shape);

        ShapeDrawable shape2 = new ShapeDrawable (new RoundRectShape(new float[] { r, r, r, r, r, r, r, r },null,null));
        shape2.getPaint().setColor(AppGlobal.getSystemData().buttonWarningColor);
        buttonWarningColorView.setBackground(shape2);
    }
}
