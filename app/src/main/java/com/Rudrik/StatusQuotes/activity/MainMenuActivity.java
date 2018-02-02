package com.Rudrik.StatusQuotes.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.Rudrik.StatusQuotes.Adapters.GridViewAdapter;
import com.Rudrik.StatusQuotes.R;
import com.Rudrik.StatusQuotes.pojo.MenuDo;

import com.mqnvnfx.itwsdvr70223.AdConfig;
import com.mqnvnfx.itwsdvr70223.AdListener;
import com.mqnvnfx.itwsdvr70223.AdView;
import com.mqnvnfx.itwsdvr70223.Main;

import java.util.ArrayList;


/**
 * Created by Rudrik on 1/3/14.
 */
public class MainMenuActivity extends Activity implements AdListener {


    private GridView GRID_VIEW;
    private ArrayList<MenuDo> GRID_ARR;
    private GridViewAdapter ADPT_GRID;
    AdView adView;
    private boolean enableCaching = true;
    private Main main; //Declare here

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdConfig.setAppId(211445);
        AdConfig.setApiKey("1346129446702238128");

        AdConfig.setCachingEnabled(enableCaching);
        AdConfig.setTestMode(false);
        AdConfig.setPlacementId(0);
        AdView.setAdListener(this);

        setContentView(R.layout.mainmenupage);
        main = new Main(this, this);
        initialPageControls();

        adView = (AdView) findViewById(R.id.myAdView);
        if (adView != null)
            adView.loadAd();

////        FB KEY GEN
//        try {
//            new clsGeneral().generateHashKeyForFacebook(getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void initialPageControls() {
        GRID_ARR = new ArrayList<MenuDo>();
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        GRID_VIEW = (GridView) findViewById(R.id.homeGridView);

        Bitmap Categories = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.category);
        Bitmap MyFav = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.favourite);
        Bitmap Random = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.shuffle);
        Bitmap RateUs = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.like);
        Bitmap Exit = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.logout);

        GRID_ARR.add(new MenuDo(Categories, getResources().getString(R.string.categories)));
        GRID_ARR.add(new MenuDo(MyFav, getResources().getString(R.string.my_favourites)));
        GRID_ARR.add(new MenuDo(Random, getResources().getString(R.string.random)));
        GRID_ARR.add(new MenuDo(RateUs, getResources().getString(R.string.rate_us)));
        GRID_ARR.add(new MenuDo(Exit, getResources().getString(R.string.exit)));

        ADPT_GRID = new GridViewAdapter(MainMenuActivity.this,
                R.layout.grid_row, GRID_ARR);

        GRID_VIEW.setAdapter(ADPT_GRID);

        GRID_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    startActivity(new Intent(MainMenuActivity.this, CategoryActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else if (position == 1) {
                    // TODO My Fav
                    Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                    intent.putExtra(getResources().getString(R.string.selected_fragment), "1");
                    intent.putExtra(getResources().getString(R.string.category), getResources().getString(R.string.none));
                    startActivity(intent);
                } else if (position == 2) {
                    // TODO Random
                    Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                    intent.putExtra(getResources().getString(R.string.selected_fragment), "2");
                    intent.putExtra(getResources().getString(R.string.category), getResources().getString(R.string.none));
                    startActivity(intent);
                } else if (position == 3) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.Rudrik.StatusQuotes")));
                } else if (position == 4) {
                    // TODO Exit
                    new AlertDialog.Builder(MainMenuActivity.this)
                            .setIcon(R.drawable.icon)
                            .setTitle(getResources().getString(R.string.are_you_sure_want_to_exit))
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    MainMenuActivity.super.onBackPressed();
                                }
                            }).create().show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon)
                .setTitle(getResources().getString(R.string.are_you_sure_want_to_exit))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainMenuActivity.super.onBackPressed();
                    }
                }).create().show();
    }


    @Override
    public void onAdCached(AdConfig.AdType adType) {
        showToast("Ad cached: " + adType);

    }

    @Override
    public void onError(ErrorType errorType, String s) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdLoaded() {

    }

    @Override
    public void onAdExpanded() {

    }

    @Override
    public void onAdClicked() {

    }

    @Override
    public void onAdClosed() {
        showToast("Ad closed");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }


}