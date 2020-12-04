package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {
    ListView listView1, listView2;
    ArrayList<DummyData> searchResultRecipeList2 = new ArrayList<DummyData>();
    ArrayList<DummyData> searchResultRecipeList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        listView1 = findViewById(R.id.searchListView);
        listView2 = findViewById(R.id.searchListView1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView txtJustMatch=findViewById(R.id.txtJustMatch);
        TextView txtBestMatch=findViewById(R.id.txtBestMatch);
        search();
        if(searchResultRecipeList1.isEmpty() && searchResultRecipeList2.isEmpty())
        {
            txtBestMatch.setText("");
            txtJustMatch.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
            txtJustMatch.setText("Sorry, we didn't match any ingredient! Take a look of our best Recipes!");
            getDataBest();
        }



    }

    void search() {
        final ArrayList<String> ingredientList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchResultRecipeList1.clear();
                searchResultRecipeList2.clear();

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    ingredientList.clear();
                    //
                    int count = 0, mincount = 0;

                    String ingredients = recipeSnapshot.child("ingredientListStr").getValue(String.class);

                    String ingredientsStr[] = ingredients.split(",");

                    for (String s : ingredientsStr) {
                        ingredientList.add(s.trim());
                    }
                    mincount = SearchActivity.listItems.size() / 2 - 1;
                    for (String a : SearchActivity.listItems) {
                        if (ingredientList.contains(a)) {
                            count++;
                        }

                    }
                    String recipeName = recipeSnapshot.child("recipeName").getValue(String.class);
                    String author = recipeSnapshot.child("author").getValue(String.class);
                    String description = recipeSnapshot.child("recipeDescription").getValue(String.class);
                    String recipeHowTo = recipeSnapshot.child("recipeHowTo").getValue(String.class);
                    String imageUrl = recipeSnapshot.child("imageURL").getValue(String.class);
                    String country = recipeSnapshot.child("country").getValue(String.class);
                    String ingredientListStr = recipeSnapshot.child("ingredientListStr").getValue(String.class);


                    DummyData dummyData = new DummyData(recipeName, author, description, recipeHowTo, country, imageUrl, ingredientListStr);
                    DummyData.keyList.add(recipeSnapshot.getKey());

                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), RecipeDisplayActivity.class);
                            intent.putExtra("key", DummyData.keyList.get(position));
//                        intent.putExtra("pos",position+"");
                            intent.putExtra("recipeName", searchResultRecipeList1.get(position).getRecipeName());
                            intent.putExtra("author", searchResultRecipeList1.get(position).getAuthor());
                            intent.putExtra("description", searchResultRecipeList1.get(position).getRecipeDescription());
                            intent.putExtra("howTo", searchResultRecipeList1.get(position).getRecipeHowTo());
                            intent.putExtra("imageURL", searchResultRecipeList1.get(position).getImageURL());
                            intent.putExtra("ingredientListStr", searchResultRecipeList1.get(position).ingredientListStr);
                            intent.putExtra("country", searchResultRecipeList1.get(position).getCountry());
                            startActivity(intent);

                        }
                    });
//
//                    if (count <= 2 && count > 0) {
//                        count = 0;
//                        searchResultRecipeList1.add(dummyData);
//
//                    }

                    if (count > (ingredientList.size()/2)) {
                        count = 0;
                        searchResultRecipeList2.add(dummyData);
                    }
                    else if(count>0)
                    {
                        count=0;
                        searchResultRecipeList1.add(dummyData);
                    }


                }

                AdapterRecipeNameImgAuthLike adapterRecipeNameImgAuthLike2 = new AdapterRecipeNameImgAuthLike(getApplicationContext(), searchResultRecipeList2,DummyData.keyList,0);
                listView2.setAdapter(adapterRecipeNameImgAuthLike2);
                adapterRecipeNameImgAuthLike2.notifyDataSetChanged();

                AdapterRecipeNameImgAuthLike adapterRecipeNameImgAuthLike1 = new AdapterRecipeNameImgAuthLike(getApplicationContext(), searchResultRecipeList1,DummyData.keyList,0);
                listView1.setAdapter(adapterRecipeNameImgAuthLike1);
                adapterRecipeNameImgAuthLike1.notifyDataSetChanged();



                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), RecipeDisplayActivity.class);
                        intent.putExtra("key", DummyData.keyList.get(position));
//                        intent.putExtra("pos",position+"");
                        intent.putExtra("recipeName", searchResultRecipeList2.get(position).getRecipeName());
                        intent.putExtra("author", searchResultRecipeList2.get(position).getAuthor());
                        intent.putExtra("description", searchResultRecipeList2.get(position).getRecipeDescription());
                        intent.putExtra("howTo", searchResultRecipeList2.get(position).getRecipeHowTo());
                        intent.putExtra("imageURL", searchResultRecipeList2.get(position).getImageURL());
                        intent.putExtra("ingredientListStr", searchResultRecipeList2.get(position).ingredientListStr);
                        intent.putExtra("country", searchResultRecipeList2.get(position).getCountry());
                        startActivity(intent);

                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    ArrayList<Integer> arrayListlike = new ArrayList<>();
    ArrayList<DummyData> arrayListBest = new ArrayList<>();
    ArrayList<String> arrayListKey = new ArrayList<>();
    void getDataBest() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RecipeLike");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListKey.clear();
                arrayListlike.clear();
                arrayListBest.clear();
                for (DataSnapshot recipeLikeSnapShot : dataSnapshot.getChildren()) {

                    arrayListKey.add(recipeLikeSnapShot.getKey());
                    arrayListlike.add((int) recipeLikeSnapShot.getChildrenCount());
                }

                for (int i = 0; i < arrayListKey.size() - 1; i++) {
                    for (int j = i + 1; j < arrayListKey.size(); j++)
                        if (arrayListlike.get(i) < arrayListlike.get(j)) {
                            int t = arrayListlike.get(i);
                            arrayListlike.set(i, arrayListlike.get(j));
                            arrayListlike.set(j, t);

                            String temp = arrayListKey.get(i);
                            arrayListKey.set(i, arrayListKey.get(j));
                            arrayListKey.set(j, temp);
                        }
                }
//                for (int i = 3; i < arrayListKey.size(); i++) {
//                    arrayListKey.remove(i);
//                    arrayListlike.remove(i);
//                }

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Recipe");
                for (int i = 0; i < arrayListKey.size(); i++) {
                    databaseReference1.child(arrayListKey.get(i)).addValueEventListener(new ValueEventListener() {
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
                            arrayListBest.add(dummyData);

                           final AdapterRecipeNameImgAuthLike adapterRecipeNameImgAuthLike=new AdapterRecipeNameImgAuthLike(getApplicationContext(),arrayListBest,arrayListKey,0);

                            listView2.setAdapter(adapterRecipeNameImgAuthLike);
                            adapterRecipeNameImgAuthLike.notifyDataSetChanged();


                            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    adapterRecipeNameImgAuthLike.notifyDataSetChanged();
                                    sendDataToDisplay(position, arrayListBest, arrayListKey);

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

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
        intent.putExtra("transition", "SearchResult");
        startActivity(intent);
        finish();
    }

}
