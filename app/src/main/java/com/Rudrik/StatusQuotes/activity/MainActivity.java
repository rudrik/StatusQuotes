package com.Rudrik.StatusQuotes.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.Rudrik.StatusQuotes.Adapters.GridViewAdapter;
import com.Rudrik.StatusQuotes.Adapters.MenuListAdapter;
import com.Rudrik.StatusQuotes.Fragment.CategoryFragment;
import com.Rudrik.StatusQuotes.Fragment.MyFavFragment;
import com.Rudrik.StatusQuotes.Fragment.RandomFragment;
import com.Rudrik.StatusQuotes.R;

import com.mqnvnfx.itwsdvr70223.AdConfig;
import com.mqnvnfx.itwsdvr70223.AdListener;
import com.mqnvnfx.itwsdvr70223.AdView;
import com.mqnvnfx.itwsdvr70223.EulaListener;
import com.mqnvnfx.itwsdvr70223.Main;


public class MainActivity extends ActionBarActivity implements AdListener, EulaListener {


    private boolean IS_ADCACHED;
    private int SELECTED_FRAGEMENT;
    private DrawerLayout DRAWER_LAYOUT;
    private ListView DRAWER_LIST_VIEW;
    private ActionBarDrawerToggle DRAWER_TOGGLE;
    private MenuListAdapter ADPT_MENULST;
    private String[] ARR_TITLE;
    private String[] ARR_SUBTITLE;
    private int[] ARR_ICON;
    private Fragment MYFAV_FRAG;
    private Fragment RANDOM_FRAG;
    private Fragment CATEGORY_FRAG;
    private String TITLE = "";
    private String CATEGORY;

    private Main main; //Declare here
    private GridViewAdapter ADPT_GRID;
    AdView adView;
    private boolean enableCaching = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_main);


//        AdConfig.setAppId(211445);
//        AdConfig.setApiKey("1346129446702238128");
//        AdConfig.setEulaListener(this);
//        AdConfig.setAdListener(this);
//        AdConfig.setCachingEnabled(enableCaching);
//        AdConfig.setTestMode(true);
        initialPageControls(savedInstanceState);
    }

    private void initialPageControls(Bundle savedInstanceState) {
        MYFAV_FRAG = new MyFavFragment();
        RANDOM_FRAG = new RandomFragment();
        CATEGORY_FRAG = new CategoryFragment();
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        String val = this.getIntent().getExtras().getString(getResources().getString(R.string.selected_fragment));
        SELECTED_FRAGEMENT = Integer.valueOf(val);
        CATEGORY = this.getIntent().getExtras().getString(getResources().getString(R.string.category));

        ARR_TITLE = new String[]{getResources().getString(R.string.home), getResources().getString(R.string.categories)
                , getResources().getString(R.string.my_favourites),
                getResources().getString(R.string.go_ahead_amuse_me)};

        ARR_SUBTITLE = new String[]{getResources().getString(R.string.go_to_home_page)
                , getResources().getString(R.string.categories), getResources().getString(R.string.go_to_my_fav),
                getResources().getString(R.string.random_it)};

        ARR_ICON = new int[]{R.drawable.home, R.drawable.menucategory, R.drawable.favourite,
                R.drawable.menushuffle};

        DRAWER_LAYOUT = (DrawerLayout) findViewById(R.id.drawer_layout);
        DRAWER_LIST_VIEW = (ListView) findViewById(R.id.left_drawer);
        DRAWER_LAYOUT.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        ADPT_MENULST = new MenuListAdapter(this, ARR_TITLE, ARR_SUBTITLE, ARR_ICON);
        DRAWER_LIST_VIEW.setAdapter(ADPT_MENULST);
        DRAWER_LIST_VIEW.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DRAWER_TOGGLE = new ActionBarDrawerToggle(this, DRAWER_LAYOUT,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(TITLE);
            }

            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }
        };

        DRAWER_LAYOUT.setDrawerListener(DRAWER_TOGGLE);

        if (savedInstanceState == null) {
            if (CATEGORY.equals(getResources().getString(R.string.none))) {
                selectItem(SELECTED_FRAGEMENT + 1);
            } else {
                selectItem(10);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (DRAWER_LAYOUT.isDrawerOpen(DRAWER_LIST_VIEW)) {
                DRAWER_LAYOUT.closeDrawer(DRAWER_LIST_VIEW);
            } else {
                DRAWER_LAYOUT.openDrawer(DRAWER_LIST_VIEW);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case 2:
                getSupportActionBar().setTitle(getResources().getString(R.string.my_favourites));
                this.TITLE = getSupportActionBar().getTitle().toString();
                ft.replace(R.id.content_frame, MYFAV_FRAG);
                break;
            case 3:
                getSupportActionBar().setTitle(getResources().getString(R.string.random_status));
                this.TITLE = getSupportActionBar().getTitle().toString();
                ft.replace(R.id.content_frame, RANDOM_FRAG);
                break;
            case 10:
                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.category), CATEGORY);
                CATEGORY_FRAG.setArguments(bundle);
                getSupportActionBar().setTitle(CATEGORY + " " + getResources().getString(R.string.status));
                this.TITLE = getSupportActionBar().getTitle().toString();
                ft.replace(R.id.content_frame, CATEGORY_FRAG);
                break;
        }
        ft.commit();
        DRAWER_LIST_VIEW.setItemChecked(position, true);
        DRAWER_LAYOUT.closeDrawer(DRAWER_LIST_VIEW);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DRAWER_TOGGLE.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DRAWER_TOGGLE.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        try {
            main.showCachedAd(AdConfig.AdType.smartwall);   //This will display the ad but it wont close the app.
        } catch (Exception e) {
            // close the activity if ad is not available.
            finish();
        }
    }

    @Override
    public void optinResult(boolean isAccepted) {
        if (isAccepted)
            showToast("You have accepted the EULA.");
        else
            showToast("You have not accepted the EULA.");
    }

    @Override
    public void showingEula() {
        showToast("EULA is showing.");
    }

    @Override
    public void onAdCached(AdConfig.AdType adType) {
        showToast("Ad cached: " + adType);

    }

    @Override
    public void onIntegrationError(String errorMessage) {
        showToast("Integration Error: " + errorMessage);

    }

    @Override
    public void onAdError(String errorMessage) {
        showToast("Ad error: " + errorMessage);
    }

    @Override
    public void noAdListener() {
        showToast("No ad received");
    }

    @Override
    public void onAdShowing() {
        showToast("Showing SmartWall ad");
    }

    @Override
    public void onAdClosed() {
        showToast("Ad closed");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdLoadingListener() {
        showToast("Ad is loaading");
    }

    @Override
    public void onAdLoadedListener() {
        showToast("Ad  is loaded");
    }

    @Override
    public void onCloseListener() {
        showToast("Ad closed");
    }

    @Override
    public void onAdExpandedListner() {
        showToast("Ad onAdExpandedListner");
    }

    @Override
    public void onAdClickedListener() {
        showToast("Ad onAdClickedListener");
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

}
