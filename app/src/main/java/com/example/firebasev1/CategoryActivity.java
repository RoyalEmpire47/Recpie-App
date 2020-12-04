package com.example.firebasev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class CategoryActivity extends AppCompatActivity {

    ListView listViewCateogory;
    int imgNameArray[];
    String CateogoryNames[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        imgNameArray = new int[]{R.drawable.indian, R.drawable.russia, R.drawable.usa, R.drawable.korea, R.drawable.itly};
        CateogoryNames = new String[]{"India", "Russia", "USA", "Korea", "Italy"};
        listViewCateogory = (ListView) findViewById(R.id.cateogoryListView);

        AdapterCateogoryNames adapter = new AdapterCateogoryNames(getApplicationContext(), CateogoryNames, imgNameArray);
        listViewCateogory.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
