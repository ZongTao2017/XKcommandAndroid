package com.xkglow.newapp;

import static com.xkglow.newapp.Helper.Helper.ICON_SIZE;
import static com.xkglow.newapp.Helper.Helper.PADDING;
import static com.xkglow.newapp.Helper.Helper.STATUS_RATIO;
import static com.xkglow.newapp.Helper.Helper.dpToPx;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabHost mTabHost;
    private Fragment mCurrentFragment;

    public static final String CONTROL_TAB = "CONTROL_TAB";
    public static final String CUSTOMIZE_TAB = "CUSTOMIZE_TAB";
    public static final String CIRCLE_TAB = "CIRCLE_TAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mTabHost = findViewById(R.id.tabhost);
        mTabHost.setup();
        initTabs();

        final ImageView logo = findViewById(R.id.logo);
        final FrameLayout signal = findViewById(R.id.signal);
        final FrameLayout layout = findViewById(R.id.activity_main_real_tab_content);
        final ImageView image = findViewById(R.id.status_bar);
        layout.post(new Runnable() {
            @Override
            public void run() {
                int height = layout.getHeight();
                int width = layout.getWidth();
                boolean horizontal = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) image.getLayoutParams();

                int paddingV = height * PADDING / (PADDING * 5 + ICON_SIZE * 4);
                int iconSize = height * ICON_SIZE / (PADDING * 5 + ICON_SIZE * 4);
                int paddingH = (width - iconSize * 2) / 3;
                layoutParams.width = (int) ((iconSize * 2 + paddingH) * STATUS_RATIO);

                if (horizontal) {
                    paddingV = height * PADDING / (PADDING * 3 + ICON_SIZE * 2);
                    iconSize = height * ICON_SIZE / (PADDING * 3 + ICON_SIZE * 2);
                    paddingH = (width - iconSize * 4) / 4;
                    layoutParams.height = (int) ((iconSize * 2 + paddingV) * STATUS_RATIO);

                    FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) logo.getLayoutParams();
                    layoutParams1.leftMargin = dpToPx(MainActivity.this, 60) +
                            (int) (iconSize * 2 + paddingH * 1.5f - layoutParams1.width / 2);
                    logo.setLayoutParams(layoutParams1);

                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) signal.getLayoutParams();
                    layoutParams2.leftMargin = dpToPx(MainActivity.this, 60) +
                            iconSize * 4 + paddingH * 3 - layoutParams2.width;
                    signal.setLayoutParams(layoutParams2);


                }

                image.setLayoutParams(layoutParams);
            }
        });
    }

    private void initTabs() {
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case CONTROL_TAB:
                        mCurrentFragment = new ControlFragment();
                        break;
                    case CUSTOMIZE_TAB:
                        mCurrentFragment = new CustomizeFragment();
                        break;
                    case CIRCLE_TAB:
                        mCurrentFragment = new CircleFragment();
                        break;
                }
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.activity_main_real_tab_content, mCurrentFragment, tabId).commit();
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
}