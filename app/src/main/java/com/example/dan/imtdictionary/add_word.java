package com.example.dan.imtdictionary;
import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.dan.imtdictionary.MainActivity.database;

public class add_word extends AppCompatActivity {
    EditText txtGetEn, txtGetVi, txtGetFr, txtGetSpec;
    Button btnAdd, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        addControl();
        addEvents();
    }

    private void addEvents() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGetEn.setText("");
                txtGetVi.setText("");
                txtGetFr.setText("");
                txtGetSpec.setText("");
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_word_to_dict();
            }
        });
    }

    private void add_word_to_dict() {
        if (TextUtils.isEmpty(txtGetVi.getText()) || TextUtils.isEmpty(txtGetEn.getText()) || TextUtils.isEmpty(txtGetFr.getText()))
        {
            Toast.makeText(add_word.this, getString(R.string.faild_message),Toast.LENGTH_LONG).show();
        }
        else
        {
            ContentValues row = new ContentValues();
            row.put("English",txtGetEn.getText().toString().toLowerCase());
            row.put("Vietnamese", txtGetVi.getText().toString().toLowerCase());
            row.put("French", txtGetFr.getText().toString().toLowerCase());
            row.put("SpecWords",txtGetSpec.getText().toString().toLowerCase());
            row.put("Favorite", "0");
            row.put("IsSearched", "0");
            long r = database.insert("Sheet1",null, row);
            if(r>0)
            {
                Toast.makeText(add_word.this, getString(R.string.succesful_message), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addControl() {
        txtGetEn = findViewById(R.id.txtGetEn);
        txtGetVi = findViewById(R.id.txtGetVi);
        txtGetFr = findViewById(R.id.txtGetFr);
        txtGetSpec = findViewById(R.id.txtGetSpec);
        btnAdd = findViewById(R.id.btnAdd);
        btnClear = findViewById(R.id.btnClear);
    }
}
