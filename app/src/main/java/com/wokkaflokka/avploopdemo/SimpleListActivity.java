package com.wokkaflokka.avploopdemo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple activity used to demonstrate an non-terminal failure condition in Espresso's DataInteraction API.
 */
public class SimpleListActivity extends EmptyListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(new ArrayAdapter<>(this, R.layout.list_item_text, R.id.text, new String[]{"oops"}));
    }
}
