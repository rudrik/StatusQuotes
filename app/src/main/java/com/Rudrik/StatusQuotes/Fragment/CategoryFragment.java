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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by Rudrik on 3/14/14.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {

    private List<String> FAV_STATUS;
    private LayoutInflater INFLATER;
    private ArrayList<String> ARR_STATUS;
    private ProgressBar PB_FETCHDATA;
    private ViewPager VP;
    private Button BTN_NEXT, BTN_PREVIOUS, BTN_SHARE;
    private int CURRENT_POSITION;
    private statusPagesAdapter STATUS_PAGERADPT;
    private String CATEGORY = "";
    private Utils UTILS;
    private TextView TV_ITEMNO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statusmsg, container, false);
        assert rootView != null;
        UTILS = new Utils(getActivity().getApplicationContext());
        PB_FETCHDATA = (ProgressBar) rootView.findViewById(R.id.pbFetchData);
        BTN_NEXT = (Button) rootView.findViewById(R.id.btnNext);
        BTN_PREVIOUS = (Button) rootView.findViewById(R.id.btnPrevious);
        BTN_SHARE = (Button) rootView.findViewById(R.id.btnShare);
        TV_ITEMNO = (TextView) rootView.findViewById(R.id.tvItemNumber);

        BTN_NEXT.setOnClickListener(this);
        BTN_PREVIOUS.setOnClickListener(this);
        BTN_SHARE.setOnClickListener(this);
        CATEGORY = getArguments().getString(getActivity().getResources().getString(R.string.category));
        ARR_STATUS = new ArrayList<String>();
        this.INFLATER = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        VP = (ViewPager) rootView.findViewById(R.id.vpStatusMessage);
        STATUS_PAGERADPT = new statusPagesAdapter();


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
        new asyncLoadData().execute();

        VP.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                TV_ITEMNO.setText((VP.getCurrentItem() + 1) + " / " + ARR_STATUS.size());
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        AdView adView = (AdView) rootView.findViewById(R.id.myAdView);
        if (adView != null)
            adView.loadAd();
        return rootView;
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
            VP.setCurrentItem(_random.nextInt(ARR_STATUS.size()));
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            System.out.print(new String(d, 0, b));//fos.write(d, 0, b);
        }
        System.out.println(" ");
        System.out.println(str);//fos.write(d, 0, b);
        cis.close();

        String[] strarr = str.split("\n");
        for (int i = 0; i < strarr.length; i++) {
            ARR_STATUS.add(strarr[i].trim().toString());
        }
        System.out.println(strarr.length);
    }

    public class asyncLoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PB_FETCHDATA.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                AssetManager assetManager = getResources().getAssets();
                InputStream inputStream = null;

                inputStream = assetManager.open(CATEGORY.toLowerCase());
                getData(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }

//            ARR_STATUS = GetData.getData(CATEGORY.toLowerCase(), getActivity().getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PB_FETCHDATA.setVisibility(View.INVISIBLE);
            VP.setAdapter(STATUS_PAGERADPT);
        }
    }

    //Implement PagerAdapter Class to handle individual page creation
    private class statusPagesAdapter extends PagerAdapter {

        public statusPagesAdapter() {

        }

        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return ARR_STATUS.size();
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = INFLATER.inflate(R.layout.laytextview, null);
            ((TextView) page.findViewById(R.id.page_textview)).setText(ARR_STATUS.get(position).toString().trim());
            CURRENT_POSITION = position;
            //Add the page to the front of the queue
            page.setTag(getActivity().getResources().getString(R.string.statusview) + position);
            ((ViewPager) container).addView(page, 0);
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }
    }
}
