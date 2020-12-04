package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RecipeDisplayActivity extends AppCompatActivity {

    ImageView imageView;

    CheckBox likecheckbox;
    CheckBox reportCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

    }


    @Override
    protected void onStart() {
        super.onStart();
        reportCheckbox=findViewById(R.id.checkbox_Report);
        final TextView recipeNametxt = (TextView) findViewById(R.id.txt_recipeName);
        final TextView authortxt = (TextView) findViewById(R.id.txt_recipeAuthor);
        final TextView countrytxt = (TextView) findViewById(R.id.txt_recipeCountry);
        final TextView ingredientstxt = (TextView) findViewById(R.id.txt_recipeIngredients);
        final TextView descriptiontxt = (TextView) findViewById(R.id.txt_recipeDescription);
        final TextView howtotxt = (TextView) findViewById(R.id.txt_recipeHowto);
        likecheckbox = (CheckBox) findViewById(R.id.chk_recipeLike);
        imageView = findViewById(R.id.imgfinal);

        String ingredientListString = "";
        String name, author, description, howTo, countryname;

        Intent intent = getIntent();
        s = intent.getStringExtra("transition");
        name = intent.getStringExtra("recipeName");
        author = intent.getStringExtra("author");
        description = intent.getStringExtra("description");
        howTo = intent.getStringExtra("howTo");
        ingredientListString = intent.getStringExtra("ingredientListStr");
        countryname = intent.getStringExtra("country");
        String imageURL = intent.getStringExtra("imageURL");
        Glide.with(getApplicationContext()).load(imageURL).into(imageView);

        final String key = intent.getStringExtra("key");

        recipeNametxt.setText(name);
        authortxt.setText(author);
        descriptiontxt.setText(description);
        howtotxt.setText(howTo);
        ingredientstxt.setText(ingredientListString);
        countrytxt.setText(countryname);
        likeFun(key);
        checkbox(key);

    }

    DatabaseReference db1;

    void likeFun(final String key) {
        db1 = FirebaseDatabase.getInstance().getReference("RecipeLike/" + key);
        //get data from SHAREDPREFERENCE
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String mainauthor = sh.getString("no", "0");
        //initial check or unchecked look into firebase
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    db1.child(mainauthor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
//                                Toast.makeText(getApplicationContext(),"Author FOUND",Toast.LENGTH_SHORT).show();
                                likecheckbox.setChecked(true);
                            } else {
                                likecheckbox.setChecked(false);
//                                Toast.makeText(getApplicationContext(),"Author not FOUND",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
//                    Toast.makeText(getApplicationContext(),"Recipe NOT FOUND",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        likecheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likecheckbox.isChecked()) {
                    db1.child(mainauthor).setValue("1");
                } else {
                    db1.child(mainauthor).removeValue();
                }
            }
        });

    }

    String s;

DatabaseReference db2;

    void checkbox(String key)
    {
        db2 = FirebaseDatabase.getInstance().getReference("RecipeReport/" + key);
        //get data from SHAREDPREFERENCE
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String mainauthor = sh.getString("no", "0");
        //initial check or unchecked look into firebase
        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    db2.child(mainauthor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
//                                Toast.makeText(getApplicationContext(),"Author FOUND",Toast.LENGTH_SHORT).show();
                                reportCheckbox.setChecked(true);
                            } else {
                                reportCheckbox.setChecked(false);
//                                Toast.makeText(getApplicationContext(),"Author not FOUND",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
//                    Toast.makeText(getApplicationContext(),"Recipe NOT FOUND",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        reportCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportCheckbox.isChecked()) {
                    db2.child(mainauthor).setValue("1");
                } else {
                    db2.child(mainauthor).removeValue();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (s.equalsIgnoreCase("MainActivity")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else if (s.equalsIgnoreCase("Category")) {
            startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
            finish();
        } else if(s.equalsIgnoreCase("ReportRecipe")) {
            startActivity(new Intent(getApplicationContext(), ReportedRecipes.class));
            finish();
        }
        else if(s.equalsIgnoreCase("SearchResult"))
        {
            startActivity(new Intent(getApplicationContext(), SearchResult.class));
            finish();
        }
        else
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


    }

}
