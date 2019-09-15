package com.example.dan.imtdictionary;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import static com.example.dan.imtdictionary.MainActivity.database;

public class specialized_words extends AppCompatActivity {

    ArrayList<String> specArray;
    ArrayAdapter<String> specArrayAdapter;
    ListView lvSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialized_words);
        addControl();
        addEvents();
        createWordsList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void addEvents() {
        lvSpec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(specialized_words.this, MainActivity.class);
                intent.putExtra("txtInput",specArray.get(position));
                intent.putExtra("isSpec", true);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        lvSpec = findViewById(R.id.lvSpec);
        specArray = new ArrayList<>();
        specArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, specArray);
        lvSpec.setAdapter(specArrayAdapter);
    }

    private void createWordsList()
    {
        Cursor cursor = database.query("Sheet1",null,null,null,null,null,null);
        specArray.clear();
        while(cursor.moveToNext()) {
            if(cursor.getString(5)!= null) {specArray.add(cursor.getString(5));}
        }
        cursor.close();
        specArrayAdapter.notifyDataSetChanged();
    }
}
