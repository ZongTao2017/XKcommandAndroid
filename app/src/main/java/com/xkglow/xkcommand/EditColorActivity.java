package com.xkglow.xkcommand;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.View.EditColorView;

public class EditColorActivity extends Activity {
    EditColorView editColorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_color);
        String type = getIntent().getStringExtra("type");
        editColorView = findViewById(R.id.edit_color_view);

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
                if (type.equals("button_color")) {
                   AppGlobal.setButtonColor(editColorView.getColor());
                }
                if (type.equals("button_warning_color")) {
                    AppGlobal.setWarningButtonColor(editColorView.getColor());
                }
                finish();
            }
        });

        if (type.equals("button_color")) {
            editColorView.setColor(AppGlobal.getSystemData().buttonColor);
        }
        if (type.equals("button_warning_color")) {
            editColorView.setColor(AppGlobal.getSystemData().buttonWarningColor);
        }

        TextView description = findViewById(R.id.description);
        if (type.equals("button_color")) {
            description.setText("Button backlight LED color when it is on.");
        }
        if (type.equals("button_warning_color")) {
            description.setText("Button backlight LED color when certain channel it links to is cut off due to exceeding set safety current.");
        }
    }
}
