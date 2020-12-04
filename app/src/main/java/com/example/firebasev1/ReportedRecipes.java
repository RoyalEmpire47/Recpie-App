package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportedRecipes extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<DummyData> reportedRecipesList=new ArrayList<>();
    ArrayList<String>reportedkeyList=new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_recipes);
        listView=findViewById(R.id.listview_reported_recipes);
    }

    AdapterRecipeNameImgAuthLike adapterRecipeNameImgAuthLike;
    @Override
    protected void onStart() {
        super.onStart();
        reportedRecipesList.clear();
        reportedkeyList.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("RecipeReport");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapShot : dataSnapshot.getChildren()) {
//                    Toast.makeText(getApplicationContext(),recipeSnapShot.getKey().toString(),Toast.LENGTH_LONG).show();
                    reportedkeyList.add(recipeSnapShot.getKey());
                }
                final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Recipe");
                for (int i = 0; i < reportedkeyList.size(); i++) {
                    databaseReference1.child(reportedkeyList.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String country = dataSnapshot.child("country").getValue(String.class);
                            String recipeName = dataSnapshot.child("recipeName").getValue(String.class);
                            String author = dataSnapshot.child("author").getValue(String.class);
                            String description = dataSnapshot.child("recipeDescription").getValue(String.class);
                            String recipeHowTo = dataSnapshot.child("recipeHowTo").getValue(String.class);
                            String imageUrl = dataSnapshot.child("imageURL").getValue(String.class);
                            String ingredientListStr = dataSnapshot.child("ingredientListStr").getValue(String.class);

                            DummyData dummyData = new DummyData(recipeName, author, description, recipeHowTo, country, imageUrl, ingredientListStr);
                            reportedRecipesList.add(dummyData);
                            adapterRecipeNameImgAuthLike = new AdapterRecipeNameImgAuthLike(getApplicationContext(), reportedRecipesList,reportedkeyList,1);
                            listView.setAdapter(adapterRecipeNameImgAuthLike);
                            adapterRecipeNameImgAuthLike.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        sendDataToDisplay(position,reportedRecipesList,reportedkeyList);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        //remove from recipe
                        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Recipe");
                        db.child(reportedkeyList.get(position)).removeValue();
                        //remove from recipelike
                        db=FirebaseDatabase.getInstance().getReference("RecipeLike");
                        db.child(reportedkeyList.get(position)).removeValue();
                        //remove from recipeReport
                        db=FirebaseDatabase.getInstance().getReference("RecipeReport");
                        db.child(reportedkeyList.get(position)).removeValue();

                        reportedRecipesList.remove(position);
                        reportedkeyList.remove(position);
                        adapterRecipeNameImgAuthLike.notifyDataSetChanged();
                        return true;
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    void sendDataToDisplay(int position, ArrayList<DummyData> recipeList, ArrayList<String> keyList) {
        Intent intent = new Intent(getApplicationContext(), RecipeDisplayActivity.class);
        intent.putExtra("key", keyList.get(position));

        intent.putExtra("recipeName", recipeList.get(position).getRecipeName());
        intent.putExtra("author", recipeList.get(position).getAuthor());
        intent.putExtra("description", recipeList.get(position).getRecipeDescription());
        intent.putExtra("howTo", recipeList.get(position).getRecipeHowTo());
        intent.putExtra("imageURL", recipeList.get(position).getImageURL());
        intent.putExtra("ingredientListStr", recipeList.get(position).ingredientListStr);
        intent.putExtra("country", recipeList.get(position).getCountry());
        intent.putExtra("transition", "ReportRecipe");
        startActivity(intent);
        finish();
    }
}
