package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    EditText editText;
    Button addButton;
    Button buttonSearch;
    GridView gridView;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    public static ArrayList<String> listItems = new ArrayList<String>();



    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE GRIDTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int counterClick=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = (EditText) findViewById(R.id.typeIngredient);
        addButton = (Button) findViewById(R.id.addIngredient);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        gridView = (GridView) findViewById(R.id.gridSearch);
        listItems.clear();
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.edit_text_custom_for_ingredients, listItems);
        gridView.setAdapter(adapter);

//        added a click listener on the item of the gridview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //delete element onclick
                Object o = gridView.getItemAtPosition(position);
                listItems.remove(position);
                counterClick--;
                adapter.notifyDataSetChanged();

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(editText.getText().toString())){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please insert the text", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {

                    if(counterClick<10){


                        if(listItems.indexOf(editText.getText().toString().trim())==-1){
                            listItems.add(editText.getText().toString().trim().toLowerCase());
                            editText.setText("");
                            counterClick++;

                            adapter.notifyDataSetChanged();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Repeat",Toast.LENGTH_SHORT).show();
                        }

                        }
                    else
                    {
                        addButton.setClickable(false);
                        Toast toast = Toast.makeText(getApplicationContext(), "Max number of ingredients reached", Toast.LENGTH_SHORT);
                        toast.show();


                    }

                }
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItems.isEmpty()){ //if user didin't put any ingredient display error message
                    Toast toast = Toast.makeText(getApplicationContext(), "You didn't insert any ingredient", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                    startActivity(intent);

                }

                }
        });

    }



}
