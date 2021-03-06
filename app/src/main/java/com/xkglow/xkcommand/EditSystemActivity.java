package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.SystemData;
import com.xkglow.xkcommand.View.SwitchView;

public class EditSystemActivity extends Activity {
    private View buttonColorView, buttonWarningColorView;
    FrameLayout minus1, plus1, minus2, plus2;
    ImageView minusImage1, plusImage1, minusImage2, plusImage2;
    SystemData systemData;
    TextView controllerName, cutoffInputText, turnOffBluetoothText, tempCText, tempFText;
    SwitchView switchAutoOff;
    LinearLayout autoOffLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_system);

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGlobal.getCurrentDevice().setSystem(systemData);
                AppGlobal.writeUserSettings();
                finish();
            }
        });

        buttonColorView = findViewById(R.id.button_color);
        buttonWarningColorView = findViewById(R.id.button_warning_color);

        LinearLayout buttonColorLayout = findViewById(R.id.button_color_layout);
        buttonColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSystemActivity.this, EditColorActivity.class);
                intent.putExtra("type", "button_color");
                startActivity(intent);
            }
        });

        LinearLayout buttonWarningColorLayout = findViewById(R.id.button_warning_color_layout);
        buttonWarningColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSystemActivity.this, EditColorActivity.class);
                intent.putExtra("type", "button_warning_color");
                startActivity(intent);
            }
        });

        LinearLayout controllerNameLayout = findViewById(R.id.controller_name_layout);
        controllerNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSystemActivity.this, EditNameActivity.class);
                intent.putExtra("system", systemData);
                startActivity(intent);
            }
        });
        controllerName = findViewById(R.id.controller_name);

        minus1 = findViewById(R.id.minus_auto_cutoff);
        minusImage1 = findViewById(R.id.minus_image_1);
        plus1 = findViewById(R.id.plus_auto_cutoff);
        plusImage1 = findViewById(R.id.plus_image_1);

        minus2 = findViewById(R.id.minus_auto_turn_off);
        minusImage2 = findViewById(R.id.minus_image_2);
        plus2 = findViewById(R.id.plus_auto_turn_off);
        plusImage2 = findViewById(R.id.plus_image_2);

        cutoffInputText = findViewById(R.id.volt);
        turnOffBluetoothText = findViewById(R.id.minutes);

        autoOffLayout = findViewById(R.id.bluetooth_auto_off_layout);
        switchAutoOff = findViewById(R.id.switch_bluetooth_auto_off);
        switchAutoOff.setOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onSwitchOn(boolean showMessage) {
                systemData.bluetoothAutoOff = true;
                autoOffLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSwitchOff(boolean showMessage) {
                systemData.bluetoothAutoOff = false;
                autoOffLayout.setVisibility(View.GONE);
            }
        });

        minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (systemData.cutoffInput > 11.0f) {
                    systemData.cutoffInput -= 0.2f;
                    cutoffInputText.setText(String.format("%.1fV", systemData.cutoffInput));
                }
                setButtons1();
            }
        });

        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (systemData.cutoffInput < 13.0f) {
                    systemData.cutoffInput += 0.2f;
                    cutoffInputText.setText(String.format("%.1fV", systemData.cutoffInput));
                }
                setButtons1();
            }
        });

        minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (systemData.turnBluetoothOffAfter > 2) {
                    systemData.turnBluetoothOffAfter -= 1;
                    turnOffBluetoothText.setText(systemData.turnBluetoothOffAfter + "min");
                }
                setButtons2();
            }
        });

        plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (systemData.turnBluetoothOffAfter < 240) {
                    systemData.turnBluetoothOffAfter += 1;
                    turnOffBluetoothText.setText(systemData.turnBluetoothOffAfter + "min");
                }
                setButtons2();
            }
        });

        tempCText = findViewById(R.id.unit_c);
        tempFText = findViewById(R.id.unit_f);

        tempCText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemData.tempUnitC = true;
                tempCText.setTextColor(getColor(R.color.blue));
                tempFText.setTextColor(getColor(R.color.white));
            }
        });
        tempFText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemData.tempUnitC = false;
                tempCText.setTextColor(getColor(R.color.white));
                tempFText.setTextColor(getColor(R.color.blue));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        systemData = AppGlobal.getCurrentDevice().getSystemData();

        controllerName.setText(systemData.name);
        cutoffInputText.setText(String.format("%.1fV", systemData.cutoffInput));
        turnOffBluetoothText.setText(systemData.turnBluetoothOffAfter + "min");
        switchAutoOff.post(new Runnable() {
            @Override
            public void run() {
                if (systemData.bluetoothAutoOff) {
                    switchAutoOff.switchOn(true, false);
                } else {
                    switchAutoOff.switchOff(true, false);
                }
            }
        });

        if (systemData.tempUnitC) {
            tempCText.setTextColor(getColor(R.color.blue));
            tempFText.setTextColor(getColor(R.color.white));
        } else {
            tempCText.setTextColor(getColor(R.color.white));
            tempFText.setTextColor(getColor(R.color.blue));
        }

        float r = 8;
        ShapeDrawable shape = new ShapeDrawable (new RoundRectShape(new float[] { r, r, r, r, r, r, r, r },null,null));
        shape.getPaint().setColor(AppGlobal.getCurrentDevice().getSystemData().buttonColor);
        buttonColorView.setBackground(shape);

        ShapeDrawable shape2 = new ShapeDrawable (new RoundRectShape(new float[] { r, r, r, r, r, r, r, r },null,null));
        shape2.getPaint().setColor(AppGlobal.getCurrentDevice().getSystemData().buttonWarningColor);
        buttonWarningColorView.setBackground(shape2);
    }

    private void setButtons1() {
        if (Math.abs(systemData.cutoffInput - 11f) < 0.001) {
            minusImage1.setColorFilter(getResources().getColor(R.color.gray));
            minus1.setClickable(false);
        } else if (Math.abs(systemData.cutoffInput - 13f) < 0.001) {
            plusImage1.setColorFilter(getResources().getColor(R.color.gray));
            plus1.setClickable(false);
        } else {
            plusImage1.setColorFilter(getResources().getColor(R.color.blue));
            minusImage1.setColorFilter(getResources().getColor(R.color.blue));
            plus1.setClickable(true);
            minus1.setClickable(true);
        }
    }

    private void setButtons2() {
        if (systemData.turnBluetoothOffAfter == 1) {
            minusImage2.setColorFilter(getResources().getColor(R.color.gray));
            minus2.setClickable(false);
        } else if (systemData.turnBluetoothOffAfter == 240) {
            plusImage2.setColorFilter(getResources().getColor(R.color.gray));
            plus2.setClickable(false);
        } else {
            plusImage2.setColorFilter(getResources().getColor(R.color.blue));
            minusImage2.setColorFilter(getResources().getColor(R.color.blue));
            plus2.setClickable(true);
            minus2.setClickable(true);
        }
    }
}
