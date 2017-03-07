package com.Rudrik.StatusQuotes.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.Rudrik.StatusQuotes.R;
import com.Rudrik.StatusQuotes.pojo.MenuDo;

import java.util.ArrayList;

/**
 * @author R3Techno
 */
public class GridViewAdapter extends ArrayAdapter<MenuDo> {
    private Context CONTEXT;
    private int LAYOUT_RES_ID;
    private ArrayList<MenuDo> DATA = new ArrayList<MenuDo>();

    public GridViewAdapter(Context context, int layoutResourceId,
                           ArrayList<MenuDo> data) {
        super(context, layoutResourceId, data);
        this.LAYOUT_RES_ID = layoutResourceId;
        this.CONTEXT = context;
        this.DATA = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) CONTEXT).getLayoutInflater();
            row = inflater.inflate(LAYOUT_RES_ID, parent, false);

            holder = new RecordHolder();
            holder.TV_TITLE = (TextView) row.findViewById(R.id.item_text);
            holder.IMG_ITEM = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        MenuDo item = DATA.get(position);
        holder.TV_TITLE.setText(item.getTitle());
        holder.IMG_ITEM.setImageBitmap(item.getImage());
        return row;

    }

    static class RecordHolder {
        TextView TV_TITLE;
        ImageView IMG_ITEM;
    }
}