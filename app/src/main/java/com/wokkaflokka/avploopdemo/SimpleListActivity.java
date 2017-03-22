package com.wokkaflokka.avploopdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple activity used to demonstrate an non-terminal failure condition in Espresso's DataInteraction API.
 */
public class SimpleListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(new ArrayAdapter<>(this, R.layout.list_item_text, R.id.text, new String[]{"oops"}));
    }
}
