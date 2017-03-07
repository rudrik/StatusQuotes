package com.Rudrik.StatusQuotes.Fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.Rudrik.StatusQuotes.R;
import com.Rudrik.StatusQuotes.utils.Utils;

import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MyFavFragment extends ListFragment {

    List<String> FAV_STATUS;
    String SHARE_STATUS;
    ArrayAdapter<String> ADAPTER;
    private Utils UTILS;
    private int SHARE_POSITION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myfav, container,
                false);
        UTILS = new Utils(getActivity().getApplicationContext());

        String listValues = UTILS.getPrefrences(getActivity().getResources().getString(R.string.favlist));
        if (!listValues.equals("")) {
            Type collectionType = new TypeToken<Collection<String>>() {
            }.getType();
            FAV_STATUS = new Gson().fromJson(listValues,
                    collectionType);
        } else {
            FAV_STATUS = new ArrayList<String>();
        }
        ADAPTER = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.favlaylistitemtext,
                R.id.page_textview, FAV_STATUS);
        setListAdapter(ADAPTER);
        return rootView;
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
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SHARE_STATUS = getListView().getItemAtPosition(i).toString();
                SHARE_POSITION = i;
                return false;
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.hold_on_status_for_sharing), Toast.LENGTH_SHORT).show();
            }
        });

        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            return;
        }
        menu.setHeaderTitle(getActivity().getResources().getString(R.string.select));
        menu.add(0, v.getId(), 0, getActivity().getResources().getString(R.string.share));
        menu.add(0, v.getId(), 0, getActivity().getResources().getString(R.string.remove));
        menu.add(0, v.getId(), 0, getActivity().getResources().getString(R.string.copy));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == getActivity().getResources().getString(R.string.share)) {
            ShareStatus();
        } else if (item.getTitle() == getActivity().getResources().getString(R.string.remove)) {
            FAV_STATUS.remove(SHARE_POSITION);
            String listValues = new Gson().toJson(FAV_STATUS);
            UTILS.setPrefrences(getActivity().getResources().getString(R.string.favlist), listValues);
            ADAPTER.notifyDataSetChanged();
        } else if (item.getTitle() == getActivity().getResources().getString(R.string.copy)) {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(SHARE_STATUS);
            } else {
                ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Status Message", SHARE_STATUS);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(getActivity().getApplicationContext(), "Text Copied To ClipBoard..!!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void ShareStatus() {
        Intent _intent = new Intent(Intent.ACTION_SEND);
        _intent.putExtra(Intent.EXTRA_TEXT, SHARE_STATUS);
        _intent.setType("text/plain");
        startActivity(_intent);
    }
}
