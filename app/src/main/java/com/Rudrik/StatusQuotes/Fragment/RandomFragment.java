package com.Rudrik.StatusQuotes.Fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.Rudrik.StatusQuotes.R;
import com.Rudrik.StatusQuotes.utils.Utils;

import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;
import com.mqnvnfx.itwsdvr70223.AdListener;
import com.mqnvnfx.itwsdvr70223.AdView;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * Created by Rudrik on 1/30/14.
 */
public class RandomFragment extends Fragment implements View.OnClickListener {


    private String[] ARR_CATEGORY;
    private LayoutInflater INFLATOR;
    private ViewPager VP;
    private List<String> ARR_STATUS_COLLECTION;
    private ProgressBar PB_FETCHDATACOLLECTION;
    private randomPagesAdapter RANDOM_ADAPTER;
    private List<String> FAV_STATUS;
    private Utils UTILS;
    private Button BTN_NEXT, BTN_PREVIOUS, BTN_SHARE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_random, container,
                false);
        assert rootView != null;
        UTILS = new Utils(getActivity().getApplicationContext());
        PB_FETCHDATACOLLECTION = (ProgressBar) rootView.findViewById(R.id.pbFetchDataCollection);
        BTN_NEXT = (Button) rootView.findViewById(R.id.btnNext);
        BTN_PREVIOUS = (Button) rootView.findViewById(R.id.btnPrevious);
        BTN_SHARE = (Button) rootView.findViewById(R.id.btnShare);
        ARR_CATEGORY = getResources().getStringArray(R.array.categories);
        ARR_STATUS_COLLECTION = new ArrayList<String>();
        BTN_NEXT.setOnClickListener(this);
        BTN_PREVIOUS.setOnClickListener(this);
        BTN_SHARE.setOnClickListener(this);

        this.INFLATOR = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        VP = (ViewPager) rootView.findViewById(R.id.viewPager);

        RANDOM_ADAPTER = new randomPagesAdapter();

        String listValues = UTILS.getPrefrences(getActivity().getResources().getString(R.string.favlist));
        if (!listValues.equals("")) {
            Type collectionType = new TypeToken<Collection<String>>() {
            }.getType();
            FAV_STATUS = new Gson().fromJson(listValues,
                    collectionType);
        } else {
            FAV_STATUS = new ArrayList<String>();
        }
        setHasOptionsMenu(true);
        getRandomData(ARR_CATEGORY);

        // set Callback listener for Static Banner(This method is optional).
        AdView adView = (AdView) rootView.findViewById(R.id.myAdView);
        if (adView != null)
            adView.loadAd();

        return rootView;
    }

    private void getRandomData(String[] arr_category) {
        List<Integer> dataList = new ArrayList<Integer>();
        for (int i = 0; i < arr_category.length; i++) {
            dataList.add(i);
        }
        Collections.shuffle(dataList);

        String _status1 = ARR_CATEGORY[dataList.get(1)];
        String _status2 = ARR_CATEGORY[dataList.get(2)];
        String _status3 = ARR_CATEGORY[dataList.get(3)];
        String _status4 = ARR_CATEGORY[dataList.get(4)];
        String _status5 = ARR_CATEGORY[dataList.get(5)];
        String[] _data = {_status1, _status2, _status3, _status4, _status5};
        new asyncLoadData(_data).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater mInflater = new MenuInflater(getActivity().getApplicationContext());
        mInflater.inflate(R.menu.activity_main_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            Random _random = new Random();
            VP.setCurrentItem(_random.nextInt(ARR_STATUS_COLLECTION.size()));
        } else if (item.getItemId() == R.id.action_share) {
            ShareStatus();
        } else if (item.getItemId() == R.id.action_Like) {
            View _view = VP.findViewWithTag(getActivity().getResources().getString(R.string.statusview) + VP.getCurrentItem());
            String _status = ((TextView) _view.findViewById(R.id.page_textview)).getText().toString();
            FAV_STATUS.add(_status);
            String listValues = new Gson().toJson(FAV_STATUS);
            UTILS.setPrefrences(getActivity().getResources().getString(R.string.favlist), listValues);
            Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.added_to_my_favourite), Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.action_copy) {
            View _view = VP.findViewWithTag(getActivity().getResources().getString(R.string.statusview) + VP.getCurrentItem());
            String _status = ((TextView) _view.findViewById(R.id.page_textview)).getText().toString();
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(_status);
            } else {
                ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Status Message", _status);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(getActivity().getApplicationContext(), "Text Copied To ClipBoard..!!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    // Detach FragmentTabHost
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Remove FragmentTabHost
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                VP.setCurrentItem(VP.getCurrentItem() + 1);
                break;
            case R.id.btnPrevious:
                VP.setCurrentItem(VP.getCurrentItem() - 1);
                break;
            case R.id.btnShare:
                ShareStatus();
                break;
        }
    }

    private void ShareStatus() {
        Intent _intent = new Intent(Intent.ACTION_SEND);
        View _view = VP.findViewWithTag(getActivity().getResources().getString(R.string.statusview) + VP.getCurrentItem());
        String _status = ((TextView) _view.findViewById(R.id.page_textview)).getText().toString();
        _intent.putExtra(Intent.EXTRA_TEXT, _status);
        _intent.setType("text/plain");
        startActivity(_intent);
    }

    public void getData(InputStream fis) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec(getString(R.string.randoms).getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        String str = "";
        int b;
        byte[] d = new byte[1024];
        while ((b = cis.read(d)) != -1) {
            str += new String(d, 0, b);
            System.out.print(new String(d, 0, b));
        }
        cis.close();

        String[] strarr = str.split("\n");
        for (int i = 0; i < strarr.length; i++) {
            ARR_STATUS_COLLECTION.add(strarr[i].trim().toString());
        }
        System.out.println(strarr.length);
    }

    class randomPagesAdapter extends PagerAdapter {

        public randomPagesAdapter() {

        }

        @Override
        public int getCount() {
            return ARR_STATUS_COLLECTION.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = INFLATOR.inflate(R.layout.laytextview, null);
            ((TextView) page.findViewById(R.id.page_textview)).setText(ARR_STATUS_COLLECTION.get(position).toString().trim());
            page.setTag(getActivity().getResources().getString(R.string.statusview) + position);
            ((ViewPager) container).addView(page, 0);
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }
    }


    public class asyncLoadData extends AsyncTask<Void, Void, Void> {

        String[] DATA;

        asyncLoadData(String[] data) {
            this.DATA = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PB_FETCHDATACOLLECTION.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = null;
            for (int i = 0; i < DATA.length; i++) {
                try {
                    inputStream = assetManager.open(DATA[i].toLowerCase());

                    getData(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PB_FETCHDATACOLLECTION.setVisibility(View.INVISIBLE);
            VP.setAdapter(RANDOM_ADAPTER);
        }
    }
}
