package com.example.dan.imtdictionary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TabHost tabHost;
    ImageButton btnFavorite;
    ImageButton btnPlus;
    ImageButton btnSpec;
    ImageButton btnSpeak, btnSpeakTrans;
    ImageButton btnGame1;
    SeekBar skPitch, skSpeed;
    AutoCompleteTextView txtInput;
    EditText txtOutput;
    Button btnTrans;
    RadioButton radEn1, radEn2, radVi1, radVi2, radFr1, radFr2,radSpec;
    ArrayList<String> enArray;
    ArrayAdapter<String> enArrayAdapter;
    ArrayList<String> viArray;
    ArrayAdapter<String> viArrayAdapter;
    ArrayList<String> frArray;
    ArrayAdapter<String> frArrayAdapter;
    ArrayList<String> specArray;
    ArrayAdapter<String> specArrayAdapter;
    TextToSpeech mttS;
    TextToSpeech textToSpeech;

    ListView lvFavorite;
    ArrayList<String> favoriteArray;
    ArrayAdapter<String> favoriteAdapter;

    ListView lvHistory;
    ArrayList<String> historyArray;
    ArrayAdapter<String> historyAdapter;

    public String DATABASE_NAME="wordsList.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        copyDataBasetoMobile();

        addControl();
        addEvents();

        showHistoryWords();
        showFavoriteWords();
        createWordsList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showHistoryWords();
        showFavoriteWords();
        createWordsList();
    }

    private void addEvents() {
        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDef();
                executeHistory();
            }
        });
        txtInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDef();
                executeHistory();
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFavorite();
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMenu();
            }
        });
        radEn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput.setAdapter(enArrayAdapter);
            }
        });
        radVi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput.setAdapter(viArrayAdapter);
            }
        });
        radFr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput.setAdapter(frArrayAdapter);
            }
        });
        radSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput.setAdapter(specArrayAdapter);
            }
        });
        radEn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtInput.getText().toString()!= "")
                {
                    getDef();
                }
            }
        });
        radVi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtInput.getText().toString()!= "")
                {
                    getDef();
                }
            }
        });
        radFr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtInput.getText().toString()!= "")
                {
                    getDef();
                }
            }
        });
        lvHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteHis(position);
                return false;
            }
        });
        lvFavorite.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteFav(position);
                return false;
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(0);
            }
        });
        btnSpeakTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(1);
            }
        });
        btnGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGame1Menu();
            }
        });
        btnSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpecMenu();
            }
        });
    }




    private void addControl() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 =  tabHost.newTabSpec("t1");
        tab1.setIndicator("",getResources().getDrawable(R.drawable.icon_translate));
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 =  tabHost.newTabSpec("t2");
        tab2.setIndicator("",getResources().getDrawable(R.drawable.icon_favorite));
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3 =  tabHost.newTabSpec("t3");
        tab3.setIndicator("",getResources().getDrawable(R.drawable.icon_history));
        tab3.setContent(R.id.tab3);
        tabHost.addTab(tab3);


        txtInput = findViewById(R.id.txtInput);
        txtOutput = findViewById(R.id.txtOutput);
        btnTrans = findViewById(R.id.btnTrans);
        radEn1 = findViewById(R.id.radEn1);
        radEn2 = findViewById(R.id.radEn2);
        radVi1 = findViewById(R.id.radVi1);
        radVi2 = findViewById(R.id.radVi2);
        radFr1 = findViewById(R.id.radFr1);
        radFr2 = findViewById(R.id.radFr2);
        radSpec= findViewById(R.id.radSpec);
        radEn1.setChecked(true);
        radVi2.setChecked(true);
        btnSpec = findViewById(R.id.btnSpec);
        btnFavorite = findViewById(R.id.btnFavortie);
        btnPlus = findViewById(R.id.btnPlus);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnSpeakTrans = findViewById(R.id.btnSpeakTrans);
        btnGame1 = findViewById(R.id.btnGame1);
        skPitch = findViewById(R.id.skPitch);
        skSpeed = findViewById(R.id.skSpeed);
        enArray = new ArrayList<>();
        enArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,enArray);
        viArray = new ArrayList<>();
        viArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,viArray);
        frArray = new ArrayList<>();
        frArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,frArray);
        specArray = new ArrayList<>();
        specArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,specArray);
        txtInput.setAdapter(enArrayAdapter);
        mttS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mttS.setLanguage(Locale.ENGLISH);
            }
        });
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.FRENCH);
            }
        });

        lvHistory = findViewById(R.id.lvHistory);
        historyArray = new ArrayList<>();
        historyAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, historyArray);
        lvHistory.setAdapter(historyAdapter);

        lvFavorite = findViewById(R.id.lvFavorite);
        favoriteArray = new ArrayList<>();
        favoriteAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, favoriteArray);
        lvFavorite.setAdapter(favoriteAdapter);

        Intent intent = getIntent();
        String result = intent.getStringExtra("txtInput");
        Boolean isSpec = intent.getBooleanExtra("isSpec", false);
        if(isSpec)
        {
            radSpec.setChecked(true);
            radEn2.setChecked(true);
        }
        txtInput.setText(result);
    }

    private void speak(int i) {
        String text;
        if(i == 0) {
            text = txtInput.getText().toString();
            if(radEn1.isChecked()||radVi1.isChecked()||radSpec.isChecked()) {outputLanguage(mttS, text);}
            else{outputLanguage(textToSpeech, text);}
        }
        else{
            text = txtOutput.getText().toString();
            if(radEn2.isChecked()||radVi2.isChecked()) {outputLanguage(mttS, text);}
            else{outputLanguage(textToSpeech, text);}
        }
    }

    private void outputLanguage(TextToSpeech t, String text) {
        float pitch = (float) skPitch.getProgress() / 50;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) skSpeed.getProgress() / 50;
        if (speed < 0.1) pitch = 0.1f;
        t.setPitch(pitch);
        t.setSpeechRate(speed);
        t.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    private void getDef()
    {
        if(!TextUtils.isEmpty(txtInput.getText())) {
            try {
                int index;
                String temp = txtInput.getText().toString().toLowerCase().trim();
                if (radEn1.isChecked()) {
                    index = enArray.indexOf(temp);
                } else if (radVi1.isChecked()) {
                    index = viArray.indexOf(temp);
                } else if (radFr1.isChecked()){
                    index = frArray.indexOf(temp);
                }
                else{
                    index = specArray.indexOf(temp);
                }
                getMeaning(index);
            }
            catch(Exception e)
            {
                txtOutput.setText(txtInput.getText().toString());
            }
        }
    }

    private void getMeaning(int position) {
        if(radEn2.isChecked()) {
            txtOutput.setText(enArray.get(position).toLowerCase());
        }
        else if(radVi2.isChecked())
        {
            txtOutput.setText(viArray.get(position).toLowerCase());
        }
        else if(radFr2.isChecked())
        {
            txtOutput.setText(frArray.get(position).toLowerCase());
        }
    }

    private void showHistoryWords() {
        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = database.query("Sheet1",null,"IsSearched = ?",new String[]{"1"},null,null,null);
        historyArray.clear();
        while(cursor.moveToNext())
        {
            String en = cursor.getString(0);
            String vi = cursor.getString(1);
            String fr = cursor.getString(2);
            String spec = cursor.getString(5);
            historyArray.add(getString(R.string.radEn) + ": " + en + " \n" +getString(R.string.radVi) + ": " + vi  + "\n" + getString(R.string.radFr) + ": "+ fr +"\n" + getString(R.string.radSpec)+ ": " + spec);
        }
        cursor.close();
        historyAdapter.notifyDataSetChanged();
    }

    private void showFavoriteWords()
    {
        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = database.query("Sheet1",null,"Favorite = ?",new String[]{"1"},null,null,null);
        favoriteArray.clear();
        while(cursor.moveToNext())
        {
            String en = cursor.getString(0);
            String vi = cursor.getString(1);
            String fr = cursor.getString(2);
            String spec = cursor.getString(5);
            favoriteArray.add(getString(R.string.radEn) + ": " + en + " \n" +getString(R.string.radVi) + ": " + vi  + "\n" + getString(R.string.radFr) + ": "+ fr + "\n" + getString(R.string.radSpec)+ ": " +spec);
        }
        cursor.close();
        favoriteAdapter.notifyDataSetChanged();
    }

    private void executeFavorite() {
        ContentValues row = new ContentValues();
        row.put("Favorite", 1);
        String index = txtInput.getText().toString().toLowerCase().trim();
        if(TextUtils.isEmpty(index)){return;}
        else if (radEn1.isChecked()) {database.update("Sheet1",row, "English = ?", new String[]{index});}
        else if (radVi1.isChecked()){database.update("Sheet1",row, "Vietnamese = ?", new String[]{index});}
        else if (radFr1.isChecked()){database.update("Sheet1",row, "French = ?", new String[]{index});}
        else{
            database.update("Sheet1", row,"SpecWords = ?",new String[]{index});
        }
        showFavoriteWords();
    }

    private void executeHistory() {
        ContentValues row = new ContentValues();
        row.put("IsSearched", 1);
        String index = txtInput.getText().toString().toLowerCase().trim();
        if (radEn1.isChecked()) {
            database.update("Sheet1", row, "English = ?", new String[]{index});
        } else if (radVi1.isChecked()) {
            database.update("Sheet1", row, "Vietnamese = ?", new String[]{index});
        } else if (radFr1.isChecked()) {
            database.update("Sheet1", row, "French = ?", new String[]{index});
        } else {
            database.update("Sheet1", row, "SpecWords = ?", new String[]{index});
        }
        showHistoryWords();
    }


    private void createWordsList()
    {
        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = database.query("Sheet1",null,null,null,null,null,null);
        enArray.clear();
        viArray.clear();
        frArray.clear();
        specArray.clear();
        while(cursor.moveToNext()) {
            enArray.add(cursor.getString(0));
            viArray.add(cursor.getString(1));
            frArray.add(cursor.getString(2));
            specArray.add(cursor.getString(5));
        }
        cursor.close();
        enArrayAdapter.notifyDataSetChanged();
        viArrayAdapter.notifyDataSetChanged();
        frArrayAdapter.notifyDataSetChanged();
        specArrayAdapter.notifyDataSetChanged();
    }
    private void deleteHis(int position) {
        ContentValues row = new ContentValues();
        row.put("IsSearched", 0);
        String temp = historyArray.get(position);
        int start = temp.indexOf(":");
        int end = temp.indexOf("\n");
        temp = temp.substring(start+2, end-1);
        database.update("Sheet1", row, "English = ?", new String[]{temp});
        showHistoryWords();
    }

    private void deleteFav(int position) {
        ContentValues row = new ContentValues();
        row.put("Favorite", 0);
        String temp = favoriteArray.get(position);
        String []line = temp.split(" ");
        int start = temp.indexOf(":");
        int end = temp.indexOf("\n");
        temp = temp.substring(start+2 , end-1);
        database.update("Sheet1", row, "English = ?", new String[]{temp});
        showFavoriteWords();
    }

    private void copyDataBasetoMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copy Success", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void CopyDataBaseFromAsset() {
        try{
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getSavePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists())
            {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex)
        {
            Log.e("Copy Database Error ",ex.toString());
        }
    }

    private String getSavePath(){
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    private void openGame1Menu() {
        Intent intent = new Intent(MainActivity.this, remember_word_game.class);
        startActivity(intent);
    }
    private void openSpecMenu()
    {
        Intent intent = new Intent(MainActivity.this, specialized_words.class);
        startActivity(intent);
    }

    private void openAddMenu() {
        Intent intent = new Intent(MainActivity.this, add_word.class);
        startActivity(intent);
    }
}
