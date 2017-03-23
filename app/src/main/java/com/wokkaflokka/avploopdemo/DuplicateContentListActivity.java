package com.wokkaflokka.avploopdemo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * List activity that renders duplicate content.
 */
public class DuplicateContentListActivity extends EmptyListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(new ArrayAdapter<>(this, R.layout.list_item_text, R.id.text, new String[]{"oops", "oops"}));
    }
}
