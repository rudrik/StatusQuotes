package com.Rudrik.StatusQuotes.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.Rudrik.StatusQuotes.R;


/**
 * Created by Rudrik on 3/14/14.
 */
public class CategoryActivity extends Activity {

    private String[] ARR_CATEGORY;
    private ListView LST_CATEGORY;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        ARR_CATEGORY = getResources().getStringArray(R.array.categories);

        LST_CATEGORY = (ListView) findViewById(R.id.lstCategories);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryActivity.this, R.layout.laylistitemtext,
                R.id.page_textview, ARR_CATEGORY);
        LST_CATEGORY.setAdapter(adapter);

        LST_CATEGORY.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String value = (String) adapter.getItem(i);
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                intent.putExtra(getResources().getString(R.string.category), value);
                intent.putExtra(getResources().getString(R.string.selected_fragment), "0");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}