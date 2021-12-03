package com.xkglow.xkcommand;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.View.DeviceList;
import com.xkglow.xkcommand.bluetooth.BluetoothService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabHost mTabHost;
    private int mCurrentTab;
    private TextView mTitle;
    private ImageView mLogo;
    private DeviceList mDeviceList;
    private boolean alreadyStarted = false;

    public static final String CONTROL_TAB = "CONTROL_TAB";
    public static final String CUSTOMIZE_TAB = "CUSTOMIZE_TAB";
    public static final String CIRCLE_TAB = "CIRCLE_TAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        final FrameLayout slidingBtn = findViewById(R.id.sliding_btn);
        slidingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        mLogo = findViewById(R.id.logo);
        mTitle = findViewById(R.id.controller_name);

        mTabHost = findViewById(R.id.tabhost);
        mTabHost.setup();
        initTabs();

        if (savedInstanceState != null)
            mCurrentTab = savedInstanceState.getInt("current_tab");
        else
            mCurrentTab = -1;
        if (mCurrentTab != -1) mTabHost.setCurrentTab(mCurrentTab);

        mDeviceList = findViewById(R.id.device_list);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                checkPermissions();
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, AppGlobal.REQUEST_ENABLE_BT);
            }
        }

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

//        if (AppGlobal.hasNoPairedDevices()) {
//            startActivity(new Intent(MainActivity.this, DevicePairActivity.class));
//        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppGlobal.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            checkPermissions();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppGlobal.REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBluetoothService();
            }
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(MessageEvent event) {
        switch (event.type) {
            case CHANGE_DEVICE:
                mTitle.setText(AppGlobal.getCurrentDevice().getSystemData().name);
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case CHANGE_DEVICE_LIST:
                int index = (int) event.data;
                mTitle.setText(AppGlobal.getCurrentDevice().getSystemData().name);
                mDeviceList.setCurrent(index);
                break;
        }
    }

    private void initTabs() {
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mLogo.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
                Fragment fragment = null;
                switch (tabId) {
                    case CUSTOMIZE_TAB:
                        mCurrentTab = 1;
                        fragment = new CustomizeFragment();
                        mLogo.setVisibility(View.GONE);
                        mTitle.setVisibility(View.VISIBLE);
                        mTitle.setText(AppGlobal.getCurrentDevice().getSystemData().name);
                        break;
                    case CIRCLE_TAB:
                        mCurrentTab = 2;
                        fragment = new CircleFragment();
                        break;
                    case CONTROL_TAB:
                    default:
                        mCurrentTab = 0;
                        fragment = new ControlFragment();
                        break;
                }
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.activity_main_real_tab_content, fragment, tabId).commit();
            }
        };
        mTabHost.setOnTabChangedListener(tabChangeListener);

        TabHost.TabSpec tabSpec;
        tabSpec = mTabHost.newTabSpec(CONTROL_TAB);
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return findViewById(R.id.activity_main_real_tab_content);
            }
        });
        tabSpec.setIndicator(createTabView(R.drawable.ic_menu_control_selector, "Control"));
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec(CUSTOMIZE_TAB);
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return findViewById(R.id.activity_main_real_tab_content);
            }
        });
        tabSpec.setIndicator(createTabView(R.drawable.ic_menu_circle_selector, "Customize"));
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec(CIRCLE_TAB);
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return findViewById(R.id.activity_main_real_tab_content);
            }
        });
        tabSpec.setIndicator(createTabView(R.drawable.ic_menu_customize_selector, "Circle"));
        mTabHost.addTab(tabSpec);
    }

    private View createTabView(final int id, final String text) {
        View view = View.inflate(this, R.layout.tabs_icon, null);
        ImageView imageView = view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, id));
        TextView textView = view.findViewById(R.id.tab_text);
        textView.setText(text);
        return view;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("current_tab", mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentTab == 0) {

        }
    }

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            startBluetoothService();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissions.size() > 0) {
            String[] list = new String[permissions.size()];
            int i = 0;
            for (String per : permissions)
                list[i++] = per;
            ActivityCompat.requestPermissions(this, list, AppGlobal.REQUEST_LOCATION);
        }
    }

    private void startBluetoothService() {
        if (!alreadyStarted) {
            Intent gattServiceIntent = new Intent(this, BluetoothService.class);
            getApplicationContext().bindService(gattServiceIntent,
                    mServiceConnection, BIND_AUTO_CREATE);
            alreadyStarted = true;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppGlobal.bluetoothService = ((BluetoothService.LocalBinder) service).getService();
            AppGlobal.bluetoothService.init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AppGlobal.bluetoothService = null;
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        if (AppGlobal.bluetoothService == null) {
                            checkPermissions();
                        } else {
                            AppGlobal.bluetoothService.startScanTimers();
                        }
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        if (AppGlobal.bluetoothService != null) {
                            AppGlobal.bluetoothService.stopScanTimers();
                        }
                        AppGlobal.turnOffBluetooth();
                        break;
                }
            }
        }
    };
}