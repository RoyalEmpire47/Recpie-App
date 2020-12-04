package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AddRecipeSubmitActivity extends AppCompatActivity {
    String RecipeName, RecipeDescription, Country, ImageURL, howToCook;
    EditText howToCookedt;
    Button btnAddRecipe;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_submit);
        TextView txtInfoHowTo = (TextView) findViewById(R.id.txt_HowTo);
        howToCookedt = (EditText) findViewById(R.id.edtHowTo);
        btnAddRecipe = (Button) findViewById(R.id.btnSubmit);
        Bundle b = getIntent().getExtras();

        RecipeName = b.getString("RecipeName");
        RecipeDescription = b.getString("RecipeDescription");
        Country = b.getString("Country");
        ImageURL = b.getString("ImageURL");


        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(howToCookedt.getText().toString())) {
                    howToCook = howToCookedt.getText().toString();
                    insertData();

                    Toast.makeText(getApplicationContext(), "Recipe Added Sucessfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    String uniqueKey = "";

    void insertData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");
        //getAuthor from shared preference

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
        final String mainauthor = sh.getString("name", "oo");
        final DummyData obj = new DummyData(RecipeName, mainauthor, RecipeDescription, howToCook, Country, ImageURL, AddIngredientListActivity.Str);


        //unique key
        uniqueKey = databaseReference.push().getKey();
        databaseReference.child(System.currentTimeMillis() + "-" + RecipeName).setValue(obj);


        //validate uploaded or not
//        Toast.makeText(getApplicationContext(),"Upload done",Toast.LENGTH_SHORT).show();
    }

}
