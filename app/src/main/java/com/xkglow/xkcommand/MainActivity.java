package com.xkglow.xkcommand;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.View.DeviceList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabHost mTabHost;
    private int mCurrentTab;
    private TextView mTitle;
    private ImageView mLogo;
    private DeviceList mDeviceList;

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
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
}