package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.firebasev1.DummyData.recipeList;

public class ShowCountryWiseRecipeActivity extends AppCompatActivity {

    String tempCountryNames[];


    TextView txtTitle;
    ListView listView;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_country_wise_recipe);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //clear the old recipeList
        recipeList.clear();
        DummyData.keyList.clear();
        Intent intent = getIntent();

        //connect with screen elements
        txtTitle = (TextView) findViewById(R.id.txt_recipeListTitle);
        listView = (ListView) findViewById(R.id.listview_recipelist);

        if (intent.hasExtra("category")) {

            title = intent.getStringExtra("category");
            //set product list title from intent key "category"
            txtTitle.setText(title);
            DataFetch();
//            Toast.makeText(getApplicationContext(),"Fetch"+recipeList.get(0),Toast.LENGTH_LONG).show();
        }
    }

    public void DataFetch() {


        Query query = FirebaseDatabase.getInstance().getReference("Recipe").
                orderByChild("country").equalTo(title);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {


                    String country = recipeSnapshot.child("country").getValue(String.class);

//                    if(country.equalsIgnoreCase(title)){
                    String recipeName = recipeSnapshot.child("recipeName").getValue(String.class);
                    String author = recipeSnapshot.child("author").getValue(String.class);
                    String description = recipeSnapshot.child("recipeDescription").getValue(String.class);
                    String recipeHowTo = recipeSnapshot.child("recipeHowTo").getValue(String.class);
                    String imageUrl = recipeSnapshot.child("imageURL").getValue(String.class);
                    String ingredientListStr = recipeSnapshot.child("ingredientListStr").getValue(String.class);

                    DummyData dummyData = new DummyData(recipeName, author, description, recipeHowTo, country, imageUrl, ingredientListStr);
                    recipeList.add(dummyData);

                    final String key = recipeSnapshot.getKey();

                    //get like count
                    //
                    DummyData.keyList.add(key);
//                    }

                }

                AdapterRecipeNameImgAuthLike adapter = new AdapterRecipeNameImgAuthLike(getApplicationContext(), recipeList,DummyData.keyList,0);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), RecipeDisplayActivity.class);
                        intent.putExtra("key", DummyData.keyList.get(position));
//                        intent.putExtra("pos",position+"");
                        intent.putExtra("recipeName", recipeList.get(position).getRecipeName());
                        intent.putExtra("author", recipeList.get(position).getAuthor());
                        intent.putExtra("description", recipeList.get(position).getRecipeDescription());
                        intent.putExtra("howTo", recipeList.get(position).getRecipeHowTo());
                        intent.putExtra("imageURL", recipeList.get(position).getImageURL());
                        intent.putExtra("ingredientListStr", recipeList.get(position).ingredientListStr);
                        intent.putExtra("country", recipeList.get(position).getCountry());
                        intent.putExtra("transition", "Category");
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "" + databaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

//        Toast.makeText(getApplicationContext(),""+recipeList.get(0).RecipeName,Toast.LENGTH_LONG).show();
    }
}
